package com.mallowigi.permify.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Integration test for keyword completion end-to-end: [PermifyCompletionContributor] +
 * [PermifyKeywordCompletionProvider] + [PermifyCompletionContext], registered exactly as in the
 * real plugin.xml (BasePlatformTestCase loads the real plugin descriptor, unlike ParsingTestCase).
 */
class PermifyKeywordCompletionTest : BasePlatformTestCase() {

  fun testTopLevel() {
    myFixture.configureByText("test.perm", "entity user {}\n<caret>")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "entity", "rule")
  }

  fun testTopLevelAtFileStart() {
    myFixture.configureByText("test.perm", "<caret>")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "entity", "rule")
  }

  fun testEntityBody() {
    myFixture.configureByText("test.perm", "entity user {\n  <caret>\n}")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "relation", "permission", "attribute", "action", "rule")
  }

  fun testEntityBodyAfterRelation() {
    myFixture.configureByText("test.perm", "entity user {\n  relation owner @user\n  <caret>\n}")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "relation", "permission", "attribute", "action", "rule")
  }

  fun testAttributeType() {
    myFixture.configureByText("test.perm", "entity user {\n  attribute foo <caret>\n}")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "boolean", "string", "integer", "double")
  }

  fun testRuleParamType() {
    myFixture.configureByText("test.perm", "rule check(foo <caret>) {\n  x\n}")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "boolean", "string", "integer", "double")
  }

  fun testExprOperator() {
    myFixture.configureByText(
      "test.perm",
      "entity user {\n  relation owner @user\n  permission x = owner <caret>\n}",
    )
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "and", "or", "not")
  }

  fun testExprOperatorInParens() {
    myFixture.configureByText(
      "test.perm",
      "entity user {\n  relation owner @user\n  relation admin @user\n  permission x = (owner <caret>)\n}",
    )
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertContainsElements(strings, "and", "or", "not")
  }

  fun testNoKeywordNoiseInsideSubjectRef() {
    myFixture.configureByText("test.perm", "entity user {\n  relation owner @<caret>\n}")
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertDoesntContain(strings, "entity")
    assertDoesntContain(strings, "relation")
    assertDoesntContain(strings, "and")
  }

  fun testNoKeywordNoiseAtRuleCallArgStart() {
    myFixture.configureByText(
      "test.perm",
      "entity user {\n  rule check(x integer) {\n    x\n  }\n  permission p = check(<caret>)\n}",
    )
    val lookups = myFixture.completeBasic()
    val strings = lookups?.mapNotNull { it.lookupString } ?: emptyList()

    assertDoesntContain(strings, "entity")
    assertDoesntContain(strings, "and")
  }

  override fun getTestDataPath(): String = "src/test/testData"
}
