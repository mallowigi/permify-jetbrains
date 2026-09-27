package com.mallowigi.permify.highlighter

import com.intellij.psi.tree.IElementType
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateElementType
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateHighlightingLexer

class PermifyHighlightingLexer : TextMateHighlightingLexer(getTextMateLanguageDescriptor(), 20000) {
  override fun getTokenType(): IElementType? {
    val tokenType = super.getTokenType() ?: return null
    return PermifyElementType((tokenType as TextMateElementType).scope)
  }
}
