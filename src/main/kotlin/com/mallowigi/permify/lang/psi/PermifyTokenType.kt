package com.mallowigi.permify.lang.psi

import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.PermifyLanguage

class PermifyTokenType(debugName: String) : IElementType(debugName, PermifyLanguage) {
  override fun toString(): String = "PermifyTokenType.${super.toString()}"
}
