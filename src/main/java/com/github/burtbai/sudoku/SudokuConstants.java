package com.github.burtbai.sudoku;

/*
 *  Copyright 2006 Jens Voß.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;

import java.awt.*;


/**
 * The <code>SudokuConstants</code> interface defines a number of
 * <code>Color</code> constants used for displaying various GUI elements of the
 * {@link com.github.burtbai.sudoku.SudokuPlugin}.
 */
@SuppressWarnings({"JavaDoc"})
public interface SudokuConstants {

  Color WHITE = JBColor.WHITE;
  Color VERY_LIGHT_GRAY = new JBColor(Gray._240, Gray._240);
  Color LIGHT_GRAY = Gray._204;
  Color DARK_GRAY = JBColor.GRAY;
  Color BLACK = JBColor.BLACK;
  Color RED = new JBColor(new Color(192, 0, 0), new Color(192, 0, 0));
  Color GREEN = new JBColor(new Color(0, 128, 0), new Color(0, 128, 0));

}
