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

public class PermifyParamListImpl extends ASTWrapperPsiElement implements PermifyParamList {

  public PermifyParamListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PermifyVisitor visitor) {
    visitor.visitParamList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PermifyVisitor) accept((PermifyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PermifyRuleParam> getRuleParamList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PermifyRuleParam.class);
  }

}
