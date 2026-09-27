rootProject.name = "permify"

pluginManagement {
  repositories {
    mavenCentral()
    maven("https://cache-redirector.jetbrains.com/plugins.gradle.org/m2/")
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
