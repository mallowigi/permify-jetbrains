package com.mallowigi.permify

import com.intellij.openapi.application.PathManager
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider.PluginBundle
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

class PermifyTextMateBundleProvider : TextMateBundleProvider {
  private val files = listOf(
    "package.json",
    "language-configuration.json",
    "snippets/permify.code-snippets",
    "syntaxes/permify.tmLanguage.json"
  )

  override fun getBundles(): List<PluginBundle> {
    try {
      val tmpDir: Path = Files.createTempDirectory(Path.of(PathManager.getTempPath()), "textmate-permify")

      files.forEach { fileToCopy ->
        val resource: URL? = javaClass.classLoader.getResource("bundles/permify/$fileToCopy")

        resource?.openStream().use { resourceStream ->
          if (resourceStream != null) {
            val target: Path = tmpDir.resolve(fileToCopy)
            Files.createDirectories(target.parent)
            Files.copy(resourceStream, target)
          }
        }
      }

      val bundle = PluginBundle("Permify", tmpDir)
      return listOf(bundle)
    } catch (e: IOException) {
      throw RuntimeException(e)
    }
  }
}
