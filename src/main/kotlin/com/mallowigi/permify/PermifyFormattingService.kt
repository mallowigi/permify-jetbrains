package com.mallowigi.permify

import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiFile
import com.mallowigi.permify.formatter.PermifyFormatter
import com.mallowigi.permify.formatter.PermifyFormatterOptions

class PermifyFormattingService : AsyncDocumentFormattingService() {
  override fun getFeatures(): Set<FormattingService.Feature?> =
    mutableSetOf(FormattingService.Feature.AD_HOC_FORMATTING)

  override fun canFormat(psiFile: PsiFile): Boolean = psiFile.virtualFile?.fileType == PermifyFileType

  override fun createFormattingTask(req: AsyncFormattingRequest): FormattingTask = object : FormattingTask {
    override fun cancel(): Boolean = false

    override fun run() {
      try {
        val codeStyleSettings = req.context.codeStyleSettings
        val commonSettings = codeStyleSettings.getCommonSettings(PermifyLanguage)
        val indentOptions = req.context.virtualFile?.let { codeStyleSettings.getIndentOptions(it.fileType) }
          ?: codeStyleSettings.indentOptions

        val formatted = PermifyFormatter.format(
          req.documentText, PermifyFormatterOptions(
            useTabCharacter = indentOptions.USE_TAB_CHARACTER,
            indentSize = indentOptions.INDENT_SIZE,
            maxBlankLines = commonSettings.KEEP_BLANK_LINES_IN_CODE,
            spaceAroundOperators = commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS,
            spaceAroundBraces = !commonSettings.SPACE_WITHIN_BRACES // inverted: our flag means "strip spaces", theirs means "keep spaces"
          )
        )
        req.onTextReady(formatted)
      } catch (e: Exception) {
        req.onError("Formatting failed", e.message ?: "Unknown error")
      }
    }
  }

  override fun getNotificationGroupId(): String = "Permify"

  override fun getName(): @NlsSafe String = "Permify Formatting"
}
