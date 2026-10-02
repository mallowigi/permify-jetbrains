package com.mallowigi.permify.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.mallowigi.permify.PermifyLanguage
import com.mallowigi.permify.file.PermifyFile
import com.mallowigi.permify.lang.parser.PermifyParser
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifyParserDefinition : ParserDefinition {
  override fun createLexer(project: Project?): Lexer = PermifyLexer()

  override fun createParser(project: Project?): PsiParser = PermifyParser()

  override fun getFileNodeType(): IFileElementType = FILE

  override fun getCommentTokens(): TokenSet = TokenSet.create(
    PermifyTypes.LINE_COMMENT,
    PermifyTypes.BLOCK_COMMENT
  )

  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

  override fun createElement(node: ASTNode?): PsiElement = PermifyTypes.Factory.createElement(node)

  override fun createFile(viewProvider: FileViewProvider): PsiFile = PermifyFile(viewProvider)

  companion object {
    val FILE = IFileElementType(PermifyLanguage)
  }
}
