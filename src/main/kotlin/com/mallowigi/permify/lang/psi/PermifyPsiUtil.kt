package com.mallowigi.permify.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.file.PermifyFile

object PermifyPsiUtil {
  fun findEntityByName(file: PermifyFile, name: String): PsiElement? {
    return file.node.getChildren(null)
      .filter { it.elementType == PermifyTypes.ENTITY_DEF }
      .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
      ?.findChildByType(PermifyTypes.IDENTIFIER)
      ?.psi
  }

  fun findRelationByName(entityDef: PermifyEntityDef, name: String): PsiElement? {
    return entityDef.node.getChildren(null)
      .filter { it.elementType == PermifyTypes.RELATION_DEF }
      .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
      ?.findChildByType(PermifyTypes.IDENTIFIER)
      ?.psi
  }

  fun findRuleByName(file: PermifyFile, enclosingEntity: PermifyEntityDef?, name: String): PsiElement? {
    val nested = enclosingEntity?.let { findInChildren(it, name, PermifyTypes.RULE_DEF) }

    return nested ?: findInChildren(file, name, PermifyTypes.RULE_DEF)
  }

  fun findDeclarationByName(entityDef: PermifyEntityDef, name: String): PsiElement? {
    val elementTypes = listOf(
      PermifyTypes.ACTION_DEF,
      PermifyTypes.PERMISSION_DEF,
      PermifyTypes.RELATION_DEF,
      PermifyTypes.RULE_DEF,
      PermifyTypes.ATTRIBUTE_DEF
    )
    return entityDef.node.getChildren(null)
      .filter { it.elementType in elementTypes }
      .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
      ?.findChildByType(PermifyTypes.IDENTIFIER)
      ?.psi
  }

  private fun findInChildren(element: PsiElement, name: String, elementType: IElementType): PsiElement? {
    return element.node.getChildren(null)
      .filter { it.elementType == elementType }
      .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
      ?.findChildByType(PermifyTypes.IDENTIFIER)
      ?.psi
  }
}
