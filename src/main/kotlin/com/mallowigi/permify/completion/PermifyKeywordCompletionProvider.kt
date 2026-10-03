package com.mallowigi.permify.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.mallowigi.permify.completion.PermifyCompletionContext.Kind
import com.mallowigi.permify.completion.PermifyCompletionContext.classify

class PermifyKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    resultSet: CompletionResultSet,
  ) {
    val keywords = classify(parameters.position).flatMap { keywordsFor(it) }.distinct()
    if (keywords.isEmpty()) return

    keywords.forEach { keyword -> resultSet.addElement(createLookupElement(keyword)) }
  }

  private fun keywordsFor(kind: Kind): List<String> = when (kind) {
    Kind.TOP_LEVEL -> TOP_LEVEL_KEYWORDS
    Kind.ENTITY_BODY -> ENTITY_BODY_KEYWORDS
    Kind.ATTRIBUTE_TYPE, Kind.RULE_PARAM -> TYPE_KEYWORDS
    Kind.EXPR -> EXPR_OPERATOR_KEYWORDS
    Kind.UNKNOWN -> emptyList()
  }

  private fun createLookupElement(keyword: String): LookupElement =
    LookupElementBuilder.create(keyword).bold().withInsertHandler(TrailingSpaceInsertHandler)

  private object TrailingSpaceInsertHandler : InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
      val editor = context.editor
      val tailOffset = context.tailOffset
      editor.document.insertString(tailOffset, " ")
      editor.caretModel.moveToOffset(tailOffset + 1)
    }
  }

  companion object {
    private val TOP_LEVEL_KEYWORDS = listOf("entity", "rule")
    private val ENTITY_BODY_KEYWORDS = listOf("relation", "permission", "attribute", "action", "rule")
    private val TYPE_KEYWORDS = listOf("boolean", "string", "integer", "double")
    private val EXPR_OPERATOR_KEYWORDS = listOf("and", "or", "not")
  }
}
