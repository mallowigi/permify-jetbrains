package com.mallowigi.permify.highlighter

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.mallowigi.permify.PermifyFileType
import org.jetbrains.plugins.textmate.bundles.readTextMateBundle
import org.jetbrains.plugins.textmate.language.TextMateConcurrentMapInterner
import org.jetbrains.plugins.textmate.language.TextMateLanguageDescriptor
import org.jetbrains.plugins.textmate.language.syntax.TextMateSyntaxTableBuilder
import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Path
import java.util.zip.ZipInputStream

private fun getBundlePath(): Path {
  try {
    val plugin = PluginManagerCore.getPlugin(PluginId.getId("com.mallowigi.permify"))
    val version = plugin?.version ?: "latest"
    val bundleDirectory = File("${plugin?.pluginPath}/bundles/$version")

    if (bundleDirectory.exists()) {
      deleteFile(bundleDirectory)
    }

    deleteFile(bundleDirectory.getParentFile())
    bundleDirectory.mkdirs()
    val resource = PermifyFileType::class.java.classLoader.getResourceAsStream("bundles/permify.zip")

    extract(ZipInputStream(resource!!), bundleDirectory)

    return Path.of("${bundleDirectory.path}/permify")
  } catch (ex: IOException) {
    throw UncheckedIOException(ex)
  }
}

private fun extract(zip: ZipInputStream, target: File) {
  zip.use { zip ->
    while (true) {
      val entry = zip.nextEntry ?: break
      val file = File(target, entry.name)

      if (!file.toPath().normalize().startsWith(target.toPath())) {
        throw IOException("Bad zip entry: ${file.absolutePath}")
      }

      if (entry.isDirectory) {
        file.mkdirs()
        continue
      }

      val buffer = ByteArray(4096)
      val parent = file.parentFile
      parent.mkdirs()

      val output = file.outputStream()
      var len: Int
      while (zip.read(buffer).also { len = it } > 0) {
        output.write(buffer, 0, len)
      }
      output.close()
    }
  }
}

private fun deleteFile(file: File) {
  if (file.isDirectory) {
    file.listFiles()?.forEach { deleteFile(it) }
  }
  file.delete()
}

fun getTextMateLanguageDescriptor(): TextMateLanguageDescriptor {
  try {
    val bundle = readTextMateBundle(getBundlePath())
    val builder = TextMateSyntaxTableBuilder(TextMateConcurrentMapInterner())
    val grammars = bundle.readGrammars()
    for (grammar in grammars) {
      builder.addSyntax(grammar.plist.value)
    }
    val syntax = builder.build()
    return TextMateLanguageDescriptor("source.perm", syntax.getSyntax("source.perm"))
  } catch (e: IOException) {
    throw RuntimeException(e)
  }
}
