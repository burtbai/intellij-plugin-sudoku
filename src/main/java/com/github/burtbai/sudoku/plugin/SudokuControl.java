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

import com.github.burtbai.sudoku.Sudoku;
import com.github.burtbai.sudoku.SudokuCache;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * This class consists of a collection of various form elements used to
 * configure the behavior of the <code>SudokuPlugin</code>.
 */
public class SudokuControl extends JPanel {

  /**
   * This inner class is a <code>JPanel</code> containing a
   * <code>JButton</code> of a specific size.
   */
  static class ButtonPanel extends JPanel {

    private static final Dimension ELEMENT_SIZE = new Dimension(140, 25);

    protected JButton button;

    /**
     * This constructor initializes the <code>JButton</code> contained by
     * this <code>ButtonPanel</code>.
     *
     * @param label The text of the <code>JButton</code>
     * @param mnemonic The keyboard shortcut of the <code>JButton</code>
     * @param alignment The horizontal alignment of the <code>JButton</code>
     *         within this <code>ButtonPanel</code>
     * @param listener An <code>ActionListener</code> associated with the
     *         <code>JButton</code> of this <code>ButtonPanel</code>
     */
    ButtonPanel(String label, int mnemonic, int alignment, ActionListener listener) {
      super(new FlowLayout(alignment, 0, 5));
      button = new JButton(label);
      button.setPreferredSize(ELEMENT_SIZE);
      button.addActionListener(listener);
      button.setMnemonic(mnemonic);
      add(button);
    }

    /**
     * This method delegates enablement or disablement to the contained
     * <code>JButton</code>.
     *
     * @param enabled If this is <b>true</b>, the <code>JButton</code> will be
     *         enabled; otherwise, it will be disabled
     */
    @Override
    public void setEnabled(boolean enabled) {
      button.setEnabled(enabled);
    }

  }

  /**
   * This inner class implements the <code>ButtonPanel</code>
   * containing the "pause"/"continue" button.
   */
  static class PauseButton extends ButtonPanel {

    private ActionListener pause;
    private ActionListener cont;

    /**
     * Public constructor. It constructs two (private) <code>ActionListener</code>
     * objects which "take turns" after respective invocations and which
     * alternatingly pause and continue the game.
     *
     * @param table The {@link SudokuTable} object to be notified of the
     *         invocation of one of this <code>PauseButton</code>'s
     *         <code>ActionListener</code>s
     * @param timer The {@link SudokuTimer} object to be notified of the
     *         invocation of one of this <code>PauseButton</code>'s
     *         <code>ActionListener</code>s
     */
    PauseButton(final SudokuTable table, final SudokuTimer timer) {
      super("Pause", KeyEvent.VK_P, SwingConstants.LEFT, null);
      pause = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          timer.pause();
          table.setPauseGame(true);
          button.setText("Continue");
          button.setMnemonic(KeyEvent.VK_O);
          button.removeActionListener(this);
          button.addActionListener(cont);
        }
      };
      cont = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          timer.start();
          table.setPauseGame(false);
          button.setText("Pause");
          button.setMnemonic(KeyEvent.VK_P);
          button.removeActionListener(this);
          button.addActionListener(pause);
        }
      };
      button.addActionListener(pause);
    }
  }

  private SudokuCache sudokuCache;

  private ButtonPanel resetButton;
  private ButtonPanel newButton;
  private ButtonPanel checkButton;
  private PauseButton pauseButton;
  private JSlider slider;
  private SudokuTimer timer;
  private JCheckBox showErrors;

  /**
   * Public constructor. Initializes and lays out all GUI elements
   * positioned below the {@link SudokuTable}.
   *
   * @param table The <code>SudokuTable</code> object placed above this
   *         <code>SudokuControl</code>
   */
  public SudokuControl(final SudokuTable table) {

    sudokuCache = new SudokuCache(this);
    timer = new SudokuTimer();

    setupControls(table);

    this.setPreferredSize(new Dimension(297, 170));
    JPanel innerPanel = new JPanel();
    innerPanel.setPreferredSize(new Dimension(285, 158));
    innerPanel.setLayout(new GridLayout(4, 1));

    JPanel panel = new JPanel(new GridLayout(1, 2));
    panel.add(newButton);
    panel.add(slider);
    innerPanel.add(panel);

    panel = new JPanel(new GridLayout(1, 2));
    panel.add(resetButton);
    panel.add(checkButton);
    innerPanel.add(panel);

    panel = new JPanel(new GridLayout(1, 2));
    panel.add(pauseButton);
    panel.add(showErrors);
    innerPanel.add(panel);

    innerPanel.add(timer);
    this.add(innerPanel);
    this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  /**
   * Getter method for this <code>SudokuControl</code>'s
   * <code>SudokuTimer</code>.
   *
   * @return The <code>SudokuTimer</code> of this <code>SudokuControl</code>
   */
  public SudokuTimer getTimer() {
    return timer;
  }

  private void setupControls(final SudokuTable table) {

    newButton = new ButtonPanel("New sudoku", KeyEvent.VK_N, FlowLayout.LEFT, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Sudoku.Difficulty difficulty = Sudoku.Difficulty.get(slider.getValue());
        table.setSudoku(sudokuCache.getSudoku(difficulty));
        newButton.setEnabled(sudokuCache.isAvailable(difficulty));
        resetButton.setEnabled(true);
        checkButton.setEnabled(true);
        pauseButton.setEnabled(true);
        timer.reset();
        timer.start();
      }
    });
    newButton.setEnabled(false);

    resetButton = new ButtonPanel("Reset", KeyEvent.VK_R, FlowLayout.LEFT, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        table.resetSudoku();
        timer.reset();
        timer.start();
      }
    });
    resetButton.setEnabled(false);

    checkButton = new ButtonPanel("Check", KeyEvent.VK_C, FlowLayout.RIGHT, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String msg;
        String title;
        int type;
        if (table.isComplete()) {
          msg = table.isCorrect() ? "Your solution is correct."
                : "Your solution is not correct.";
          title = table.isCorrect() ? "Congratulations" : "Try again!";
          type = table.isCorrect() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        }
        else {
          msg = table.isCorrect()
                ? "Your solution is still incomplete, but has no errors."
                : "Your solution is still incomplete, but contains errors.";
          title = table.isCorrect() ? "So far so good" : "Get some practice!";
          type = table.isCorrect() ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE;
        }
        JOptionPane.showMessageDialog(SudokuControl.this, msg, title, type);
      }
    });
    checkButton.setEnabled(false);

    pauseButton = new PauseButton(table, timer);
    pauseButton.setEnabled(false);

    slider = new JSlider(0, 2);
    slider.setMajorTickSpacing(1);
    slider.setSnapToTicks(true);
    Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    labels.put(0, new JLabel("easy"));
    labels.put(2, new JLabel("hard"));
    slider.setLabelTable(labels);
    slider.setPaintLabels(true);
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        Sudoku.Difficulty difficulty = Sudoku.Difficulty.get(slider.getValue());
        newButton.setEnabled(sudokuCache.isAvailable(difficulty));
      }
    });

    showErrors = new JCheckBox("Indicate errors");
    showErrors.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        table.setShowErrors(showErrors.isSelected());
      }
    });
    showErrors.setMnemonic(KeyEvent.VK_I);

  }

  /**
   * This method may be used to notify this <code>SudokuControl</code> of
   * the availability of a sudoku puzzle in the {@link SudokuCache}.
   *
   * @param difficulty The <code>Difficulty</code> of the newly available
   *         sudoku puzzle
   */
  public void setSudokuAvailable(Sudoku.Difficulty difficulty) {
    if (slider != null && slider.getValue() == difficulty.ordinal()) {
      newButton.setEnabled(true);
    }
  }

}
