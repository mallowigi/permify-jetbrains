package com.mallowigi.permify.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.mallowigi.permify.lang.psi.PermifyIdentifierElement

class PermifyRefactoringSupport : RefactoringSupportProvider() {
  override fun isSafeDeleteAvailable(element: PsiElement): Boolean = element is PermifyIdentifierElement
}
