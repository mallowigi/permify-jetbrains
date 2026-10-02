package com.mallowigi.permify.lang

import com.intellij.testFramework.ParsingTestCase

/**
 * Verifies the generated Permify PSI tree for a handful of hand-written fixtures, following
 * templ-jetbrains' `TemplParsingTest.kt` pattern: each `testXxx()` method parses
 * `testData/Xxx.perm` and compares the resulting PSI tree dump against the golden file
 * `testData/Xxx.txt`.
 */
class PermifyParsingTest : ParsingTestCase("", "perm", PermifyParserDefinition()) {
  fun testParsingTestSubjectSet() {
    doTest(true)
  }

  fun testParsingTestRuleAndRuleCall() {
    doTest(true)
  }

  fun testParsingTestActionAlias() {
    doTest(true)
  }

  fun testParsingTestComments() {
    doTest(true)
  }

  fun testParsingTestFullSchema() {
    doTest(true)
  }

  override fun getTestDataPath(): String = "src/test/testData"

  override fun skipSpaces(): Boolean = false

  override fun includeRanges(): Boolean = true
}
