package com.mallowigi.permify

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

@Suppress("UnstableApiUsage")
class PermifyTemplateContextType : TemplateContextType(PERMIFY) {
  override fun isInContext(context: TemplateActionContext): Boolean {
    val file = context.editor?.virtualFile ?: return false

    return file.extension in setOf("perm", "permify")
  }
}
