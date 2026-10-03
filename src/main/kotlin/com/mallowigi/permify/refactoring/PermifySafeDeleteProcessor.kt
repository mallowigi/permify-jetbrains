package com.mallowigi.permify.refactoring

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo
import com.intellij.refactoring.safeDelete.SafeDeleteProcessorDelegate
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceSimpleDeleteUsageInfo
import com.intellij.usageView.UsageInfo
import com.mallowigi.permify.lang.psi.PermifyTypes

class PermifySafeDeleteProcessor : SafeDeleteProcessorDelegate {
  override fun handlesElement(element: PsiElement?): Boolean = when (element?.parent?.elementType) {
    PermifyTypes.ENTITY_DEF,
    PermifyTypes.RELATION_DEF,
    PermifyTypes.PERMISSION_DEF,
    PermifyTypes.ACTION_DEF,
    PermifyTypes.RULE_DEF,
    PermifyTypes.ATTRIBUTE_DEF,
    PermifyTypes.RULE_PARAM -> true

    else -> false
  }

  override fun findUsages(
    element: PsiElement,
    allElementsToDelete: Array<out PsiElement?>,
    result: MutableList<in UsageInfo>
  ): NonCodeUsageSearchInfo {
    // Checks whether a usage is inside an element that is being deleted
    val insideDeleted = Condition<PsiElement> { usage ->
      allElementsToDelete.any { it != null && PsiTreeUtil.isAncestor(it, usage, false) }
    }

    ReferencesSearch.search(element)
      .forEach { ref ->
        result.add(
          SafeDeleteReferenceSimpleDeleteUsageInfo(
            /* element = */ ref.element,
            /* referencedElement = */ element,
            /* isSafeDelete = */ insideDeleted.value(ref.element)
          )
        )
      }
    return NonCodeUsageSearchInfo(insideDeleted, element)
  }

  override fun getElementsToSearch(
    element: PsiElement,
    allElementsToDelete: Collection<PsiElement?>
  ): Collection<PsiElement?> = listOf(element)

  override fun getAdditionalElementsToDelete(
    element: PsiElement,
    allElementsToDelete: Collection<PsiElement?>,
    askUser: Boolean
  ): Collection<PsiElement?>? = null

  override fun preprocessUsages(
    project: Project,
    usages: Array<out UsageInfo?>
  ): Array<out UsageInfo?> = usages

  override fun prepareForDeletion(element: PsiElement): Unit = element.parent?.delete()!!

  override fun isToSearchInComments(element: PsiElement?): Boolean = false

  override fun setToSearchInComments(element: PsiElement?, enabled: Boolean) {}

  override fun isToSearchForTextOccurrences(element: PsiElement?): Boolean = false

  override fun setToSearchForTextOccurrences(element: PsiElement?, enabled: Boolean) {

  }
}
