package com.mallowigi.permify

import com.intellij.application.options.*
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.*

class PermifyLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
  override fun getLanguage(): Language = PermifyLanguage

  override fun getCodeSample(settingsType: SettingsType): String = SAMPLE

  override fun getIndentOptionsEditor(): IndentOptionsEditor = SmartIndentOptionsEditor(this)

  override fun createConfigurable(
    settings: CodeStyleSettings,
    modelSettings: CodeStyleSettings
  ): CodeStyleConfigurable =
    object : CodeStyleAbstractConfigurable(settings, modelSettings, language.displayName) {
      override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel =
        object : TabbedLanguageCodeStylePanel(language, currentSettings, settings) {}

      override fun getHelpTopic(): String? = null
    }

  override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
    when (settingsType) {
      SettingsType.INDENT_SETTINGS -> consumer.showStandardOptions(
        "USE_TAB_CHARACTER",
        "INDENT_SIZE",
      )

      SettingsType.SPACING_SETTINGS -> {
        consumer.showStandardOptions(
          "SPACE_AROUND_ASSIGNMENT_OPERATORS",
          "SPACE_BEFORE_DO_LBRACE",
        )
        consumer.renameStandardOption("SPACE_BEFORE_DO_LBRACE", "Space before opening brace")
      }

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
    indentOptions.USE_TAB_CHARACTER = false
    indentOptions.INDENT_SIZE = 4
    commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true
    commonSettings.SPACE_BEFORE_DO_LBRACE = true
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
