package com.mallowigi.permify

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.application.options.codeStyle.CodeStyleBlankLinesPanel
import com.intellij.application.options.codeStyle.CodeStyleSpacesPanel
import com.intellij.application.options.codeStyle.WrappingAndBracesPanel
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.*
import com.intellij.util.LocalTimeCounter
import com.mallowigi.permify.formatter.PermifyFormatter
import com.mallowigi.permify.formatter.PermifyFormatterOptions

class PermifyLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
  override fun getLanguage(): Language = PermifyLanguage

  override fun getCodeSample(settingsType: SettingsType): String = SAMPLE

  override fun getIndentOptionsEditor(): IndentOptionsEditor = IndentOptionsEditor(this)

  override fun createConfigurable(
    settings: CodeStyleSettings,
    modelSettings: CodeStyleSettings
  ): CodeStyleConfigurable =
    object : CodeStyleAbstractConfigurable(settings, modelSettings, language.displayName) {
      override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel =
        object : TabbedLanguageCodeStylePanel(language, currentSettings, settings) {
          override fun addSpacesTab(settings: CodeStyleSettings) {
            addTab(object : CodeStyleSpacesPanel(settings) {
              override fun getDefaultLanguage(): Language = language
              override fun shouldHideOptions(): Boolean = true
              override fun doReformat(project: Project, psiFile: PsiFile): PsiFile =
                reformatPreview(project, psiFile)
            })
          }

          override fun addBlankLinesTab(settings: CodeStyleSettings) {
            addTab(object : CodeStyleBlankLinesPanel(settings) {
              override fun getDefaultLanguage(): Language = language
              override fun doReformat(project: Project, psiFile: PsiFile): PsiFile =
                reformatPreview(project, psiFile)
            })
          }

          override fun addWrappingAndBracesTab(settings: CodeStyleSettings) {
            addTab(object : WrappingAndBracesPanel(settings) {
              override fun getDefaultLanguage(): Language = language
              override fun doReformat(project: Project, psiFile: PsiFile): PsiFile =
                reformatPreview(project, psiFile)
            })
          }

          override fun addIndentOptionsTab(settings: CodeStyleSettings) {
            val editor = this@PermifyLanguageCodeStyleSettingsProvider.indentOptionsEditor
            addTab(object : MyIndentOptionsWrapper(settings, editor) {
              override fun doReformat(project: Project, psiFile: PsiFile): PsiFile =
                reformatPreview(project, psiFile)
            })
          }

          private fun reformatPreview(project: Project, psiFile: PsiFile): PsiFile {
            val options = PermifyFormatterOptions.from(currentSettings)
            val formatted = PermifyFormatter.format(psiFile.text, options)
            return PsiFileFactory.getInstance(project).createFileFromText(
              psiFile.name, psiFile.fileType, formatted, LocalTimeCounter.currentTime(), false
            )
          }
        }

      override fun getHelpTopic(): String? = null
    }

  override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
    when (settingsType) {
      SettingsType.INDENT_SETTINGS -> consumer.showStandardOptions(
        "USE_TAB_CHARACTER",
        "INDENT_SIZE",
        "TAB_SIZE",
      )

      SettingsType.SPACING_SETTINGS -> consumer.showStandardOptions(
        "SPACE_AROUND_ASSIGNMENT_OPERATORS",
        "SPACE_WITHIN_BRACES",
      )

      SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions(
        "KEEP_BLANK_LINES_IN_CODE",
      )

      else -> Unit
    }
  }

  override fun customizeDefaults(
    commonSettings: CommonCodeStyleSettings,
    indentOptions: CommonCodeStyleSettings.IndentOptions,
  ) {
    // Defaults matching PermifyFormatterOptions()'s current hardcoded defaults
    indentOptions.USE_TAB_CHARACTER = false
    indentOptions.INDENT_SIZE = 2
    indentOptions.TAB_SIZE = 2
    commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true
    commonSettings.SPACE_WITHIN_BRACES = true
    commonSettings.KEEP_BLANK_LINES_IN_CODE = 1
  }

  companion object {
    private val SAMPLE = """
      entity user {}

      entity document {
          relation owner @user
          permission view = owner

          // a comment
          attribute is_public boolean
      }
    """.trimIndent()
  }
}
