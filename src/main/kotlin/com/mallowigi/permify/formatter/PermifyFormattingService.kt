package com.mallowigi.permify.formatter

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiFile
import com.mallowigi.permify.PermifyFileType

class PermifyFormattingService : AsyncDocumentFormattingService() {
  override fun getFeatures(): Set<FormattingService.Feature?> =
    mutableSetOf(FormattingService.Feature.AD_HOC_FORMATTING)

  override fun canFormat(psiFile: PsiFile): Boolean =
    psiFile.viewProvider.virtualFile.fileType == PermifyFileType

  override fun createFormattingTask(req: AsyncFormattingRequest): FormattingTask = object : FormattingTask {
    override fun cancel(): Boolean = false

    override fun run() {
      try {
        val options = PermifyFormatterOptions.from(req.context.codeStyleSettings)
        val formatted = PermifyFormatter.format(req.documentText, options)
        req.onTextReady(formatted)
      } catch (e: Exception) {
        req.onError("Formatting failed", e.message ?: "Unknown error")
      }
    }
  }

  override fun getNotificationGroupId(): String = "Permify"

  override fun getName(): @NlsSafe String = "Permify Formatting"
}
