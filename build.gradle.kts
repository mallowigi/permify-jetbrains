import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

fun properties(key: String) = providers.gradleProperty(key).get()

fun fileProperties(key: String) = project.findProperty(key).toString().let { if (it.isNotEmpty()) file(it) else null }

fun environment(key: String) = providers.environmentVariable(key)



plugins {
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.gradleIntelliJPlugin)
  alias(libs.plugins.changelog)
  alias(libs.plugins.detekt)
  alias(libs.plugins.ktlint)
}

val pluginGroup: String = properties("pluginGroup")
val pluginName: String = properties("pluginName")
val pluginVersion: String = properties("pluginVersion")
val pluginSinceBuild: String = properties("pluginSinceBuild")
val pluginUntilBuild: String = properties("pluginUntilBuild")
val platformVersion: String = properties("platformVersion")
val javaVersion: String = properties("javaVersion")

group = pluginGroup
version = pluginVersion

repositories {
  mavenCentral()
  mavenLocal()
  gradlePluginPortal()

  intellijPlatform {
    defaultRepositories()
    jetbrainsRuntime()
  }
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
  testImplementation("junit:junit:4.13.2")

  intellijPlatform {
    testFramework(TestFrameworkType.Platform)
    intellijIdeaUltimate(platformVersion) {
      useInstaller = false
    }
    pluginVerifier()
    zipSigner()

    bundledPlugins(
      "com.intellij.java",
      "org.jetbrains.plugins.textmate",
    )

    bundledModule("intellij.textmate")
    bundledModule("intellij.textmate.core")
  }
}


kotlin {
  jvmToolchain(javaVersion.toInt())
}

intellijPlatform {
  pluginConfiguration {
    id = pluginGroup
    name = pluginName
    version = pluginVersion

    ideaVersion {
      sinceBuild = pluginSinceBuild
      untilBuild = pluginUntilBuild
    }

    changeNotes = provider {
      with(changelog) {
        renderItem(
          changelog
            .getUnreleased()
            .withHeader(false)
            .withEmptySections(false),
          Changelog.OutputType.HTML,
        )
      }
    }
  }

  publishing {
    token = environment("PUBLISH_TOKEN")
    channels = listOf(pluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
  }

  signing {
    certificateChain = environment("CERTIFICATE_CHAIN")
    privateKey = environment("PRIVATE_KEY")
    password = environment("PRIVATE_KEY_PASSWORD")
  }
}


changelog {
  path.set("${project.projectDir}/docs/CHANGELOG.md")
  version.set(pluginVersion)
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  unreleasedTerm.set("[Unreleased]")
  groups.set(listOf("Features", "Fixes", "Other", "Chore"))
}

detekt {
  config.setFrom("./detekt-config.yml")
  buildUponDefaultConfig = true
  autoCorrect = true
}

tasks {
  javaVersion.let {
    // Set the compatibility versions to 21
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }

    withType<Detekt> {
      jvmTarget = it
      reports.xml.required.set(true)
    }
  }

  wrapper {
    gradleVersion = properties("gradleVersion")
  }

  buildSearchableOptions {
    enabled = false
  }

  register("markdownToHtml") {
    val input = File("./docs/CHANGELOG.md")
    File("./docs/CHANGELOG.html").run {
      writeText(markdownToHTML(input.readText()))
    }
  }
}
