package com.mallowigi.permify.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import com.mallowigi.permify.file.PermifyFile
import com.mallowigi.permify.lang.psi.*

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

    // Reference rule calls (e.g. check_ip_range(...))
    registrar.registerReferenceProvider(
      PlatformPatterns.psiElement(PermifyTypes.IDENTIFIER)
        .withParent(PlatformPatterns.psiElement(PermifyTypes.RULE_CALL)),
      object : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?> =
          arrayOf(PermifyRuleReference(element))
      }
    )

    // Reference member expressions (e.g. `admin / admin.member[.blabla]`) or rule-call arguments (e.g. `check_ip_range(admin, ...)`)
    registrar.registerReferenceProvider(
      PlatformPatterns.psiElement(PermifyTypes.IDENTIFIER)
        .withParent(PlatformPatterns.psiElement(PermifyTypes.MEMBER_EXPR)),
      object : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?> {
          // Not the first segment of the dotted chain - out of scope for now
          if (element.prevSibling != null) return PsiReference.EMPTY_ARRAY

          // Walk up past any paren_expr/expr/primary_expr wrapper layers to find
          // whether we're a rule-call argument or a plain permission/action operand
          val enclosingContext = PsiTreeUtil.getParentOfType(
            element,
            PermifyRuleCall::class.java,
            PermifyPermissionDef::class.java,
            PermifyActionDef::class.java
          )

          return when (enclosingContext) {
            is PermifyRuleCall -> arrayOf(PermifyAttributeReference(element))
            else -> arrayOf(PermifySelfReference(element))
          }
        }
      }
    )
  }

  class PermifyEntityReference(element: PsiElement) :
    PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {
    override fun resolve(): PsiElement? {
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findEntityByName(file, element.text)
    }

    override fun getVariants(): Array<PsiElement> {
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findAllEntities(file).filterNotNull().toTypedArray()
    }
  }

  class PermifyRelationReference(element: PsiElement) :
    PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {
    override fun resolve(): PsiElement? {
      // Get the sibling @user for @user#member
      val entityName = element.prevSibling.prevSibling.text
      val containingEntity =
        PermifyPsiUtil.findEntityByName(element.containingFile as PermifyFile, entityName) ?: return null

      return PermifyPsiUtil.findRelationByName(containingEntity.parent as PermifyEntityDef, element.text)
    }

    override fun getVariants(): Array<PsiElement> {
      val entityName = element.prevSibling.prevSibling.text
      val containingEntity =
        PermifyPsiUtil.findEntityByName(element.containingFile as PermifyFile, entityName) ?: return emptyArray()

      return PermifyPsiUtil.findAllRelations(containingEntity.parent as PermifyEntityDef).filterNotNull().toTypedArray()
    }
  }

  class PermifyRuleReference(element: PsiElement) :
    PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {
    override fun resolve(): PsiElement? {
      val enclosingEntityDef = element.parentOfType<PermifyEntityDef>()
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findRuleByName(file, enclosingEntityDef, element.text)
    }

    override fun getVariants(): Array<PsiElement> {
      val enclosingEntityDef = element.parentOfType<PermifyEntityDef>()
      val file = element.containingFile as PermifyFile
      return PermifyPsiUtil.findAllRules(file, enclosingEntityDef).filterNotNull().toTypedArray()
    }
  }

  class PermifyAttributeReference(element: PsiElement) :
    PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {
    override fun resolve(): PsiElement? {
      val containingEntity = element.parentOfType<PermifyEntityDef>() ?: return null
      return PermifyPsiUtil.findDeclarationByName(containingEntity, element.text)
    }

    override fun getVariants(): Array<PsiElement> {
      val containingEntity = element.parentOfType<PermifyEntityDef>() ?: return emptyArray()
      return PermifyPsiUtil.findAllDeclarations(containingEntity).filterNotNull().toTypedArray()
    }
  }

  class PermifySelfReference(element: PsiElement) :
    PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {
    override fun resolve(): PsiElement? {
      val containingEntity = element.parentOfType<PermifyEntityDef>() ?: return null
      return PermifyPsiUtil.findDeclarationByName(containingEntity, element.text)
    }

    override fun getVariants(): Array<PsiElement> {
      val containingEntity = element.parentOfType<PermifyEntityDef>() ?: return emptyArray()
      return PermifyPsiUtil.findAllDeclarations(containingEntity).filterNotNull().toTypedArray()
    }
  }
}
