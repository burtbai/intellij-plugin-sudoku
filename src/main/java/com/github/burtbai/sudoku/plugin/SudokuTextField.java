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

import com.github.burtbai.sudoku.SudokuConstants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The instances of this class are the <code>JTextField</code>s of which the
 * <code>SudokuTable</code> consists.
 */
public class SudokuTextField extends JTextField {

    /**
     * This enum is used to enumerate the different statuses a
     * <code>SudokuTextField</code> may have.
     */
    public enum Status {

        /**
         * An empty table cell.
         */
        EMPTY,

        /**
         * A preset (or "picked") table cell.
         */
        PICKED,

        /**
         * A table cell containing a correct manually entered value.
         */
        CORRECT,

        /**
         * A table cell containing an incorrect manually entered value.
         */
        INCORRECT
    }

    private int row;
    private int col;

    private int correctSolution;
    private Status status;


    /**
     * Public constructor.
     *
     * @param sudokuTable The <code>SudokuTable</code> of which this
     *                    <code>SudokuTextField</code> is a part
     * @param row         The row index of this <code>SudokuTextField</code>
     * @param col         The column index of this <code>SudokuTextField</code>
     */
    public SudokuTextField(final SudokuTable sudokuTable, int row, int col) {
        this.row = row;
        this.col = col;
        this.setBorder(LineBorder.createGrayLineBorder());
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(sudokuTable == null
                ? super.getFont()
                : sudokuTable.getFont().deriveFont(Font.PLAIN, 18));
        this.setEditable(false);
        this.setBackground(SudokuConstants.WHITE);
        this.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                setBackground(SudokuConstants.LIGHT_GRAY);
            }

            public void focusLost(FocusEvent e) {
                setBackground(SudokuConstants.WHITE);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SudokuTextField field = SudokuTextField.this;
                if (field.status == Status.PICKED) {
                    return;
                }
                int c = e.getKeyChar();
                if (49 <= c && c <= 57) {
                    int d = c - 48;
                    field.setForeground(d != correctSolution && sudokuTable.getShowErrors()
                            ? SudokuConstants.RED : SudokuConstants.DARK_GRAY);
                    field.setText(String.valueOf(d));
                    sudokuTable.updateStatus();
                } else if (c == KeyEvent.VK_SPACE || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    field.setText("");
                    sudokuTable.updateStatus();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                SudokuTextField parent = SudokuTextField.this;
                int x = parent.col;
                int y = parent.row;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        x--;
                        if (x == -1) {
                            y--;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        x++;
                        if (x == 9) {
                            y++;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        y--;
                        if (y == -1) {
                            x--;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        y++;
                        if (y == 9) {
                            x++;
                        }
                        break;
                    default:
                        return;
                }
                sudokuTable.focus(y, x);
            }

        });
    }

    /**
     * Getter method for this <code>SudokuTextField</code>'s <code>Status</code>.
     *
     * @return The <code>Status</code> of this <code>SudokuTextField</code>
     */
    public Status getStatus() {
        return status;
    }

    /**
     * This method is used to set the correct digit of this
     * <code>SudokuTextField</code> (without displaying it).
     *
     * @param correctSolution The correct digit of this
     *                        <code>SudokuTextField</code>
     */
    public void setCorrectSolution(int correctSolution) {
        super.setText("");
        this.correctSolution = correctSolution;
        this.status = Status.EMPTY;
        setForeground(SudokuConstants.DARK_GRAY);
    }

    /**
     * This method is used to set the correct digit of this picked
     * <code>SudokuTextField</code> (including displaying it).
     *
     * @param i The correct digit of this <code>SudokuTextField</code>
     */
    public void pickValue(int i) {
        setCorrectSolution(i);
        showValue();
        this.status = Status.PICKED;
        setForeground(SudokuConstants.BLACK);
    }

    private void showValue() {
        super.setText(String.valueOf(correctSolution));
    }

    /**
     * This method (like its parent implementation) displays the specified text in
     * this <code>SudokuTextField</code> and additionally determines the
     * <code>Status</code> value.
     *
     * @param t The text to be displayed
     */
    @Override
    public void setText(String t) {
        super.setText(t);
        if (t == null || t.length() == 0) {
            this.status = Status.EMPTY;
        } else if (t.charAt(0) - 48 == correctSolution) {
            this.status = Status.CORRECT;
        } else {
            this.status = Status.INCORRECT;
        }
    }

    /**
     * This method displays the contents of this <code>SudokuTextField</code> in
     * a style which depends on the error indication and pausing settings. If the
     * latter is <b>true</b>, all digits will be hidden; otherwise the entered
     * digits will be visible in a color depending on the error indication flag
     * and the <code>Status</code>.
     *
     * @param showError If this is <b>true</b> (and <code>pauseGame</code> is
     *                  <b>false</b>), incorrect values will be displayed in red color; if
     *                  <code>showError</code> and <code>pauseGame</code> are both
     *                  <b>false</b>, incorrect values will be displayed in normal (gray)
     *                  color.
     * @param pauseGame If this is <b>true</b>, all values (both automatically
     *                  "picked" and manually entered ones) will be hidden.
     */
    public void display(boolean showError, boolean pauseGame) {
        setBackground(pauseGame ? SudokuConstants.VERY_LIGHT_GRAY : SudokuConstants.WHITE);
        if (pauseGame) {
            setForeground(SudokuConstants.VERY_LIGHT_GRAY);
        } else if (status == Status.INCORRECT) {
            setForeground(showError ? SudokuConstants.RED : SudokuConstants.DARK_GRAY);
        } else if (status == Status.CORRECT) {
            setForeground(SudokuConstants.DARK_GRAY);
        } else {
            setForeground(SudokuConstants.BLACK);
        }
    }

}
