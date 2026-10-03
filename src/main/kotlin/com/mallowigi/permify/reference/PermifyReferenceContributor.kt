package com.mallowigi.permify.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import com.mallowigi.permify.file.PermifyFile
import com.mallowigi.permify.lang.psi.PermifyEntityDef
import com.mallowigi.permify.lang.psi.PermifyPsiUtil
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifyReferenceContributor : PsiReferenceContributor() {
  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    // Reference the subject (e.g. `@user#member`)
    registrar.registerReferenceProvider(
      PlatformPatterns.psiElement(PermifyTypes.IDENTIFIER)
        .withParent(PlatformPatterns.psiElement(PermifyTypes.SUBJECT_REF)),
      object : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?> {
          when (element.prevSibling?.elementType) {
            null -> return PsiReference.EMPTY_ARRAY
            PermifyTypes.AT -> return arrayOf(PermifyEntityReference(element))
            PermifyTypes.HASH -> return arrayOf(PermifyRelationReference(element))
            else -> return PsiReference.EMPTY_ARRAY
          }
        }
      }
    )

    registrar.registerReferenceProvider(
      PlatformPatterns.psiElement(PermifyTypes.IDENTIFIER)
        .withParent(PlatformPatterns.psiElement(PermifyTypes.RULE_CALL)),
      object : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?> =
          arrayOf(PermifyRuleReference(element))
      }
    )
  }

  class PermifyEntityReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findEntityByName(file, element.text)
    }

    override fun getVariants(): Array<Any> = emptyArray()
  }

  class PermifyRelationReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
      // Get the sibling @user for @user#member
      val entityName = element.prevSibling.prevSibling.text
      val containingEntity =
        PermifyPsiUtil.findEntityByName(element.containingFile as PermifyFile, entityName) ?: return null

      return PermifyPsiUtil.findRelationByName(containingEntity.parent as PermifyEntityDef, element.text)
    }

    override fun getVariants(): Array<Any> = emptyArray()
  }

  class PermifyRuleReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
      val enclosingEntityDef = element.parentOfType<PermifyEntityDef>()
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findRuleByName(file, enclosingEntityDef, element.text)
    }

    override fun getVariants(): Array<Any> = emptyArray()
  }
}
