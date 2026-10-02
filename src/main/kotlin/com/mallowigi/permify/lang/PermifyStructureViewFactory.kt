package com.mallowigi.permify.lang

import com.intellij.icons.AllIcons
import com.intellij.ide.structureView.*
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.mallowigi.permify.lang.psi.PermifyEntityDef
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
      override fun getPresentableText(): String? {
        when (element) {
          is PsiFile -> return element.name
          else -> return getDeclarationName(element)
        }
      }

      override fun getIcon(unused: Boolean): Icon = when (element.elementType) {
        PermifyTypes.ENTITY_DEF -> AllIcons.Nodes.Class
        PermifyTypes.RELATION_DEF -> AllIcons.Nodes.Related
        PermifyTypes.PERMISSION_DEF -> AllIcons.Nodes.Padlock
        PermifyTypes.ACTION_DEF -> AllIcons.Nodes.Method
        PermifyTypes.ATTRIBUTE_DEF -> AllIcons.Nodes.Property
        PermifyTypes.RULE_DEF -> AllIcons.Nodes.Function
        else -> AllIcons.Nodes.Unknown
      }

      /**
       * For entity/relation/etc we want to display the name of the declaration,
       * so we look for the IDENTIFIER child of the element and return its text.
       */
      private fun getDeclarationName(element: PsiElement): String? =
        element.node.findChildByType(PermifyTypes.IDENTIFIER)?.text
    }

    override fun getChildren(): Array<StructureViewTreeElement> = when (element) {
      is PsiFile -> element.children
        .filter {
          it.elementType in setOf(
            PermifyTypes.ENTITY_DEF,
            PermifyTypes.RULE_DEF
          )
        }
        .map { PermifyStructureViewElement(it) }
        .toTypedArray()

      is PermifyEntityDef -> element.children
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

      else -> emptyArray()
    }
  }
}
