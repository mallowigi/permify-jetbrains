package com.mallowigi.permify.lang

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import com.mallowigi.permify.lang.psi.PermifyIdentifierElement
import com.mallowigi.permify.lang.psi.PermifyTypes
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

class PermifyFindUsagesProvider : FindUsagesProvider {
  override fun canFindUsagesFor(element: PsiElement): Boolean = element is PermifyIdentifierElement

  override fun getHelpId(psiElement: PsiElement): @NonNls String? = null

  override fun getType(element: PsiElement): @Nls String = when (element.parent.elementType) {
    PermifyTypes.ENTITY_DEF -> "entity"
    PermifyTypes.RELATION_DEF -> "relation"
    PermifyTypes.PERMISSION_DEF -> "permission"
    PermifyTypes.ACTION_DEF -> "action"
    PermifyTypes.RULE_DEF -> "rule"
    PermifyTypes.ATTRIBUTE_DEF -> "attribute"
    PermifyTypes.RULE_PARAM -> "parameter"
    else -> "unknown"
  }

  override fun getDescriptiveName(element: PsiElement): @Nls String =
    (element as PsiNamedElement).name ?: element.text


  override fun getNodeText(
    element: PsiElement,
    useFullName: Boolean
  ): @Nls String = getDescriptiveName(element)

  override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
    PermifyLexer(),
    TokenSet.create(PermifyTypes.IDENTIFIER),
    TokenSet.create(PermifyTypes.LINE_COMMENT, PermifyTypes.BLOCK_COMMENT),
    TokenSet.EMPTY
  )
}
