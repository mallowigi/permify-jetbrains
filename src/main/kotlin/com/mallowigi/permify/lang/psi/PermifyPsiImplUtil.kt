package com.mallowigi.permify.lang.psi

/**
 * Hand-written helper methods mixed into Grammar-Kit-generated PSI interfaces.
 *
 * Grammar-Kit's parser generator scans this class for `public static` methods whose first
 * parameter type matches a generated PSI rule type (e.g. `entityDef`); any match is added as a
 * method on that rule's generated interface/impl, with the body delegating here.
 *
 * Must be `object` + `@JvmStatic` (not top-level functions) so the methods compile as true
 * `public static` members of this exact class, matching what the generator expects.
 *
 * Empty for now — no PSI mixin methods needed yet (references/rename/structure-view are a
 * future phase). Add `@JvmStatic fun getName(element: PermifyEntityDef): String` etc. here
 * once those features are implemented.
 */
object PermifyPsiImplUtil
