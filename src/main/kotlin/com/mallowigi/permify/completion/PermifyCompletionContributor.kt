package com.mallowigi.permify.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifyCompletionContributor : CompletionContributor() {
  init {
    extend(
      CompletionType.BASIC,
      PlatformPatterns.psiElement(PermifyTypes.IDENTIFIER),
      PermifyKeywordCompletionProvider()
    )
  }
}
