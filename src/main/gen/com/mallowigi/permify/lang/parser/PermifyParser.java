// This is a generated file. Not intended for manual editing.
package com.mallowigi.permify.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.mallowigi.permify.lang.psi.PermifyTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PermifyParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // ACTION IDENTIFIER EQ expr
  public static boolean action_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "action_def")) return false;
    if (!nextTokenIs(b, ACTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ACTION_DEF, null);
    r = consumeTokens(b, 1, ACTION, IDENTIFIER, EQ);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AND | OR | NOT
  static boolean and_or_not(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_or_not")) return false;
    boolean r;
    r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, NOT);
    return r;
  }

  /* ********************************************************** */
  // ATTRIBUTE IDENTIFIER type
  public static boolean attribute_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_def")) return false;
    if (!nextTokenIs(b, ATTRIBUTE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_DEF, null);
    r = consumeTokens(b, 1, ATTRIBUTE, IDENTIFIER);
    p = r; // pin = 1
    r = r && type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // relation_def | attribute_def | permission_def | action_def | rule_def
  static boolean entity_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entity_body")) return false;
    boolean r;
    r = relation_def(b, l + 1);
    if (!r) r = attribute_def(b, l + 1);
    if (!r) r = permission_def(b, l + 1);
    if (!r) r = action_def(b, l + 1);
    if (!r) r = rule_def(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ENTITY IDENTIFIER LBRACE entity_body* RBRACE
  public static boolean entity_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entity_def")) return false;
    if (!nextTokenIs(b, ENTITY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENTITY_DEF, null);
    r = consumeTokens(b, 1, ENTITY, IDENTIFIER, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, entity_def_3(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // entity_body*
  private static boolean entity_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entity_def_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!entity_body(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "entity_def_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // primary_expr (and_or_not primary_expr)*
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    if (!nextTokenIs(b, "<expr>", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR, "<expr>");
    r = primary_expr(b, l + 1);
    r = r && expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (and_or_not primary_expr)*
  private static boolean expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_1", c)) break;
    }
    return true;
  }

  // and_or_not primary_expr
  private static boolean expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = and_or_not(b, l + 1);
    r = r && primary_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOT IDENTIFIER)*
  public static boolean member_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_expr")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && member_expr_1(b, l + 1);
    exit_section_(b, m, MEMBER_EXPR, r);
    return r;
  }

  // (DOT IDENTIFIER)*
  private static boolean member_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!member_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "member_expr_1", c)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean member_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // rule_param (COMMA rule_param)*
  public static boolean param_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rule_param(b, l + 1);
    r = r && param_list_1(b, l + 1);
    exit_section_(b, m, PARAM_LIST, r);
    return r;
  }

  // (COMMA rule_param)*
  private static boolean param_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!param_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "param_list_1", c)) break;
    }
    return true;
  }

  // COMMA rule_param
  private static boolean param_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && rule_param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN expr RPAREN
  public static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAREN_EXPR, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PERMISSION IDENTIFIER EQ expr
  public static boolean permission_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "permission_def")) return false;
    if (!nextTokenIs(b, PERMISSION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PERMISSION_DEF, null);
    r = consumeTokens(b, 1, PERMISSION, IDENTIFIER, EQ);
    p = r; // pin = 1
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // rule_call | paren_expr | member_expr
  public static boolean primary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expr")) return false;
    if (!nextTokenIs(b, "<primary expr>", IDENTIFIER, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY_EXPR, "<primary expr>");
    r = rule_call(b, l + 1);
    if (!r) r = paren_expr(b, l + 1);
    if (!r) r = member_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // RELATION IDENTIFIER subject_ref+
  public static boolean relation_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relation_def")) return false;
    if (!nextTokenIs(b, RELATION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RELATION_DEF, null);
    r = consumeTokens(b, 1, RELATION, IDENTIFIER);
    p = r; // pin = 1
    r = r && relation_def_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // subject_ref+
  private static boolean relation_def_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relation_def_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = subject_ref(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!subject_ref(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "relation_def_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // top_level_item*
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    while (true) {
      int c = current_position_(b);
      if (!top_level_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "root", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // RULE_BODY_CONTENT*
  public static boolean rule_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_body")) return false;
    Marker m = enter_section_(b, l, _NONE_, RULE_BODY, "<rule body>");
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, RULE_BODY_CONTENT)) break;
      if (!empty_element_parsed_guard_(b, "rule_body", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER LPAREN (expr (COMMA expr)*)? RPAREN
  public static boolean rule_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_call")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_CALL, null);
    r = consumeTokens(b, 2, IDENTIFIER, LPAREN);
    p = r; // pin = 2
    r = r && report_error_(b, rule_call_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (expr (COMMA expr)*)?
  private static boolean rule_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_call_2")) return false;
    rule_call_2_0(b, l + 1);
    return true;
  }

  // expr (COMMA expr)*
  private static boolean rule_call_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_call_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && rule_call_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA expr)*
  private static boolean rule_call_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_call_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!rule_call_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "rule_call_2_0_1", c)) break;
    }
    return true;
  }

  // COMMA expr
  private static boolean rule_call_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_call_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // RULE IDENTIFIER LPAREN param_list? RPAREN LBRACE rule_body RBRACE
  public static boolean rule_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_def")) return false;
    if (!nextTokenIs(b, RULE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_DEF, null);
    r = consumeTokens(b, 1, RULE, IDENTIFIER, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, rule_def_3(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, RPAREN, LBRACE)) && r;
    r = p && report_error_(b, rule_body(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // param_list?
  private static boolean rule_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_def_3")) return false;
    param_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER type
  public static boolean rule_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_param")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_PARAM, null);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // AT IDENTIFIER (HASH IDENTIFIER)?
  public static boolean subject_ref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subject_ref")) return false;
    if (!nextTokenIs(b, AT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SUBJECT_REF, null);
    r = consumeTokens(b, 1, AT, IDENTIFIER);
    p = r; // pin = 1
    r = r && subject_ref_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (HASH IDENTIFIER)?
  private static boolean subject_ref_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subject_ref_2")) return false;
    subject_ref_2_0(b, l + 1);
    return true;
  }

  // HASH IDENTIFIER
  private static boolean subject_ref_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subject_ref_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, HASH, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // entity_def | rule_def
  static boolean top_level_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_item")) return false;
    if (!nextTokenIs(b, "", ENTITY, RULE)) return false;
    boolean r;
    r = entity_def(b, l + 1);
    if (!r) r = rule_def(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (TYPE_BOOLEAN | TYPE_STRING | TYPE_INTEGER | TYPE_DOUBLE) (LBRACKET RBRACKET)?
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = type_0(b, l + 1);
    p = r; // pin = 1
    r = r && type_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // TYPE_BOOLEAN | TYPE_STRING | TYPE_INTEGER | TYPE_DOUBLE
  private static boolean type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_0")) return false;
    boolean r;
    r = consumeToken(b, TYPE_BOOLEAN);
    if (!r) r = consumeToken(b, TYPE_STRING);
    if (!r) r = consumeToken(b, TYPE_INTEGER);
    if (!r) r = consumeToken(b, TYPE_DOUBLE);
    return r;
  }

  // (LBRACKET RBRACKET)?
  private static boolean type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_1")) return false;
    type_1_0(b, l + 1);
    return true;
  }

  // LBRACKET RBRACKET
  private static boolean type_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACKET, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

}
