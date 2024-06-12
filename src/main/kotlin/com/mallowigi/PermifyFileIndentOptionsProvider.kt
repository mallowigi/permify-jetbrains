package com.mallowigi

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider
import com.mallowigi.permify.isPermifyFile

class PermifyFileIndentOptionsProvider : FileIndentOptionsProvider() {
  override fun getIndentOptions(project: Project, settings: CodeStyleSettings, file: VirtualFile): CommonCodeStyleSettings.IndentOptions? {
    if (!isPermifyFile(file)) return null

    val indentOptions = settings.indentOptions
    indentOptions.USE_TAB_CHARACTER = true
    return indentOptions
  }
}
