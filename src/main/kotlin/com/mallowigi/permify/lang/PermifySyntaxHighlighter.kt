package com.mallowigi.permify.lang

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.lang.psi.PermifyTypes
import com.mallowigi.permify.settings.*

class PermifySyntaxHighlighter : SyntaxHighlighterBase() {
  override fun getHighlightingLexer(): Lexer = PermifyLexer()

  override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey?> = when (tokenType) {
    PermifyTypes.ENTITY,
    PermifyTypes.RELATION,
    PermifyTypes.PERMISSION,
    PermifyTypes.ACTION,
    PermifyTypes.ATTRIBUTE,
    PermifyTypes.RULE,
    PermifyTypes.AND,
    PermifyTypes.OR,
    PermifyTypes.NOT,
    PermifyTypes.IN -> pack(PERMIFY_KEYWORD)

    PermifyTypes.TYPE_BOOLEAN,
    PermifyTypes.TYPE_STRING,
    PermifyTypes.TYPE_INTEGER,
    PermifyTypes.TYPE_DOUBLE -> pack(PERMIFY_TYPE)

    PermifyTypes.LPAREN,
    PermifyTypes.RPAREN,
    PermifyTypes.LBRACE,
    PermifyTypes.RBRACE,
    PermifyTypes.LBRACKET,
    PermifyTypes.RBRACKET,
    PermifyTypes.COMMA,
    PermifyTypes.SEMICOLON -> pack(PERMIFY_BRACKETS)

    PermifyTypes.EQ -> pack(PERMIFY_OPERATOR)
    PermifyTypes.DOT -> pack(PERMIFY_DOT)
    PermifyTypes.AT -> pack(PERMIFY_REFERENCE)
    PermifyTypes.HASH -> pack(PERMIFY_ATTRIBUTE)
    PermifyTypes.NUMBER -> pack(PERMIFY_NUMBER)
    PermifyTypes.STRING -> pack(PERMIFY_STRING)
    PermifyTypes.IDENTIFIER -> pack(PERMIFY_IDENTIFIER)
    PermifyTypes.LINE_COMMENT,
    PermifyTypes.BLOCK_COMMENT -> pack(PERMIFY_COMMENT)

    // Opaque CEL rule body: left unstyled, same as today's TextMate behavior (no highlighting inside rule bodies).
    else -> pack(null)
  }
}
