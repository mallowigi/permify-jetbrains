package com.mallowigi.permify.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import com.mallowigi.permify.completion.PermifyCompletionContext.classify

class PermifyKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    resultSet: CompletionResultSet
  ) {
    val position = parameters.position
    val kind = classify(position)
  }
}
