package com.mallowigi.permify

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

const val PERMIFY: String = "Permify"

fun isPermifyFile(file: VirtualFile): Boolean = file.extension in setOf("perm", "permify")

fun isPermifyFile(file: PsiFile): Boolean = file.name.endsWith(".perm") || file.name.endsWith(".permify")
