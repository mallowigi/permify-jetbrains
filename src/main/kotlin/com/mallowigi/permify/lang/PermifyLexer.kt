package com.mallowigi.permify.lang

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.mallowigi.permify.lang.lexer._PermifyLexer
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifyLexer : MergingLexerAdapter(
  FlexAdapter(_PermifyLexer(null)),
  TokenSet.create(
    PermifyTypes.RULE_BODY_CONTENT,
    TokenType.BAD_CHARACTER,
  )
) {
}
