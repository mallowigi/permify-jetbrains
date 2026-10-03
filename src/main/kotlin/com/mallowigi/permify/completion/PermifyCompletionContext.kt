package com.mallowigi.permify.completion

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.mallowigi.permify.file.PermifyFile
import com.mallowigi.permify.lang.psi.PermifyPrimaryExpr
import com.mallowigi.permify.lang.psi.PermifyTypes

object PermifyCompletionContext {
  enum class Kind {
    TOP_LEVEL,
    ENTITY_BODY,
    ATTRIBUTE_TYPE,
    RULE_PARAM,
    EXPR,
    UNKNOWN
  }

  private val TOP_LEVEL_ITEM_TYPES = setOf(
    PermifyTypes.ENTITY_DEF,
    PermifyTypes.RULE_DEF,
  )

  private val ENTITY_BODY_ITEM_TYPES = setOf(
    PermifyTypes.RELATION_DEF,
    PermifyTypes.ATTRIBUTE_DEF,
    PermifyTypes.PERMISSION_DEF,
    PermifyTypes.ACTION_DEF,
    PermifyTypes.RULE_DEF,
  )

  /**
   * Classifies the element at the given position into the set of grammatically valid
   * kinds (usually one, but `EXPR`/`ENTITY_BODY` can both apply - see `isEndOfPrimaryExpr`).
   */
  fun classify(position: PsiElement): Set<Kind> {
    val prevLeaf = previousLeaf(position) ?: return setOf(Kind.TOP_LEVEL)

    if (isAttributeName(prevLeaf)) return setOf(Kind.ATTRIBUTE_TYPE)
    if (isRuleParam(prevLeaf)) return setOf(Kind.RULE_PARAM)

    val kinds = mutableSetOf<Kind>()
    if (isEndOfPrimaryExpr(prevLeaf)) kinds += Kind.EXPR
    if (isEndOfEntityBody(prevLeaf)) kinds += Kind.ENTITY_BODY
    if (kinds.isNotEmpty()) return kinds

    if (isEndOfTopLevelItem(prevLeaf)) return setOf(Kind.TOP_LEVEL)
    return setOf(Kind.UNKNOWN)
  }

  /**
   * Retrieve the previous leaf that is not whitespace or comment
   */
  fun previousLeaf(position: PsiElement): PsiElement? {
    var leaf = PsiTreeUtil.prevLeaf(position)
    while (leaf != null && (leaf is PsiWhiteSpace || leaf is PsiComment || leaf.textLength == 0)) {
      leaf = PsiTreeUtil.prevLeaf(leaf)
    }
    return leaf
  }

  /**
   * When inside an attribute definition, completion for the types
   * Ex `attribute total <caret>`
   */
  fun isAttributeName(prev: PsiElement): Boolean = when {
    prev.elementType != PermifyTypes.IDENTIFIER -> false
    prev.parent?.elementType != PermifyTypes.ATTRIBUTE_DEF -> false
    else -> true
  }

  /**
   * When inside a rule definition, completion for the parameter types
   * Ex `rule check_ip_range(ip <caret>)`
   */
  fun isRuleParam(prev: PsiElement): Boolean = when {
    prev.elementType != PermifyTypes.IDENTIFIER -> false
    prev.parent?.elementType != PermifyTypes.RULE_PARAM -> false
    else -> true
  }

  /**
   * When inside a primary expression, completion for the declarations
   * Ex `relation admin = <caret>` or `check(a <caret>)`
   */
  fun isEndOfPrimaryExpr(prev: PsiElement): Boolean {
    // Verify first that the previous token is the last token of a primary expression
    val primaryExpr = PsiTreeUtil.getParentOfType(prev, PermifyPrimaryExpr::class.java) ?: return false

    if (primaryExpr.parent?.elementType != PermifyTypes.EXPR) return false
    return primaryExpr.textRange.endOffset == prev.textRange.endOffset
  }

  /**
   * Inside an entity body, completion for the declarations
   * Ex `entity user { <caret> }` or `entity user { relation member: user <caret> }`
   */
  fun isEndOfEntityBody(prev: PsiElement): Boolean {
    if (prev.elementType == PermifyTypes.LBRACE && prev.parent?.elementType == PermifyTypes.ENTITY_DEF) return true

    // Walk up to the parent whose own end coincides with prev's end that is an entity
    var node: PsiElement? = prev
    while (node != null) {
      if (
        node.elementType in ENTITY_BODY_ITEM_TYPES &&
        node.parent?.elementType == PermifyTypes.ENTITY_DEF &&
        node.textRange.endOffset == prev.textRange.endOffset
      ) {
        return true
      }
      node = node.parent
    }
    return false
  }

  /**
   * Inside a top-level item, completion for the next top-level item
   * Ex `entity user { ... } <caret>`
   */
  fun isEndOfTopLevelItem(prev: PsiElement): Boolean {
    // Walk up to the parent whose own end coincides with prev's end that is a top-level item
    var node: PsiElement? = prev
    while (node != null) {
      if (
        node.elementType in TOP_LEVEL_ITEM_TYPES &&
        node.parent is PermifyFile &&
        node.textRange.endOffset == prev.textRange.endOffset
      ) {
        return true
      }
      node = node.parent
    }
    return false
  }
}
