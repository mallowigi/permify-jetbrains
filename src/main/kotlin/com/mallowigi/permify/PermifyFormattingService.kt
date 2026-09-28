package com.mallowigi.permify

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiFile

class PermifyFormattingService : AsyncDocumentFormattingService() {
  override fun getFeatures(): Set<FormattingService.Feature?> =
    mutableSetOf(FormattingService.Feature.AD_HOC_FORMATTING)

  override fun canFormat(psiFile: PsiFile): Boolean = psiFile.fileType == PermifyFileType

  override fun createFormattingTask(req: AsyncFormattingRequest): FormattingTask? {
    val formattingContext = req.context
    val project = formattingContext.project
    val file = req.ioFile ?: return null

    if (file.extension != "permify") return null

    return object : FormattingTask {
      override fun cancel(): Boolean {
        TODO("Not yet implemented")
      }

      override fun run() {
        TODO("Not yet implemented")
      }
    }
  }

  override fun getNotificationGroupId(): String = "Permify"

  override fun getName(): @NlsSafe String = "Permify Formatting"
}
