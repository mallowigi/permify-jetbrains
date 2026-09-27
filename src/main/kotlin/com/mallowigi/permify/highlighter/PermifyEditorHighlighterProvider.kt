package com.mallowigi.permify.highlighter

import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.DataStorage
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.EditorHighlighterProvider
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class PermifyEditorHighlighterProvider : EditorHighlighterProvider {
  override fun getEditorHighlighter(
    project: Project?,
    fileType: FileType,
    virtualFile: VirtualFile?,
    colors: EditorColorsScheme
  ): EditorHighlighter {
    val syntaxHighlighter = PermifyHighlighterFactory().getSyntaxHighlighter(project, virtualFile)
    return PermifyEditorHighlighter(syntaxHighlighter, colors)
  }

  private class PermifyEditorHighlighter(highlighter: SyntaxHighlighter, colors: EditorColorsScheme) :
    LexerEditorHighlighter(highlighter, colors) {
    override fun createStorage(): DataStorage {
      return PermifyLexerDataStorage()
    }
  }
}
