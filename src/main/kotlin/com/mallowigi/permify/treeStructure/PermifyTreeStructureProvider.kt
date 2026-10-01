package com.mallowigi.permify.treeStructure

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import org.jetbrains.annotations.Unmodifiable

class PermifyTreeStructureProvider : TreeStructureProvider {
  override fun modify(
    parent: AbstractTreeNode<*>,
    children: Collection<AbstractTreeNode<*>?>,
    settings: ViewSettings?
  ): @Unmodifiable Collection<AbstractTreeNode<*>?> {
    TODO("Not yet implemented")
  }
}
