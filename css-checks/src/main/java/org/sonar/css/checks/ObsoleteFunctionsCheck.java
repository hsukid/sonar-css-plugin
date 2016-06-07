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

import java.text.MessageFormat;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.CssCheck;
import org.sonar.css.model.Function;
import org.sonar.css.parser.CssGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "obsolete-functions",
  name = "Obsolete functions should not be used",
  priority = Priority.MAJOR,
  tags = {Tags.BROWSER_COMPATIBILITY})
@ActivatedByDefault
@SqaleConstantRemediation("10min")
public class ObsoleteFunctionsCheck extends CssCheck {

  @Override
  public void init() {
    subscribeTo(CssGrammar.FUNCTION);
  }

  @Override
  public void leaveNode(AstNode functionNode) {
    Function function = new Function(functionNode.getFirstChild(CssGrammar.IDENT).getTokenValue());
    if (function.getStandardFunction().isObsolete()) {
      addIssue(
        this,
        MessageFormat.format("Remove this usage of the obsolete / not on W3C Standards track \"{0}\" function.", function.getStandardFunction().getName()),
        functionNode.getFirstChild(CssGrammar.IDENT));
    }
  }

}
