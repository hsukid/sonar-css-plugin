/*
 * SonarQube CSS / SCSS / Less Analyzer
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
package org.sonar.css.parser.scss;

import org.junit.Test;
import org.sonar.css.parser.LexicalGrammar;
import org.sonar.plugins.css.api.tree.scss.ScssPlaceholderSelectorTree;

import static org.fest.assertions.Assertions.assertThat;

public class ScssPlaceholderSelectorTreeTest extends ScssTreeTest {

  public ScssPlaceholderSelectorTreeTest() {
    super(LexicalGrammar.SCSS_PLACEHOLDER_SELECTOR);
  }

  @Test
  public void scssPlaceholderSelector() {
    checkParsed("%id", "id");
    checkParsed("%abc-def", "abc-def");
  }

  @Test
  public void notScssPlaceholderSelector() {
    checkNotParsed("#");
    checkNotParsed("%");
    checkNotParsed(" %id");
    checkNotParsed("% id");
    checkNotParsed("%%id");
  }

  private void checkParsed(String toParse, String expectedIdentifier) {
    ScssPlaceholderSelectorTree tree = (ScssPlaceholderSelectorTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.percentageSymbol()).isNotNull();
    assertThat(tree.identifier()).isNotNull();
    assertThat(tree.identifier().text()).isEqualTo(expectedIdentifier);
  }

}