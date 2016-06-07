/*
 * SonarQube CSS Plugin
 * Copyright (C) 2013-2016 Tamas Kende and David RACODON
 * mailto: kende.tamas@gmail.com and david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css.checks;

import com.sonar.sslr.api.AstNode;

import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.CssCheck;
import org.sonar.css.parser.CssGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "empty-declaration",
  name = "Empty declarations should be removed",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("2min")
@ActivatedByDefault
public class EmptyDeclarationCheck extends CssCheck {

  private static final String MESSAGE = "Remove this empty declaration.";

  @Override
  public void init() {
    subscribeTo(CssGrammar.SUP_DECLARATION);
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.getChildren() != null && !astNode.getChildren().isEmpty()) {
      List<AstNode> node = astNode.getChildren();
      if (CssGrammar.SEMICOLON.equals(node.get(0).getType())) {
        addIssue(this, MESSAGE, node.get(0));
      }
      for (int i = 1; i < astNode.getChildren().size(); i++) {
        if (CssGrammar.SEMICOLON.equals(node.get(i).getType())
          && !CssGrammar.DECLARATION.equals(node.get(i - 1).getType())
          && !CssGrammar.VARIABLE_DECLARATION.equals(node.get(i - 1).getType())) {
          addIssue(this, MESSAGE, node.get(i));
        }
      }
    }
  }
}
