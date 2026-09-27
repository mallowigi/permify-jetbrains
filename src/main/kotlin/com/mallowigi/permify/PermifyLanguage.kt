package com.mallowigi.permify

import com.intellij.lang.Language
import com.intellij.openapi.util.NlsSafe

@Suppress("unused")
object PermifyLanguage : Language("permify") {
  private fun readResolve(): Any = PermifyLanguage
  override fun getDisplayName(): @NlsSafe String = "Permify"
}
