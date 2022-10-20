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
import javax.swing.border.BevelBorder;

/**
 * This class represents the bottom part of the <code>SudokuControl</code> in
 * which the current time spent during the process of solving the sudoku puzzle
 * (in seconds) is displayed.
 */
public final class SudokuTimer extends JLabel implements Runnable {

    private static final StringBuilder TEXT = new StringBuilder();

    private boolean done = false;
    private long startDate = 0;
    private long endDate = 0;

    /**
     * This public constructor sets up the GUI of this <code>SudokuTimer</code>
     * and creates and starts the thread respnsible for measuring and displaying
     * the time spent solving the Sudoku.
     */
    public SudokuTimer() {
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setHorizontalAlignment(SwingConstants.CENTER);
        new Thread(this).start();
    }

    private void display() {
        if (startDate != 0) {
            if (endDate == 0) {
                long time = (System.currentTimeMillis() - startDate) / 1000;
                long min = time / 60;
                long sec = time % 60;
                TEXT.setLength(0);
                TEXT.append(min < 10 ? "0" : "").append(min).append(':');
                TEXT.append(sec < 10 ? "0" : "").append(sec);
                setText(TEXT.toString());
            }
        } else {
            setText("00:00");
        }
    }

    /**
     * This method starts (or restarts) the timekeeping process of this
     * <code>SudokuTimer</code>.
     */
    public void start() {
        if (endDate == 0) {
            startDate = System.currentTimeMillis();
        } else {
            startDate = System.currentTimeMillis() - endDate + startDate;
            endDate = 0;
        }
    }

    /**
     * This method halts the timekeeping process of this <code>SudokuTimer</code>.
     */
    public void pause() {
        endDate = System.currentTimeMillis();
    }

    /**
     * This method resets the timekeeping process (to zero seconds).
     */
    public void reset() {
        this.setForeground(SudokuConstants.BLACK);
        done = false;
        startDate = 0;
        endDate = 0;
    }

    /**
     * This method is responsible for repeatedly updating the time spent solving.
     */
    public void run() {
        try {
            while (true) {
                if (!done) {
                    display();
                }
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            // interrupted, stop thread execution
        }
    }

    /**
     * This method is used when the Sudoku has been successfully solved and
     * displays an according message (including the time needed for solving).
     */
    public void congratulate() {
        done = true;
        long time = (System.currentTimeMillis() - startDate) / 1000;
        long min = time / 60;
        long sec = time % 60;
        TEXT.setLength(0);
        TEXT.append("Sudoku solved in ");
        TEXT.append(min < 10 ? "0" : "").append(min).append(':');
        TEXT.append(sec < 10 ? "0" : "").append(sec);
        TEXT.append(" minutes!");
        this.setForeground(SudokuConstants.GREEN);
        setText(TEXT.toString());
    }

}
