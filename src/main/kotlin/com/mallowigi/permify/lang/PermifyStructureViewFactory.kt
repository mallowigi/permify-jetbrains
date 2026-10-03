package com.mallowigi.permify.lang

import com.intellij.ide.structureView.*
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.mallowigi.permify.lang.psi.PermifyEntityDef
import com.mallowigi.permify.lang.psi.PermifyPsiUtil
import com.mallowigi.permify.lang.psi.PermifyTypes
import javax.swing.Icon

class PermifyStructureViewFactory : PsiStructureViewFactory {
  override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder =
    object : TreeBasedStructureViewBuilder() {
      override fun createStructureViewModel(editor: Editor?): StructureViewModel = PermifyStructureViewModel(psiFile)
    }

  class PermifyStructureViewModel(psiFile: PsiFile) : TextEditorBasedStructureViewModel(psiFile),
    StructureViewModel.ElementInfoProvider {
    override fun getRoot(): StructureViewTreeElement = PermifyStructureViewElement(psiFile)

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean =
      (element as? PermifyStructureViewElement)?.children?.isEmpty() ?: false

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false
  }

  class PermifyStructureViewElement(private val element: PsiElement) : StructureViewTreeElement {
    override fun getValue(): Any = element

    override fun getPresentation(): ItemPresentation = object : ItemPresentation {
      override fun getPresentableText(): String? = when {
        element is PsiFile -> element.name
        element.elementType == PermifyTypes.RELATION_DEF -> relationLabel(element)
        element.elementType == PermifyTypes.PRIMARY_EXPR -> element.text.trim()
        else -> getDeclarationName(element)
      }

      override fun getIcon(unused: Boolean): Icon = PermifyPsiUtil.iconFor(element.elementType)

      /**
       * For entity/relation/etc we want to display the name of the declaration,
       * so we look for the IDENTIFIER child of the element and return its text.
       */
      private fun getDeclarationName(element: PsiElement): String? =
        element.node.findChildByType(PermifyTypes.IDENTIFIER)?.text

      /** `relationName[@Subject1, @Subject2#relation]`, one bracket entry per subject_ref. */
      private fun relationLabel(element: PsiElement): String {
        val name = getDeclarationName(element) ?: "<unnamed>"
        val subjects = element.node.getChildren(null)
          .filter { it.elementType == PermifyTypes.SUBJECT_REF }
          .joinToString(", ") { it.text }

        return if (subjects.isEmpty()) name else "$name[$subjects]"
      }
    }

    override fun getChildren(): Array<StructureViewTreeElement> = when {
      element is PsiFile -> element.children
        .filter {
          it.elementType in setOf(
            PermifyTypes.ENTITY_DEF,
            PermifyTypes.RULE_DEF
          )
        }
        .map { PermifyStructureViewElement(it) }
        .toTypedArray()

      element is PermifyEntityDef -> element.children
        .filter {
          it.elementType in setOf(
            PermifyTypes.RELATION_DEF,
            PermifyTypes.PERMISSION_DEF,
            PermifyTypes.ACTION_DEF,
            PermifyTypes.ATTRIBUTE_DEF,
            PermifyTypes.RULE_DEF
          )
        }
        .map { PermifyStructureViewElement(it) }
        .toTypedArray()

      element.elementType == PermifyTypes.PERMISSION_DEF || element.elementType == PermifyTypes.ACTION_DEF ->
        element.node.getChildren(null)
          .firstOrNull { it.elementType == PermifyTypes.EXPR }
          ?.getChildren(null)
          ?.filter { it.elementType == PermifyTypes.PRIMARY_EXPR }
          ?.map { PermifyStructureViewElement(it.psi) }
          ?.toTypedArray()
          ?: emptyArray()

      else -> emptyArray()
    }

    override fun navigate(requestFocus: Boolean) {
      (element as? Navigatable)?.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = true

    override fun canNavigateToSource(): Boolean = true
  }
}
