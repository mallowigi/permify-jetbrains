package com.mallowigi.permify.lang.psi

import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType

/**
 * Leaf PSI element for `IDENTIFIER` tokens that makes references registered through
 * [com.intellij.psi.PsiReferenceContributor] actually resolve.
 *
 * Custom languages don't get this for free: the default leaf ([LeafPsiElement]) only
 * implements the single-reference [com.intellij.psi.PsiElement.getReference], which
 * returns `null` and never consults [ReferenceProvidersRegistry]. The platform's real
 * reference-search machinery (`PsiReferenceService`, used by go-to-declaration, find
 * usages, rename) only queries the registry for elements implementing
 * [ContributedReferenceHost] - this class exists purely to opt our identifiers into that.
 *
 * Installed via [com.mallowigi.permify.lang.PermifyASTFactory].
 */
class PermifyIdentifierElement(type: IElementType, text: CharSequence) :
  LeafPsiElement(type, text),
  ContributedReferenceHost {
  override fun getReferences(): Array<PsiReference> = ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
