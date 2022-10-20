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

import com.github.burtbai.sudoku.dlx.Matrix;
import com.github.burtbai.sudoku.dlx.Row;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class Sudoku extends Matrix {

    private static final String PATTERN_ROW_COLUMN = "R{0,number,integer}C{1,number,integer}";
    private static final String PATTERN_DIGIT_ROW = "D{0,number,integer}R{1,number,integer}";
    private static final String PATTERN_DIGIT_COLUMN = "D{0,number,integer}C{1,number,integer}";
    private static final String PATTERN_DIGIT_BLOCK = "D{0,number,integer}B{1,number,integer}";

    /**
     * This enum is used to distinguish between different difficulty levels to a
     * Sudoku problem. These levels differ by the number of given digits (which is
     * a decent approximation to the actual difficulty of the puzzle).
     */
    public enum Difficulty {

        /**
         * This <code>Difficulty</code> is used to mark relatively easy Sudoku
         * puzzles.
         */
        EASY(33, 36),
        /**
         * This <code>Difficulty</code> is used to mark medium difficult Sudoku
         * puzzles.
         */
        MEDIUM(29, 32),
        /**
         * This <code>Difficulty</code> is used to mark hard-to-solve Sudoku
         * puzzles.
         */
        HARD(25, 28);

        /**
         * This method retrieves the <code>Difficulty</code> of the given order.
         *
         * @param index The order of the <code>Difficulty</code> to be retrieved
         * @return The <code>Difficulty</code> whose order is <em>index</em>
         */
        public static Difficulty get(int index) {
            return values()[index];
        }

        private int min;
        private int max;

        /**
         * Public constructor specifying the <code>Difficulty</code>'s range of
         * numbers of given digits.
         *
         * @param min The minimum number of given digits for this
         *            <code>Difficulty</code>
         * @param max The maximum number of given digits for this
         *            <code>Difficulty</code>
         */
        Difficulty(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * This method determines whether a number of given digits lies in the
         * proper range of this <code>Difficulty</code>.
         *
         * @param size The number to be compared againts this
         *             <code>Difficulty</code>'s range of numbers of given digits
         * @return <b>true</b> if <em>size</em> lies in the range of numbers of
         * given digits of this <code>Difficulty</code> level; <b>false</b>
         * otherwise
         */
        public boolean isInRange(int size) {
            return min <= size && size <= max;
        }
    }

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private int n;
    private int nn;
    private final Row[][][] dlxRows;
    private final int[][] values;
    private Difficulty difficulty;

    /**
     * This public constructor sets up a 9-by-9-Sudoku puzzle with one of a set
     * of acceptable <code>Difficulty</code> levels.
     *
     * @param difficulties An array of acceptable <code>Difficulty</code> levels
     *                     for this <code>Sudoku</code> puzzle.
     */
    public Sudoku(Difficulty... difficulties) {
        this(3, difficulties);
    }

    /**
     * This public constructor sets up a n^2-by-n^2-Sudoku puzzle with one of a
     * set of acceptable <code>Difficulty</code> levels.
     *
     * @param n            The "dimension" of the Sudoku puzzle
     * @param difficulties An array of acceptable <code>Difficulty</code> levels
     *                     for this <code>Sudoku</code> puzzle.
     */
    public Sudoku(int n, Difficulty... difficulties) {
        this.n = n;
        nn = n * n;
        dlxRows = new Row[nn][nn][nn];
        values = new int[nn][nn];
        if (difficulties != null && difficulties.length > 0) {
            outer:
            while (true) {
                reset();
                setup();
                solution = new Stack<Row>();
                generate();
                minimize();
                for (Difficulty diff : difficulties) {
                    if (diff.isInRange(size())) {
                        difficulty = diff;
                        break outer;
                    }
                }
            }
        } else {
            setup();
        }
    }

    private void setup() {
        for (int r = 0; r < nn; r++) {
            for (int c = 0; c < nn; c++) {
                values[r][c] = 0;
                for (int d = 1; d <= nn; d++) {
                    dlxRows[r][c][d - 1] = newRow(new StringBuilder().append(String.valueOf(r)).append(String.valueOf(c))
                            .append(String.valueOf(d)).toString());
                }
            }
        }
        List<Row> rowColumn = new Vector<Row>(nn);
        List<Row> digitRow = new Vector<Row>(nn);
        List<Row> digitColumn = new Vector<Row>(nn);
        List<Row> digitBlock = new Vector<Row>(nn);
        for (int a = 0; a < nn; a++) {
            for (int b = 0; b < nn; b++) {
                rowColumn.clear();
                digitRow.clear();
                digitColumn.clear();
                digitBlock.clear();
                for (int c = 0; c < nn; c++) {
                    rowColumn.add(dlxRows[a][b][c]);
                    digitRow.add(dlxRows[b][c][a]);
                    digitColumn.add(dlxRows[c][b][a]);
                    digitBlock.add(dlxRows[n * (b / n) + c / n][n * (b % n) + c % n][a]);
                }
                newColumn(MessageFormat.format(PATTERN_ROW_COLUMN, Integer.valueOf(a),
                                Integer.valueOf(b)),
                        rowColumn);
                newColumn(MessageFormat.format(PATTERN_DIGIT_ROW,
                                Integer.valueOf(a + 1),
                                Integer.valueOf(b)),
                        digitRow);
                newColumn(MessageFormat.format(PATTERN_DIGIT_COLUMN,
                                Integer.valueOf(a + 1),
                                Integer.valueOf(b)),
                        digitColumn);
                newColumn(MessageFormat.format(PATTERN_DIGIT_BLOCK,
                                Integer.valueOf(a + 1),
                                Integer.valueOf(b)),
                        digitBlock);
            }
        }
    }

    private void generate() {
        for (int i = 0; i < 2 * nn; i++) {
            Row row = (Row) getDown();
            while (true) {
                if (!row.equals(this) && RANDOM.nextInt(1000) == 0) {
                    pickValue(row);
                    if (countSolutions(1) == 0) {
                        unpickValue();
                    } else {
                        break;
                    }
                }
                row = (Row) row.getDown();
            }
        }
        Stack<Row> sol = getSolution();
        while (countSolutions(1) == 2) {
            try {
                Row row = sol.pop();
                if (RANDOM.nextInt(sol.size()) == 0) {
                    pickValue(row);
                } else {
                    sol.add(0, row);
                }
            } catch (IllegalStateException e) {
                // ignore
            }
            sol = getSolution();
            for (Row row : sol) {
                String name = row.getName();
                int i = name.charAt(0) - 48;
                int j = name.charAt(1) - 48;
                if (values[i][j] >= 0) {
                    values[i][j] = name.charAt(2) - 48;
                }
            }
        }
    }

    private void minimize() {
        outer:
        while (true) {
            for (int i = solution.size(); i > 0; i--) {
                Row row = unpickRow();
                if (countSolutions(1) == 2) {
                    solution.add(0, row);
                } else {
                    pickRow(row);
                    unpickValue();
                    continue outer;
                }
            }
            break;
        }
    }

    void pickValue(int row, int col, int digit) {
        pickRow(dlxRows[row][col][digit - 1]);
        values[row][col] = -digit;
    }

    private void pickValue(Row row) {
        String name = row.getName();
        pickValue(name.charAt(0) - 48, name.charAt(1) - 48, name.charAt(2) - 48);
    }

    void unpickValue() {
        Row row = unpickRow();
        int r = row.getName().charAt(0) - 48;
        int c = row.getName().charAt(1) - 48;
        values[r][c] = 0;
    }

    /**
     * This method returns a "classic" <code>String</code> representation of this
     * <code>Sudoku</code>.
     *
     * @return An n^2-by-n^2 table of this <code>Sudoku</code> puzzle.
     */
    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder("\n");
        for (int i = 0; i < nn; i++) {
            for (int j = 0; j < nn; j++) {
                if (values[i][j] >= 0) {
                    retVal.append(' ');
                }
                retVal.append(String.valueOf(values[i][j]));
                retVal.append(' ');
            }
            retVal.append('\n');
        }
        return retVal.toString();
    }

    int size() {
        return solution.size();
    }

    /**
     * This method retrieves the value of this <code>Sudoku</code> puzzle in the
     * <code>r</code>-th row and the <code>c</code>-th column.
     *
     * @param r The row index of the value to be retrieved
     * @param c The column index of the value to be retrieved
     * @return The value placed in the <code>r</code>-th row and the
     * <code>c</code>-th column
     */
    public int getValue(int r, int c) {
        return values[r][c];
    }

    /**
     * Getter method for this <code>Sudoku</code>'s <code>Difficulty</code> level.
     *
     * @return The <code>Difficulty</code> level of this <code>Sudoku</code>
     * puzzle
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

}
