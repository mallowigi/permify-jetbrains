package com.mallowigi.permify

import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.nio.file.Path

class PermifyTextMateBundleProvider : TextMateBundleProvider {
  override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
    return TextMateBundleProvider.PluginBundle(
      "Permify",
      Path.of("")
    )
  }
}
