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

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * The <code>SudokuTable</code> is the section of the <code>SudokuPanel</code>
 * above the <code>SudokuControl</code> in which the sudoku 9-by-9-grid is
 * displayed.
 */
public class SudokuTable extends JPanel {

    private SudokuTextField[][] cells = new SudokuTextField[9][9];
    private Sudoku sudoku;
    private boolean showErrors = false;
    private boolean pauseGame = false;
    private boolean complete;
    private boolean correct;
    private SudokuPanel sudokuPanel;

    /**
     * Public constructor.
     *
     * @param sudokuPanel The <code>SudokuPanel</code> in which this
     *                    <code>SudokuTable</code> is displayed
     */
    public SudokuTable(SudokuPanel sudokuPanel) {
        this.sudokuPanel = sudokuPanel;
        Dimension dim = new Dimension(297, 297);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            JPanel block = new JPanel(new GridLayout(3, 3));
            for (int j = 0; j < 9; j++) {
                int r = j / 3 + 3 * (i / 3);
                int c = j % 3 + 3 * (i % 3);
                cells[r][c] = new SudokuTextField(this, r, c);
                block.add(cells[r][c]);
            }
            this.add(block);
            block.setBorder(LineBorder.createBlackLineBorder());
        }
        this.setBorder(LineBorder.createBlackLineBorder());
    }

    private void updateCells() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].display(this.showErrors, this.pauseGame);
            }
        }
    }

    /**
     * The <code>updateStatus()</code> method determines the completeness and
     * correctness of the solution entered; if this solution is complete and
     * correct, a message is displayed in the <code>SudokuTimer</code> area at
     * the bottom of the <code>SudokuControl</code>.
     */
    public void updateStatus() {
        complete = true;
        correct = true;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                SudokuTextField.Status status = cells[r][c].getStatus();
                correct &= status != SudokuTextField.Status.INCORRECT;
                complete &= status != SudokuTextField.Status.EMPTY;
                if (!correct && !complete) {
                    return;
                }
            }
        }
        if (complete && correct) {
            this.sudokuPanel.getControl().getTimer().congratulate();
        }
    }

    /**
     * Setter method for the error indication behavior of this
     * <code>SudokuTable</code>.
     *
     * @param showErrors If this is <b>true</b>, any wrong digit entered will be
     *                   immediately highlighted
     */
    public void setShowErrors(boolean showErrors) {
        this.showErrors = showErrors;
        if (sudoku == null) {
            return;
        }
        updateCells();
    }

    /**
     * Getter method for this <code>SudokuTable</code>'s error indication flag.
     *
     * @return <b>true</b> if immediate error indication is selected;
     * <b>false</b> otherwise
     */
    public boolean getShowErrors() {
        return showErrors;
    }

    /**
     * Setter method for this <code>SudokuTable</code>'s pause flag.
     *
     * @param pauseGame If this is <b>true</b>, all digits entered will be
     *                  hidden, and the timekeeping will be halted; if the
     *                  <code>pauseGame</code> flag is <b>false</b>, all entered digits
     *                  will be redisplayed and the timekeeping will be resumed
     */
    public void setPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
        if (sudoku == null) {
            return;
        }
        updateCells();
    }

    /**
     * Getter method for this <code>SudokuTable</code>'s completeness flag.
     *
     * @return <b>true</b> if no cell is empty; <b>false</b> otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Getter method for this <code>SudokuTable</code>'s correctness flag.
     *
     * @return <b>true</b> if no cell contains an incorrect value; <b>false</b>
     * otherwise
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * This method gives the focus to the selected cell.
     *
     * @param row The row index of the selected cell
     * @param col The column index of the selected cell
     */
    public void focus(int row, int col) {
        cells[(9 + row) % 9][(9 + col) % 9].requestFocus();
    }

    /**
     * Setter method for this <code>SudokuTable</code>'s <code>Sudoku</code>.
     *
     * @param sudoku The <code>Sudoku</code> matrix of this
     *               <code>SudokuTable</code>
     */
    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = sudoku.getValue(r, c);
                if (val >= 0) {
                    cells[r][c].setCorrectSolution(val);
                } else {
                    cells[r][c].pickValue(-val);
                }
            }
        }
        complete = false;
        correct = true;
    }

    /**
     * This method clears the <code>SudokuTable</code> of all manually entered
     * values.
     */
    public void resetSudoku() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = sudoku.getValue(r, c);
                if (val >= 0) {
                    cells[r][c].setText("");
                }
            }
        }
    }

}
