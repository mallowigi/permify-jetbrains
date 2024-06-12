package com.mallowigi.permify

import com.intellij.openapi.vfs.VirtualFile

const val PERMIFY: String = "Permify"

fun isPermifyFile(file: VirtualFile): Boolean = file.extension in setOf("perm", "permify")
