package com.mallowigi.permify.lang.psi

import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.PermifyLanguage

class PermifyElementType(debugName: String) : IElementType(debugName, PermifyLanguage) {
  override fun toString(): String = "PermifyElementType.${super.toString()}"
}
