// This is a generated file. Not intended for manual editing.
package com.mallowigi.permify.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.mallowigi.permify.lang.psi.impl.*;

public interface PermifyTypes {

  IElementType ACTION_DEF = new PermifyElementType("ACTION_DEF");
  IElementType ATTRIBUTE_DEF = new PermifyElementType("ATTRIBUTE_DEF");
  IElementType ENTITY_DEF = new PermifyElementType("ENTITY_DEF");
  IElementType EXPR = new PermifyElementType("EXPR");
  IElementType MEMBER_EXPR = new PermifyElementType("MEMBER_EXPR");
  IElementType PARAM_LIST = new PermifyElementType("PARAM_LIST");
  IElementType PAREN_EXPR = new PermifyElementType("PAREN_EXPR");
  IElementType PERMISSION_DEF = new PermifyElementType("PERMISSION_DEF");
  IElementType PRIMARY_EXPR = new PermifyElementType("PRIMARY_EXPR");
  IElementType RELATION_DEF = new PermifyElementType("RELATION_DEF");
  IElementType RULE_BODY = new PermifyElementType("RULE_BODY");
  IElementType RULE_CALL = new PermifyElementType("RULE_CALL");
  IElementType RULE_DEF = new PermifyElementType("RULE_DEF");
  IElementType RULE_PARAM = new PermifyElementType("RULE_PARAM");
  IElementType SUBJECT_REF = new PermifyElementType("SUBJECT_REF");
  IElementType TYPE = new PermifyElementType("TYPE");

  IElementType ACTION = new PermifyTokenType("action");
  IElementType AND = new PermifyTokenType("and");
  IElementType AT = new PermifyTokenType("@");
  IElementType ATTRIBUTE = new PermifyTokenType("attribute");
  IElementType BLOCK_COMMENT = new PermifyTokenType("BLOCK_COMMENT");
  IElementType COMMA = new PermifyTokenType(",");
  IElementType DOT = new PermifyTokenType(".");
  IElementType ENTITY = new PermifyTokenType("entity");
  IElementType EQ = new PermifyTokenType("=");
  IElementType HASH = new PermifyTokenType("#");
  IElementType IDENTIFIER = new PermifyTokenType("IDENTIFIER");
  IElementType IN = new PermifyTokenType("in");
  IElementType LBRACE = new PermifyTokenType("{");
  IElementType LBRACKET = new PermifyTokenType("[");
  IElementType LINE_COMMENT = new PermifyTokenType("LINE_COMMENT");
  IElementType LPAREN = new PermifyTokenType("(");
  IElementType NOT = new PermifyTokenType("not");
  IElementType NUMBER = new PermifyTokenType("NUMBER");
  IElementType OR = new PermifyTokenType("or");
  IElementType PERMISSION = new PermifyTokenType("permission");
  IElementType RBRACE = new PermifyTokenType("}");
  IElementType RBRACKET = new PermifyTokenType("]");
  IElementType RELATION = new PermifyTokenType("relation");
  IElementType RPAREN = new PermifyTokenType(")");
  IElementType RULE = new PermifyTokenType("rule");
  IElementType RULE_BODY_CONTENT = new PermifyTokenType("RULE_BODY_CONTENT");
  IElementType SEMICOLON = new PermifyTokenType(";");
  IElementType STRING = new PermifyTokenType("STRING");
  IElementType TYPE_BOOLEAN = new PermifyTokenType("boolean");
  IElementType TYPE_DOUBLE = new PermifyTokenType("double");
  IElementType TYPE_INTEGER = new PermifyTokenType("integer");
  IElementType TYPE_STRING = new PermifyTokenType("string");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ACTION_DEF) {
        return new PermifyActionDefImpl(node);
      }
      else if (type == ATTRIBUTE_DEF) {
        return new PermifyAttributeDefImpl(node);
      }
      else if (type == ENTITY_DEF) {
        return new PermifyEntityDefImpl(node);
      }
      else if (type == EXPR) {
        return new PermifyExprImpl(node);
      }
      else if (type == MEMBER_EXPR) {
        return new PermifyMemberExprImpl(node);
      }
      else if (type == PARAM_LIST) {
        return new PermifyParamListImpl(node);
      }
      else if (type == PAREN_EXPR) {
        return new PermifyParenExprImpl(node);
      }
      else if (type == PERMISSION_DEF) {
        return new PermifyPermissionDefImpl(node);
      }
      else if (type == PRIMARY_EXPR) {
        return new PermifyPrimaryExprImpl(node);
      }
      else if (type == RELATION_DEF) {
        return new PermifyRelationDefImpl(node);
      }
      else if (type == RULE_BODY) {
        return new PermifyRuleBodyImpl(node);
      }
      else if (type == RULE_CALL) {
        return new PermifyRuleCallImpl(node);
      }
      else if (type == RULE_DEF) {
        return new PermifyRuleDefImpl(node);
      }
      else if (type == RULE_PARAM) {
        return new PermifyRuleParamImpl(node);
      }
      else if (type == SUBJECT_REF) {
        return new PermifySubjectRefImpl(node);
      }
      else if (type == TYPE) {
        return new PermifyTypeImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
