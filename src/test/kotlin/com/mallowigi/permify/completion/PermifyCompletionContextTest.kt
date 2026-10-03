package com.mallowigi.permify.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import com.mallowigi.permify.lang.PermifyParserDefinition
import junit.framework.TestCase

/**
 * Unit test for [PermifyCompletionContext] against real PSI shapes produced during
 * completion-time error recovery (a dummy "DUMMY" identifier stands in for the platform's real
 * `IntellijIdeaRulezzz` marker - only the identifier's text differs, the PSI shape is the same).
 */
class PermifyCompletionContextTest : ParsingTestCase("", "perm", PermifyParserDefinition()) {
  private fun findDummy(file: PsiElement): PsiElement =
    PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
      .first { it.firstChild == null && it.text == "DUMMY" }

  private fun check(label: String, text: String, vararg expected: PermifyCompletionContext.Kind) {
    val file = parseFile("test", text)
    val dummy = findDummy(file)
    val actual = PermifyCompletionContext.classify(dummy)
    TestCase.assertEquals(label, expected.toSet(), actual)
  }

  fun testClassify() {
    check("TOP_LEVEL", "entity user {}\nDUMMY", PermifyCompletionContext.Kind.TOP_LEVEL)
    check("TOP_LEVEL_FILE_START", "DUMMY", PermifyCompletionContext.Kind.TOP_LEVEL)
    check("ENTITY_BODY", "entity user {\n  DUMMY\n}", PermifyCompletionContext.Kind.ENTITY_BODY)
    check("ATTRIBUTE_TYPE", "entity user {\n  attribute foo DUMMY\n}", PermifyCompletionContext.Kind.ATTRIBUTE_TYPE)
    check(
      "RULE_PARAM_TYPE",
      "entity user {\n  rule check(foo DUMMY) {\n    x\n  }\n}",
      PermifyCompletionContext.Kind.RULE_PARAM,
    )
    // Ambiguous: entity's last body item before closing `}` — both EXPR and ENTITY_BODY apply.
    check(
      "EXPR_OPERATOR_PERMISSION_AT_ENTITY_END",
      "entity user {\n  relation owner @user\n  permission x = owner DUMMY\n}",
      PermifyCompletionContext.Kind.EXPR,
      PermifyCompletionContext.Kind.ENTITY_BODY,
    )
    // Same ambiguity, as reported: `action` instead of `permission`.
    check(
      "EXPR_OPERATOR_ACTION_AT_ENTITY_END",
      "entity user2 {\n  relation foo @user\n  action foo = foo\n  DUMMY\n}",
      PermifyCompletionContext.Kind.EXPR,
      PermifyCompletionContext.Kind.ENTITY_BODY,
    )
    // Also ambiguous mid-paren: error recovery collapses the ancestor chain to the same offset.
    check(
      "EXPR_OPERATOR_PAREN",
      "entity user {\n  relation owner @user\n  relation admin @user\n  permission x = (owner DUMMY)\n}",
      PermifyCompletionContext.Kind.EXPR,
      PermifyCompletionContext.Kind.ENTITY_BODY,
    )
    check(
      "ENTITY_BODY_AFTER_RELATION",
      "entity user {\n  relation owner @user\n  DUMMY\n}",
      PermifyCompletionContext.Kind.ENTITY_BODY,
    )
    check(
      "ENTITY_BODY_MID_LIST",
      "entity user {\n  relation a @user\n  DUMMY\n  relation b @user\n}",
      PermifyCompletionContext.Kind.ENTITY_BODY,
    )
    check(
      "UNKNOWN_INSIDE_SUBJECT_REF",
      "entity user {\n  relation owner @DUMMY\n}",
      PermifyCompletionContext.Kind.UNKNOWN,
    )
    check(
      "UNKNOWN_RULE_CALL_ARG_START",
      "entity user {\n  rule check(x integer) {\n    x\n  }\n  permission p = check(DUMMY)\n}",
      PermifyCompletionContext.Kind.UNKNOWN,
    )
  }

  override fun getTestDataPath(): String = "src/test/testData"
  override fun skipSpaces(): Boolean = false
  override fun includeRanges(): Boolean = true
}
