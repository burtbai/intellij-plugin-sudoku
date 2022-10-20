package com.github.burtbai.sudoku.plugin;

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

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The <code>SudokuPanel</code> class is the main GUI element of the
 * <code>SudokuPlugin</code>. It consists of the (upper) "table" section
 * containing the actual sudoku puzzle and the (lower) "control" section
 * containing buttons and other elements used to configure different
 * aspects of the sudoku.
 */
public class SudokuPanel extends JPanel {

  private SudokuControl control;

  /**
   * Public constructor of the <code>SudokuPanel</code>. It sets up both the
   * "table" and the "control" sections of the panel and adds them to it.
   */
  public SudokuPanel() {
    super(new BorderLayout(10, 10));
    SudokuTable table = new SudokuTable(this);
    add(table, BorderLayout.CENTER);
    control = new SudokuControl(table);
    add(control, BorderLayout.SOUTH);
    setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
  }

  /**
   * Public getter method for this <code>SudokuPanel</code>'s
   * <code>SudokuControl</code> section.
   *
   * @return The (lower) "control" section of this <code>SudokuPanel</code>
   */
  public SudokuControl getControl() {
    return control;
  }

  /**
   * Main entry point for running a <code>SudokuPanel</code> outside the
   * {@link SudokuPlugin} as an application in its own <code>JFrame</code>.
   *
   * @param args Emtpy String array
   */
  public static void main(String[] args) {

    JFrame frame = new JFrame("Sudoku");
    frame.setLocation(500, 100);
    SudokuPanel sudokuPanel = new SudokuPanel();
    frame.add(sudokuPanel);
    frame.pack();
    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

  }

}

