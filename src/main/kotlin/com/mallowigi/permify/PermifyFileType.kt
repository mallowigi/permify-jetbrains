package com.mallowigi.permify

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

object PermifyFileType : LanguageFileType(PermifyLanguage) {
  override fun getName(): @NonNls String = "permify"

  override fun getDescription(): @NlsContexts.Label String = "Permify File"

  override fun getDefaultExtension(): @NlsSafe String = "perm"

  override fun getIcon(): Icon = PermifyIcons.PERMIFY_FILE

}
