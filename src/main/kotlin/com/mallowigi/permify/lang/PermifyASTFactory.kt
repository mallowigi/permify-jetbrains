package com.mallowigi.permify.lang

import com.intellij.lang.ASTFactory
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.tree.IElementType
import com.mallowigi.permify.lang.psi.PermifyIdentifierElement
import com.mallowigi.permify.lang.psi.PermifyTypes

/**
 * Registered via `lang.ast.factory` for the `permify` language. Only overrides leaf
 * creation for `IDENTIFIER` tokens, swapping in [PermifyIdentifierElement] so they
 * support references contributed via [com.mallowigi.permify.reference.PermifyReferenceContributor].
 * Every other token type returns `null`, which makes [ASTFactory.leaf] fall back to the
 * platform default leaf creation.
 */
class PermifyASTFactory : ASTFactory() {
  override fun createLeaf(type: IElementType, text: CharSequence): LeafElement? =
    if (type == PermifyTypes.IDENTIFIER) PermifyIdentifierElement(type, text) else null
}
