# Permify Language Support

This plugin adds basic support for the **Permify authorization language** to JetBrains IDEs.

[Permify](https://permify.co/) is a set of tools based on a domain-specific language that allows developers to define and enforce
authorization rules in their applications. Inspired by Google Zanzibar.

This plugin provides syntax highlighting and code completion for the _Permify Domain Specific Language (DSL)_ through
the [TextMate Bundles](https://plugins.jetbrains.com/plugin/7221-textmate-bundles) plugin.

## Features

- Syntax highlighting
- Basic code completion
- Automatic indentation
- Simple commenting
- Bracket matching

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
`src/main/gen`), in addition to the existing TextMate-based syntax highlighting and
text-based formatter (both left untouched — they don't depend on PSI). This unlocks
future IDE features that need a real syntax tree. Still open:

- [ ] CEL-aware parsing of `rule { ... }` bodies (currently captured as one opaque,
      brace-balanced token; the expression inside is Google CEL, not Permify's own
      grammar)
- [ ] References/go-to-definition and rename for `@EntityType`/`#relation` subject
      references and permission/action/rule call targets
- [ ] Inspections (e.g. restricting rule-call arguments to bare identifiers, matching
      Permify's own parser, currently accepted permissively by the grammar)
- [ ] Structure view based on the PSI tree
- [ ] Migrating the text-based formatter to a PSI-based `FormattingModelBuilder`

## Credits

* [permify.co](https://permify.co/)
* [TextMate Bundles Specification](https://raw.githubusercontent.com/martinring/tmlanguage/master/tmlanguage.json)
