package com.mallowigi.permify.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.mallowigi.permify.PermifyFileType
import com.mallowigi.permify.PermifyLanguage

class PermifyFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PermifyLanguage) {
  override fun getFileType() = PermifyFileType

  override fun toString(): String = "Permify File"
}
