package com.mallowigi.permify.lang

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifyBraceMatcher : PairedBraceMatcher {
  override fun getPairs(): Array<out BracePair?> = arrayOf(
    BracePair(PermifyTypes.LBRACE, PermifyTypes.RBRACE, true),
    BracePair(PermifyTypes.LPAREN, PermifyTypes.RPAREN, true),
    BracePair(PermifyTypes.LBRACKET, PermifyTypes.RBRACKET, true)
  )

  override fun isPairedBracesAllowedBeforeType(
    lbraceType: IElementType,
    contextType: IElementType?
  ): Boolean = true

  override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
