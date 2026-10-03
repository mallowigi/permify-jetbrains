# Permify Language Support

This plugin adds basic support for the **Permify authorization language** to JetBrains IDEs.

[Permify](https://permify.co/) is a set of tools based on a domain-specific language that allows developers to define and enforce
authorization rules in their applications. Inspired by Google Zanzibar.

This plugin provides syntax highlighting, scope-aware code completion, and PSI-based
code intelligence (go to declaration, Find Usages, rename, structure view) for the
_Permify Domain Specific Language (DSL)_.

## Features

- Syntax highlighting
- Scope-aware code completion (keywords + reference-based)
- Automatic indentation
- Simple commenting
- Bracket matching
- Go to Declaration, Find Usages, Rename, Safe Delete
- Unresolved reference highlighting
- Structure view

## Installation

1. Open the Plugin Manager
2. Search for `Permify Language Support`
3. Click `Install`
4. Restart the IDE

Also, please create an `.editorconfig` file if you didn't already do so. This will help the plugin to correctly format and indent the code.

## Screenshots

![CleanShot 2024-06-08 at 18 28 46@2x](https://github.com/mallowigi/permify-tmbundle/assets/5015756/3795cb8f-60bb-4325-8091-65b39b1f5242)

![CleanShot 2024-06-08 at 18 29 30@2x](https://github.com/mallowigi/permify-tmbundle/assets/5015756/9716ebea-5f65-49e9-81ae-cf8d7b88b0a9)

## TODO

- [ ] Color Scheme
- [ ] File Templates
- [x] Convert to a first-party language (lexer + grammar + PSI wiring; see below)
- [ ] LSP Support
- [ ] Error Highlighting
- [ ] Tool Window for visualizing the permissions

### First-party language support progress

The plugin now has a real IntelliJ `Lexer`/`PsiParser`/PSI tree for `.perm` files
(JFlex lexer + Grammar-Kit BNF grammar, generated sources committed under
`src/main/gen`), in addition to the existing TextMate-based syntax highlighting (now
driven by the real PSI lexer via `lang.syntaxHighlighterFactory`) and text-based
formatter. On top of that PSI tree, the plugin now supports:

- References/go-to-declaration, Find Usages, Rename, and Safe Delete for
  `@EntityType`/`#relation` subject references, rule-call targets, and
  permission/action/rule expression operands (file-local resolution only - Permify has
  no import/namespace mechanism)
- Unresolved-reference error highlighting
- A Structure View based on the PSI tree
- Scope-aware keyword completion (`entity`/`rule` at the top level,
  `relation`/`permission`/`attribute`/`action`/`rule` inside an entity body, type
  keywords after an attribute/rule-param name, `and`/`or`/`not` after a complete
  expression operand), alongside the existing reference-based completion

Still open:

- [ ] CEL-aware parsing of `rule { ... }` bodies (currently captured as one opaque,
      brace-balanced token; the expression inside is Google CEL, not Permify's own
      grammar)
- [ ] Chained dotted-segment resolution (e.g. the `admin` part of `parent.admin`) -
      currently only the first segment of a dotted member expression resolves
- [ ] Inspections (e.g. restricting rule-call arguments to bare identifiers, matching
      Permify's own parser, currently accepted permissively by the grammar)
- [ ] Migrating the text-based formatter to a PSI-based `FormattingModelBuilder`

## Credits

* [permify.co](https://permify.co/)
* [TextMate Bundles Specification](https://raw.githubusercontent.com/martinring/tmlanguage/master/tmlanguage.json)
