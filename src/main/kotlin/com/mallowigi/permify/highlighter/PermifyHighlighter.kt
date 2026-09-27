package com.mallowigi.permify.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.settings.*

class PermifyHighlighter(private val lexer: PermifyHighlightingLexer) : SyntaxHighlighterBase() {
  override fun getHighlightingLexer(): Lexer = this.lexer

  /**
   * Convert textmate tokens to IJ tokens
   *
   * @param tokenType the type of token
   * @return the highlight attributes
   */
  override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey?> {
    if (tokenType !is PermifyElementType) return pack(null)
    val scope = tokenType.getScope().scopeName ?: ""
    if (scope.isEmpty()) return pack(null)

    return when {
      // comments
      scope.startsWith("comment.")     -> {
        when {
          scope.startsWith("comment.line.") -> pack(PERMIFY_COMMENT)
          else                              -> pack(PERMIFY_COMMENT)
        }
      }

      // strings
      scope.startsWith("string.")      -> {
        when {
          scope.startsWith("string.quoted") -> pack(PERMIFY_STRING)
          else                              -> pack(PERMIFY_STRING)
        }
      }

      // brackets, punctuation, operators
      scope.startsWith("punctuation.") -> {
        when {
          scope.startsWith("punctuation.definition.string")     -> pack(PERMIFY_STRING)
          scope.startsWith("punctuation.definition.parameters") -> pack(PERMIFY_BRACKETS)
          scope.startsWith("punctuation.separator.method")      -> pack(PERMIFY_DOT)
          else                                                  -> pack(PERMIFY_OPERATOR)
        }
      }

      // keywords
      scope.startsWith("keyword.")     -> {
        when {
          scope.startsWith("keyword.control.class")      -> pack(PERMIFY_ENTITY)
          scope.startsWith("keyword.control.relation")   -> pack(PERMIFY_RELATION)
          scope.startsWith("keyword.control.permission") -> pack(PERMIFY_PERMISSION)
          scope.startsWith("keyword.control")            -> pack(PERMIFY_KEYWORD)
          scope.startsWith("keyword.other.action")       -> pack(PERMIFY_ACTION)
          scope.startsWith("keyword.other.attribute")    -> pack(PERMIFY_ATTRIBUTE_KEYWORD)
          scope.startsWith("keyword.operator")           -> pack(PERMIFY_OPERATOR)
          else                                           -> pack(PERMIFY_KEYWORD)
        }
      }

      // constants
      scope.startsWith("constant.")    -> {
        when {
          scope.startsWith("constant.numeric")   -> pack(PERMIFY_NUMBER)
          scope.startsWith("constant.character") -> pack(PERMIFY_STRING)
          else                                   -> pack(PERMIFY_NUMBER)
        }
      }

      // Entities
      scope.startsWith("entity.name.") -> {
        when {
          scope.startsWith("entity.name.type.class")          -> pack(PERMIFY_ENTITY_NAME)
          scope.startsWith("entity.name.type.attribute-name") -> pack(PERMIFY_ATTRIBUTE)
          scope.startsWith("entity.name.type.extension")      -> pack(PERMIFY_EXTENSION)
          scope.startsWith("entity.name.function")            -> pack(PERMIFY_RULE_NAME)
          else                                                -> pack(PERMIFY_IDENTIFIER)
        }
      }

      // Variables
      scope.startsWith("variable.")    -> {
        when {
          scope.startsWith("variable.language.relation")   -> pack(PERMIFY_RELATION_NAME)
          scope.startsWith("variable.language.permission") -> pack(PERMIFY_PERMISSION_NAME)
          scope.startsWith("variable.language.action")     -> pack(PERMIFY_ACTION_NAME)
          scope.startsWith("variable.language.attribute")  -> pack(PERMIFY_ATTRIBUTE_NAME)
          scope.startsWith("variable.language")            -> pack(PERMIFY_PROPERTY)
          scope.startsWith("variable.parameter.function")  -> pack(PERMIFY_PARAMETER)
          scope.startsWith("variable.parameter")           -> pack(PERMIFY_IDENTIFIER)
          scope.startsWith("variable.other")               -> pack(PERMIFY_REFERENCE)
          else                                             -> pack(PERMIFY_IDENTIFIER)
        }
      }

      // storage
      scope.startsWith("storage.")     -> {
        when {
          scope.startsWith("storage.type") -> pack(PERMIFY_RULE)
          else                             -> pack(PERMIFY_RULE)
        }
      }

      // support
      scope.startsWith("support.")     -> {
        when {
          scope.startsWith("support.function") -> pack(PERMIFY_FUNCTION)
          scope.startsWith("support.type")     -> pack(PERMIFY_TYPE)
          else                                 -> pack(PERMIFY_FUNCTION)
        }
      }

      else                             -> pack(null)
    }
  }
}
