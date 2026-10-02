// This is a generated file. Not intended for manual editing.
package com.mallowigi.permify.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.mallowigi.permify.lang.psi.PermifyTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.mallowigi.permify.lang.psi.*;

public class PermifyEntityDefImpl extends ASTWrapperPsiElement implements PermifyEntityDef {

  public PermifyEntityDefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PermifyVisitor visitor) {
    visitor.visitEntityDef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PermifyVisitor) accept((PermifyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PermifyActionDef> getActionDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyActionDef.class);
  }

  @Override
  @NotNull
  public List<PermifyAttributeDef> getAttributeDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyAttributeDef.class);
  }

  @Override
  @NotNull
  public List<PermifyPermissionDef> getPermissionDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyPermissionDef.class);
  }

  @Override
  @NotNull
  public List<PermifyRelationDef> getRelationDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyRelationDef.class);
  }

  @Override
  @NotNull
  public List<PermifyRuleDef> getRuleDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyRuleDef.class);
  }

}
