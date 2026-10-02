package com.mallowigi.permify.lang.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;

import static com.intellij.psi.TokenType.*;
import static com.mallowigi.permify.lang.psi.PermifyTypes.*;

%%

%{
	private int ruleBraceDepth = 0;
%}

%public
%class _PermifyLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE=\s+

IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
NUMBER=[0-9]+(\.[0-9]+)?
STRING=\"([^\"\\]|\\.)*\"

LINE_COMMENT="//"[^\r\n]*
BLOCK_COMMENT="/*"~"*/"

%xstate RULE_PARAMS
%xstate AFTER_RULE_PARAMS
%xstate IN_RULE_BODY

%%

{WHITE_SPACE} { return WHITE_SPACE; }
{LINE_COMMENT} { return LINE_COMMENT; }
{BLOCK_COMMENT} { return BLOCK_COMMENT; }

// Keywords
"entity" { return ENTITY; }
"relation" { return RELATION; }
"permission" { return PERMISSION; }
"attribute" { return ATTRIBUTE; }
"action" { return ACTION; }
"rule" {
    yybegin(RULE_PARAMS);
    return RULE;
}
"and" { return AND; }
"or" { return OR; }
"not" { return NOT; }
"in" { return IN; }

// Types
"boolean" { return TYPE_BOOLEAN; }
"string" { return TYPE_STRING; }
"integer" { return TYPE_INTEGER; }
"double" { return TYPE_DOUBLE; }

// Punctuation
"(" { return LPAREN; }
")" { return RPAREN; }
"{" { return LBRACE; }
"}" { return RBRACE; }
"[" { return LBRACKET; }
"]" { return RBRACKET; }
"," { return COMMA; }
";" { return SEMICOLON; }
"." { return DOT; }
"=" { return EQ; }
"@" { return AT; }
"#" { return HASH; }

// Numbers, Strings and identifiers
{NUMBER} { return NUMBER; }
{STRING} { return STRING; }
{IDENTIFIER} { return IDENTIFIER; }

// Rule handling
<RULE_PARAMS> {
	{WHITE_SPACE} { return WHITE_SPACE; }
	{LINE_COMMENT} { return LINE_COMMENT; }
	{BLOCK_COMMENT} { return BLOCK_COMMENT; }
  {IDENTIFIER} { return IDENTIFIER; }

  "," { return COMMA; }
  "[" { return LBRACKET; }
  "]" { return RBRACKET; }

  // Types for the rules
  "boolean" { return TYPE_BOOLEAN; }
  "string" { return TYPE_STRING; }
  "integer" { return TYPE_INTEGER; }
  "double" { return TYPE_DOUBLE; }

  "(" { return LPAREN; }

	")" {
		yybegin(AFTER_RULE_PARAMS);
		return RPAREN;
	}

	[^] { return BAD_CHARACTER; }
}

<AFTER_RULE_PARAMS> {
	{WHITE_SPACE} { return WHITE_SPACE; }

  "{" {
    ruleBraceDepth = 1;
		yybegin(IN_RULE_BODY);
		return LBRACE;
	}

	[^] { return BAD_CHARACTER; }
}

<IN_RULE_BODY> {
	"{" {
		ruleBraceDepth++;
		return RULE_BODY_CONTENT;
	}

  "}" {
		ruleBraceDepth--;
		if (ruleBraceDepth == 0) {
			yybegin(YYINITIAL);
      return RBRACE;
    }
		return RULE_BODY_CONTENT;
	}

  [^] {
    return RULE_BODY_CONTENT;
  }
}

[^] { return BAD_CHARACTER; }
