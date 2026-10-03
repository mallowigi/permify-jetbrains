package com.mallowigi.permify.lang.psi

import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType

class PermifyIdentifierElement(type: IElementType, text: CharSequence) :
  LeafPsiElement(type, text),
  ContributedReferenceHost,
  PsiNamedElement {
  override fun getReferences(): Array<PsiReference> = ReferenceProvidersRegistry.getReferencesFromProviders(this)

  override fun getName(): String = text

  override fun setName(name: String): PsiElement = replaceWithText(name) as PsiElement
}
