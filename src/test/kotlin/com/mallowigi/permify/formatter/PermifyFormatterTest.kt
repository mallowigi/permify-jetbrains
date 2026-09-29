package com.mallowigi.permify.formatter

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PermifyFormatterTest : BasePlatformTestCase() {

  fun testDebugTokens() {
    val text = """
      entity user {}

      entity document {
          relation owner @user
          permission view = owner
          // a comment
          attribute is_public boolean
      }
    """.trimIndent()

    PermifyFormatter.debugTokens(text)

  }

  fun testProtectedRanges() {
    val text = """
      entity user {}

      entity document {
          relation owner @user
          permission view = owner
          // a comment
          attribute is_public boolean
      }
    """.trimIndent()

    val protected = PermifyFormatter.protectedRanges(text)
    for (i in text.indices) {
      when {
        protected[i] -> print("#")
        else -> print("${text[i]}")
      }
    }
  }

  fun testFormat() {
    val text = """
               entity user {}

 entity document {
                                    relation owner @user
  permission view = owner
          // a comment
          attribute is_public boolean         
            }
    """.trimIndent()

    println("<<<START>>>")
    val formatted = PermifyFormatter.format(text)
    println(formatted)
    println("<<<END>>>")
  }
}
