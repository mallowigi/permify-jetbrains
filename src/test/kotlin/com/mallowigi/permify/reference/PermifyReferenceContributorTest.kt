package com.mallowigi.permify.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.lang.LanguageASTFactory
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.ParsingTestCase
import com.intellij.util.KeyedLazyInstance
import com.mallowigi.permify.PermifyLanguage
import com.mallowigi.permify.lang.PermifyASTFactory
import com.mallowigi.permify.lang.PermifyParserDefinition
import com.mallowigi.permify.lang.psi.PermifyTypes

/**
 * Verifies that each PsiReference wired in [PermifyReferenceContributor] resolves to the
 * expected declaration, for every reference kind documented in plan.md:
 * `@entity`, `#relation`, rule-call -> rule, rule-call-argument -> attribute/permission,
 * and plain-operand self-scope references. Also confirms declaration-name identifiers
 * themselves carry no reference.
 *
 * `ParsingTestCase` does not load plugin.xml, so the contributor is registered manually
 * in [setUp] via its extension point, following the standard IntelliJ Platform test
 * pattern for PsiReferenceContributor.
 */
class PermifyReferenceContributorTest : ParsingTestCase("", "perm", PermifyParserDefinition()) {
  override fun setUp() {
    super.setUp()
    // PsiReferenceContributor.EP_NAME is keyed by language ID, same shape ParsingTestCase
    // itself uses to register ParserDefinition - see its myLangParserDefinition wiring.
    registerExtension(
      PsiReferenceContributor.EP_NAME,
      object : KeyedLazyInstance<PsiReferenceContributor> {
        override fun getKey(): String = "permify"
        override fun getInstance(): PsiReferenceContributor = PermifyReferenceContributor()
      }
    )
    // Without this, IDENTIFIER leaves are plain LeafPsiElements and never consult
    // ReferenceProvidersRegistry - see PermifyIdentifierElement/PermifyASTFactory.
    addExplicitExtension(LanguageASTFactory.INSTANCE, PermifyLanguage, PermifyASTFactory())
  }

  fun testSubjectRefEntityReference() {
    val file = parseFile(
      "test",
      """
      entity user {}
      entity document {
        relation owner @user
      }
      """.trimIndent()
    )

    val reference = findIdentifier(file, PermifyTypes.SUBJECT_REF, "user")
    val resolved = assertResolves(reference)
    assertEquals(PermifyTypes.ENTITY_DEF, resolved.parent.node.elementType)
  }

  fun testSubjectRefRelationReference() {
    val file = parseFile(
      "test",
      """
      entity user {}
      entity organization {
        relation member @user
      }
      entity document {
        relation viewer @organization#member
      }
      """.trimIndent()
    )

    val reference = findIdentifier(file, PermifyTypes.SUBJECT_REF, "member")
    val resolved = assertResolves(reference)
    assertEquals(PermifyTypes.RELATION_DEF, resolved.parent.node.elementType)
  }

  fun testRuleCallResolvesToRuleDef() {
    val file = parseFile(
      "test",
      """
      entity account {
        attribute balance integer

        rule check_balance(balance integer) {
          balance > 5000
        }

        permission withdraw = check_balance(balance)
      }
      """.trimIndent()
    )

    val reference = findIdentifier(file, PermifyTypes.RULE_CALL, "check_balance")
    val resolved = assertResolves(reference)
    assertEquals(PermifyTypes.RULE_DEF, resolved.parent.node.elementType)
  }

  fun testRuleCallArgumentResolvesToAttributeNotRuleParam() {
    val file = parseFile(
      "test",
      """
      entity account {
        attribute balance integer

        rule check_balance(balance integer) {
          balance > 5000
        }

        permission withdraw = check_balance(balance)
      }
      """.trimIndent()
    )

    // The argument's parent is MEMBER_EXPR, distinct from the rule's own RULE_PARAM
    // declaration - this disambiguates the two "balance" identifiers in the fixture.
    val reference = findIdentifier(file, PermifyTypes.MEMBER_EXPR, "balance")
    val resolved = assertResolves(reference)
    assertEquals(PermifyTypes.ATTRIBUTE_DEF, resolved.parent.node.elementType)
  }

  fun testSelfScopePlainOperandReference() {
    val file = parseFile(
      "test",
      """
      entity user {}
      entity account {
        relation owner @user
        relation administrator @user
        permission admin = owner or administrator
      }
      """.trimIndent()
    )

    val reference = findIdentifier(file, PermifyTypes.MEMBER_EXPR, "administrator")
    val resolved = assertResolves(reference)
    assertEquals(PermifyTypes.RELATION_DEF, resolved.parent.node.elementType)
  }

  fun testDeclarationNameHasNoReference() {
    val file = parseFile(
      "test",
      """
      entity user {}
      entity account {
        relation owner @user
      }
      """.trimIndent()
    )

    val declarationName = findIdentifier(file, PermifyTypes.RELATION_DEF, "owner")
    assertTrue(declarationName.references.isEmpty())
  }

  private fun assertResolves(element: PsiElement): PsiElement {
    val resolved = element.references.firstOrNull()?.resolve()
    assertNotNull("Expected ${element.text} to resolve to a declaration", resolved)
    return resolved!!
  }

  /** Finds the single IDENTIFIER leaf with the given text whose direct parent has [parentType]. */
  private fun findIdentifier(file: PsiFile, parentType: IElementType, text: String): PsiElement {
    var found: PsiElement? = null
    file.accept(object : PsiRecursiveElementVisitor() {
      override fun visitElement(element: PsiElement) {
        if (found == null &&
          element.node.elementType == PermifyTypes.IDENTIFIER &&
          element.parent?.node?.elementType == parentType &&
          element.text == text
        ) {
          found = element
        }
        super.visitElement(element)
      }
    })
    return found ?: error("No IDENTIFIER '$text' found with parent $parentType")
  }

  override fun getTestDataPath(): String = "src/test/testData"

  override fun skipSpaces(): Boolean = false

  override fun includeRanges(): Boolean = true
}
