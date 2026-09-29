package com.mallowigi.permify.formatter

import com.mallowigi.permify.highlighter.PermifyElementType
import com.mallowigi.permify.highlighter.PermifyHighlightingLexer

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

  fun normalizeSpacing(text: String): String {
    val protected = protectedRanges(text)
    val segments = toSegments(text, protected)

    return segments.joinToString("") {
      when {
        it.isProtected -> it.text
        else -> normalizeText(it.text)
      }
    }
  }

  private fun normalizeText(text: String): String {
    var result = text

    // Replace multiple spaces with a single space
    result = result.replace(Regex("[ \t]+"), " ")

    // Remove spaces around braces and parentheses
    result = result.replace(Regex("[ \t]*([()])[ \t]*"), "$1")

    // Add spaces between operators
    result = result.replace(Regex("[ \t]*([=<>!&|]+)[ \t]*"), " $1 ")

    // Add spaces around keywords
    for (keyword in KEYWORDS) {
      result = result.replace(Regex("\\b$keyword\\b(?!\\s)"), " $keyword ")
    }

    return result
  }

  /**
   * Indentation formatter using depth
   */
  fun format(text: String): String {
    val normalized = normalizeSpacing(text)
    val protected = protectedRanges(normalized)
    val lines = normalized.split("\n")
    val result = StringBuilder()
    var depth = 0
    var offset = 0

    // Loop over lines
    for (line in lines) {
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

      // If the line starts with closing braces, we need to decrease the depth before printing the line
      val leadingCloses = trimmed.takeWhile { it == '}' }.length
      val indent = maxOf(0, depth - leadingCloses)

      if (trimmed.isEmpty()) {
        result.append("")
      } else {
        result.append("\t".repeat(indent))
        result.append(trimmed)
      }
      result.append("\n")

      depth += opens - closes
      offset += line.length + 1
    }

    // Remove trailing newlines and add a newline at the end of the file
    return result.toString().trimEnd('\n') + "\n"
  }
}
