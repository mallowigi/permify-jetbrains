package com.mallowigi.permify.highlighter

import com.intellij.openapi.application.PathManager
import com.mallowigi.permify.PermifyFileType
import org.jetbrains.plugins.textmate.bundles.TextMateNioResourceReader
import org.jetbrains.plugins.textmate.bundles.readTextMateBundle
import org.jetbrains.plugins.textmate.language.TextMateConcurrentMapInterner
import org.jetbrains.plugins.textmate.language.TextMateLanguageDescriptor
import org.jetbrains.plugins.textmate.language.syntax.TextMateSyntaxTableBuilder
import org.jetbrains.plugins.textmate.plist.JsonOrXmlOrYamlPlistReader
import org.jetbrains.plugins.textmate.plist.JsonPlistReader
import org.jetbrains.plugins.textmate.plist.XmlPlistReader
import org.jetbrains.plugins.textmate.plist.YamlPlistReader
import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Path
import java.security.MessageDigest
import java.util.HexFormat
import java.util.zip.ZipInputStream

private fun getBundlePath(): Path {
  try {
    val resource = PermifyFileType::class.java.classLoader.getResource("bundles/permify.zip")
      ?: error("TextMate bundle resource not found")

    // Key the extraction directory by the zip's content, so it is only re-extracted
    // when the bundled grammar actually changes, instead of on every plugin/lexer load.
    val bundleHash = resource.openStream().use { input ->
      val digest = MessageDigest.getInstance("SHA-256").digest(input.readBytes())
      HexFormat.of().formatHex(digest)
    }
    val bundleDirectory = PathManager.getSystemDir()
      .resolve("permify")
      .resolve("textmate")
      .resolve(bundleHash)
      .toFile()

    if (!bundleDirectory.exists()) {
      deleteFile(bundleDirectory.parentFile)
      bundleDirectory.mkdirs()
      resource.openStream().use { extract(ZipInputStream(it), bundleDirectory) }
    }

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

private val cachedTextMateLanguageDescriptor: TextMateLanguageDescriptor by lazy {
  try {
    val bundle = readTextMateBundle(
      fallbackBundleName = "permify",
      plistReader = JsonOrXmlOrYamlPlistReader(JsonPlistReader(), XmlPlistReader(), YamlPlistReader()),
      resourceReader = TextMateNioResourceReader(getBundlePath()),
    )
    val builder = TextMateSyntaxTableBuilder(TextMateConcurrentMapInterner())
    val grammars = bundle.readGrammars()
    for (grammar in grammars) {
      builder.addSyntax(grammar.plist.value)
    }
    val syntax = builder.build()
    syntax.getLanguageDescriptor("source.perm")
  } catch (e: IOException) {
    throw RuntimeException(e)
  }
}

fun getTextMateLanguageDescriptor(): TextMateLanguageDescriptor = cachedTextMateLanguageDescriptor
