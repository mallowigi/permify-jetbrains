package com.mallowigi.permify.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.file.PermifyFile

object PermifyPsiUtil {
  val INTERNAL_ELEMENTS = listOf(
    PermifyTypes.ACTION_DEF,
    PermifyTypes.ATTRIBUTE_DEF,
    PermifyTypes.PERMISSION_DEF,
    PermifyTypes.RELATION_DEF,
    PermifyTypes.RULE_DEF,
  )

  fun findEntityByName(file: PermifyFile, name: String): PsiElement? = file.node.getChildren(null)
    .filter { it.elementType == PermifyTypes.ENTITY_DEF }
    .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
    ?.findChildByType(PermifyTypes.IDENTIFIER)
    ?.psi

  fun findAllEntities(file: PermifyFile): Array<out PsiElement?> = file.node.getChildren(null)
    .filter { it.elementType == PermifyTypes.ENTITY_DEF }
    .map { it.findChildByType(PermifyTypes.IDENTIFIER)?.psi }
    .toTypedArray()

  fun findRelationByName(entityDef: PermifyEntityDef, name: String): PsiElement? = entityDef.node.getChildren(null)
    .filter { it.elementType == PermifyTypes.RELATION_DEF }
    .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
    ?.findChildByType(PermifyTypes.IDENTIFIER)
    ?.psi

  fun findAllRelations(entityDef: PermifyEntityDef): Array<out PsiElement?> = entityDef.node.getChildren(null)
    .filter { it.elementType == PermifyTypes.RELATION_DEF }
    .map { it.findChildByType(PermifyTypes.IDENTIFIER)?.psi }
    .toTypedArray()

  fun findRuleByName(file: PermifyFile, enclosingEntity: PermifyEntityDef?, name: String): PsiElement? {
    val nested = enclosingEntity?.let { findElementInChildren(it, name, PermifyTypes.RULE_DEF) }
    return nested ?: findElementInChildren(file, name, PermifyTypes.RULE_DEF)
  }

  fun findAllRules(file: PermifyFile, enclosingEntity: PermifyEntityDef?): Array<out PsiElement?> {
    val nested = enclosingEntity?.let { findElementsInChildren(it, PermifyTypes.RULE_DEF) } ?: emptyList()
    return (nested + findElementsInChildren(file, PermifyTypes.RULE_DEF)).toTypedArray()
  }

  fun findDeclarationByName(entityDef: PermifyEntityDef, name: String): PsiElement? = entityDef.node.getChildren(null)
    .filter { it.elementType in INTERNAL_ELEMENTS }
    .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
    ?.findChildByType(PermifyTypes.IDENTIFIER)
    ?.psi

  fun findAllDeclarations(entityDef: PermifyEntityDef): Array<out PsiElement?> = entityDef.node.getChildren(null)
    .filter { it.elementType in INTERNAL_ELEMENTS }
    .map { it.findChildByType(PermifyTypes.IDENTIFIER)?.psi }
    .toTypedArray()

  private fun findElementInChildren(element: PsiElement, name: String, elementType: IElementType): PsiElement? =
    element.node.getChildren(null)
      .filter { it.elementType == elementType }
      .firstOrNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.text == name }
      ?.findChildByType(PermifyTypes.IDENTIFIER)
      ?.psi

  private fun findElementsInChildren(element: PsiElement, elementType: IElementType): List<PsiElement> =
    element.node.getChildren(null)
      .filter { it.elementType == elementType }
      .mapNotNull { it.findChildByType(PermifyTypes.IDENTIFIER)?.psi }
}
