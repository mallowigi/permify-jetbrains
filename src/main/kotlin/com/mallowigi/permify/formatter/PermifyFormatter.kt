package com.mallowigi.permify.formatter

import com.mallowigi.permify.highlighter.PermifyElementType
import com.mallowigi.permify.highlighter.PermifyHighlightingLexer

data class PermifyFormatterOptions(
  val useTabCharacter: Boolean = false,
  val indentSize: Int = 2,
  val maxBlankLines: Int = 1,
  val spaceAroundOperators: Boolean = true,
  val spaceAroundBraces: Boolean = true
)

object PermifyFormatter {
  // Represents a segment of text and whether it is protected (inside a comment or string literal)
  private data class Segment(val text: String, val isProtected: Boolean)

  private val KEYWORDS = setOf(
    "entity",
    "relation",
    "permission",
    "action",
    "attribute",
    "rule",
    "return",
    "and",
    "or",
    "not",
    "in"
  )

  fun debugTokens(text: String) {
    val lexer = PermifyHighlightingLexer()
    lexer.start(text)
    while (lexer.tokenType != null) {
      val tokenText = text.substring(lexer.tokenStart, lexer.tokenEnd)
      val scope = (lexer.tokenType as? PermifyElementType)?.getScope()

      println("[$scope] -> ${tokenText.replace("\n", "\\n")}")
      lexer.advance()
    }
  }

  fun protectedRanges(text: String): BooleanArray {
    val protected = BooleanArray(text.length)
    val lexer = PermifyHighlightingLexer()
    lexer.start(text)

    // Mark ranges that are inside comments or string literals as protected so that they are not modified during formatting
    while (lexer.tokenType != null) {
      val scope = (lexer.tokenType as? PermifyElementType)?.getScope()?.toString() ?: ""
      // If we are inside a comment or a literal
      if (scope.contains("comment") || scope.contains("string")) {
        for (i in lexer.tokenStart until lexer.tokenEnd) {
          protected[i] = true
        }
      }
      lexer.advance()
    }
    return protected
  }

  private fun toSegments(text: String, protected: BooleanArray): List<Segment> {
    val segments = mutableListOf<Segment>()
    var i = 0
    while (i < text.length) {
      val start = i
      val isProtected = protected[i]

      while (i < text.length && protected[i] == isProtected) i++

      segments.add(Segment(text.substring(start, i), isProtected))
    }
    return segments
  }

  fun normalizeSpacing(text: String, options: PermifyFormatterOptions = PermifyFormatterOptions()): String {
    val protected = protectedRanges(text)
    val segments = toSegments(text, protected)

    return segments.joinToString("") {
      when {
        it.isProtected -> it.text
        else -> normalizeText(it.text, options)
      }
    }
  }

  private fun normalizeText(text: String, options: PermifyFormatterOptions = PermifyFormatterOptions()): String {
    var result = text

    // Replace multiple spaces with a single space
    result = result.replace(Regex("[ \t]+"), " ")

    // Strip trailing whitespace on each line so whitespace-only lines become truly empty
    // (otherwise they aren't a literal run of "\n" and the blank-line collapse below misses them)
    result = result.replace(Regex("[ \t]+\n"), "\n")

    // Collapse 2+ blank lines (3+ consecutive newlines) down to exactly one blank line
    result = result.replace(Regex("\n{3,}"), "\n".repeat(options.maxBlankLines + 1))

    // Remove spaces around braces and parentheses
    if (options.spaceAroundBraces) {
      result = result.replace(Regex("[ \t]*([()])[ \t]*"), "$1")
    }

    // Add spaces between operators
    if (options.spaceAroundOperators) {
      result = result.replace(Regex("[ \t]*([=<>!&|]+)[ \t]*"), " $1 ")
    }


    // Add spaces around keywords
    for (keyword in KEYWORDS) {
      result = result.replace(Regex("\\b$keyword\\b(?!\\s)"), " $keyword ")
    }

    return result
  }

  fun format(text: String, options: PermifyFormatterOptions = PermifyFormatterOptions()): String {
    val normalized = normalizeSpacing(text, options)
    val protected = protectedRanges(normalized)
    val lines = normalized.split("\n")
    val result = StringBuilder()
    var depth = 0
    var offset = 0

    for ((index, line) in lines.withIndex()) {
      val lineStart = offset
      val trimmed = line.trim()
      // Count the number of opening and closing braces in the line
      var opens = 0
      var closes = 0

      // Count the opens and closing braces in the line
      for (i in line.indices) {
        val absIndex = lineStart + i
        if (protected[absIndex]) continue
        when (line[i]) {
          '{' -> opens++
          '}' -> closes++
        }
      }

      // Skip last line if it is empty to prevent adding newlines after it
      val isTrailingArtifact = trimmed.isEmpty() && index == lines.lastIndex
      if (!isTrailingArtifact) {
        // If the line starts with closing braces, we need to decrease the depth before printing the line
        val leadingCloses = trimmed.takeWhile { it == '}' }.length
        val indent = maxOf(0, depth - leadingCloses)
        val tabCharacter = if (options.useTabCharacter) "\t" else " ".repeat(options.indentSize)

        if (trimmed.isEmpty()) {
          result.append("")
        } else {
          result.append(tabCharacter.repeat(indent))
          result.append(trimmed)
        }
        result.append("\n")
      }

      depth += opens - closes
      offset += line.length + 1
    }

    return result.toString()
  }
}
