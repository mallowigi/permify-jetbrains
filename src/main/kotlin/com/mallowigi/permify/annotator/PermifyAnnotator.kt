package com.mallowigi.permify.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.elementType
import com.mallowigi.permify.lang.psi.PermifyTypes
import com.mallowigi.permify.settings.*

class PermifyAnnotator : PsiElementVisitor(), Annotator {
  protected var myHolder: AnnotationHolder? = null

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    assert(myHolder == null) { "unsupported concurrent annotator invocation" }

    try {
      myHolder = holder
      element.accept(this)
    } finally {
      myHolder = null
    }
  }

  override fun visitElement(element: PsiElement) {
    assert(myHolder != null)
    val kind = getIdentifierKind(element) ?: return

    val textRange = element.textRange
    val range = TextRange(textRange.startOffset, textRange.endOffset)
    val highlightSeverity = HighlightSeverity.INFORMATION

    (myHolder ?: return).newSilentAnnotation(highlightSeverity)
      .range(range)
      .textAttributes(kind)
      .create()
  }

  private fun getIdentifierKind(element: PsiElement): TextAttributesKey? = when (element.elementType) {
    PermifyTypes.IDENTIFIER -> getParentRelatedKind(element)
    else -> null
  }

  private fun getParentRelatedKind(element: PsiElement): TextAttributesKey? = when (element.parent.elementType) {
    PermifyTypes.ENTITY_DEF -> PERMIFY_ENTITY_NAME
    PermifyTypes.RELATION_DEF -> PERMIFY_RELATION_NAME
    PermifyTypes.PERMISSION_DEF -> PERMIFY_PERMISSION_NAME
    PermifyTypes.ACTION_DEF -> PERMIFY_ACTION_NAME
    PermifyTypes.ATTRIBUTE_DEF -> PERMIFY_ATTRIBUTE_NAME
    PermifyTypes.RULE_DEF -> PERMIFY_RULE_NAME
    PermifyTypes.RULE_PARAM -> PERMIFY_PARAMETER
    else -> getSiblingRelatedKind(element)
  }

  private fun getSiblingRelatedKind(element: PsiElement): TextAttributesKey? = when (element.prevSibling?.elementType) {
    PermifyTypes.AT -> PERMIFY_REFERENCE
    PermifyTypes.HASH -> PERMIFY_ATTRIBUTE
    PermifyTypes.DOT -> PERMIFY_EXTENSION
    else -> null
  }
}
