package com.mallowigi.permify

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile

class PermifyTemplateContextType : TemplateContextType("Permify") {
  fun isPermifyFile(file: PsiFile): Boolean = file.name.endsWith(".perm") || file.name.endsWith(".permify")

  override fun isInContext(context: TemplateActionContext): Boolean = isPermifyFile(context.file)
}
