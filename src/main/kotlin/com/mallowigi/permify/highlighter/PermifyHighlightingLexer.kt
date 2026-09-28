package com.mallowigi.permify.highlighter

import com.intellij.psi.tree.IElementType
import com.intellij.textmate.joni.JoniRegexFactory
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateElementType
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateHighlightingLexer
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateSyntaxMatcherImpl
import org.jetbrains.plugins.textmate.language.syntax.selector.TextMateSelectorWeigherImpl
import org.jetbrains.plugins.textmate.regex.CaffeineCachingRegexProvider
import org.jetbrains.plugins.textmate.regex.RememberingLastMatchRegexFactory
import java.util.concurrent.ConcurrentHashMap

class PermifyHighlightingLexer : TextMateHighlightingLexer(
  getTextMateLanguageDescriptor(),
  TextMateSyntaxMatcherImpl(
    CaffeineCachingRegexProvider(RememberingLastMatchRegexFactory(JoniRegexFactory())),
    TextMateSelectorWeigherImpl(),
  ),
  20000
) {
  private val tokenTypesByScope = ConcurrentHashMap<TextMateScope, PermifyElementType>()

  override fun getTokenType(): IElementType? {
    val tokenType = super.getTokenType() as? TextMateElementType ?: return null
    return tokenTypesByScope.computeIfAbsent(tokenType.scope, ::PermifyElementType)
  }
}
