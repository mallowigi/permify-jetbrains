// This is a generated file. Not intended for manual editing.
package com.mallowigi.permify.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PermifyEntityDef extends PsiElement {

  @NotNull
  List<PermifyActionDef> getActionDefList();

  @NotNull
  List<PermifyAttributeDef> getAttributeDefList();

  @NotNull
  List<PermifyPermissionDef> getPermissionDefList();

  @NotNull
  List<PermifyRelationDef> getRelationDefList();

  @NotNull
  List<PermifyRuleDef> getRuleDefList();

}
