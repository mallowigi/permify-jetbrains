package com.mallowigi.permify

import com.intellij.ide.FileIconProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class PermifyFileIconProvider : FileIconProvider {
  override fun getIcon(file: VirtualFile, flags: Int, project: Project?): Icon? = when {
    isPermifyFile(file) -> PermifyIcons.PERMIFY_FILE
    else                -> null
  }
}
