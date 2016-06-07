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
import com.sonar.sslr.api.GenericTokenType;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.CssCheck;
import org.sonar.css.parser.CssGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "case",
  name = "Properties, functions and variables should be lower case",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleConstantRemediation("2min")
@ActivatedByDefault
public class CaseCheck extends CssCheck {

  @Override
  public void init() {
    subscribeTo(CssGrammar.PROPERTY, CssGrammar.FUNCTION, CssGrammar.VARIABLE);
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(CssGrammar.PROPERTY)
      && containsUpperCaseCharacter(astNode.getTokenValue())) {
      createIssue(astNode, "property", astNode.getTokenValue());
    } else if (astNode.is(CssGrammar.FUNCTION)
      && containsUpperCaseCharacter(astNode.getTokenValue())) {
      createIssue(astNode.getFirstChild(CssGrammar.IDENT), "function", astNode.getTokenValue());
    } else if (astNode.is(CssGrammar.VARIABLE)
      && containsUpperCaseCharacter(astNode.getFirstChild(GenericTokenType.IDENTIFIER).getTokenValue())) {
      createIssue(astNode, "variable", astNode.getFirstChild(GenericTokenType.IDENTIFIER).getTokenValue());
    }
  }

  private void createIssue(AstNode astNode, String nodeType, String value) {
    addIssue(
      this,
      "Write " + nodeType + " \"" + value + "\" in lowercase.",
      astNode);
  }

  private boolean containsUpperCaseCharacter(String value) {
    return value.matches("^.*[A-Z]+.*$");
  }

}
