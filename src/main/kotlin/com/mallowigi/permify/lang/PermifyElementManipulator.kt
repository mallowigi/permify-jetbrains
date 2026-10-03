package com.mallowigi.permify.lang

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulator
import com.mallowigi.permify.lang.psi.PermifyIdentifierElement

class PermifyElementManipulator : ElementManipulator<PermifyIdentifierElement> {
  override fun handleContentChange(
    element: PermifyIdentifierElement,
    range: TextRange,
    newContent: String?
  ): PermifyIdentifierElement? {
    val newText = range.replace(element.text, newContent.orEmpty())
    return element.replaceWithText(newText) as? PermifyIdentifierElement
  }

  override fun handleContentChange(
    element: PermifyIdentifierElement,
    newContent: String?
  ): PermifyIdentifierElement? = handleContentChange(element, getRangeInElement(element), newContent)

  override fun getRangeInElement(element: PermifyIdentifierElement): TextRange = TextRange(0, element.textLength)
}
