package com.mallowigi.permify

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class PermifyTemplateContextType : TemplateContextType(PERMIFY) {
  override fun isInContext(context: TemplateActionContext): Boolean {

    return isPermifyFile(context.file)
  }
}
