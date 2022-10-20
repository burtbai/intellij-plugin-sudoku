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

import junit.framework.TestCase;

import java.util.Arrays;

public class SudokuTest extends TestCase {
    public void testSudoku() throws Exception {
        Sudoku sudoku = new Sudoku();
        sudoku.pickValue(0, 4, 5);
        sudoku.pickValue(0, 5, 1);
        sudoku.pickValue(0, 7, 9);
        sudoku.pickValue(0, 8, 2);
        sudoku.pickValue(1, 0, 4);
        sudoku.pickValue(1, 1, 6);
        sudoku.pickValue(1, 7, 7);
        sudoku.pickValue(2, 1, 9);
        sudoku.pickValue(2, 2, 5);
        sudoku.pickValue(2, 8, 3);
        sudoku.pickValue(3, 0, 2);
        sudoku.pickValue(3, 4, 4);
        sudoku.pickValue(4, 1, 5);
        sudoku.pickValue(4, 3, 9);
        sudoku.pickValue(4, 4, 7);
        sudoku.pickValue(5, 3, 3);
        sudoku.pickValue(5, 4, 8);
        sudoku.pickValue(5, 5, 2);
        sudoku.pickValue(5, 8, 1);
        sudoku.pickValue(6, 1, 3);
        sudoku.pickValue(6, 2, 7);
        sudoku.pickValue(6, 5, 4);
        sudoku.pickValue(7, 0, 9);
        sudoku.pickValue(7, 6, 5);
        sudoku.pickValue(7, 7, 3);
        sudoku.pickValue(7, 8, 6);
        sudoku.pickValue(8, 0, 8);
        sudoku.pickValue(8, 1, 2);
        sudoku.pickValue(8, 3, 5);
        sudoku.pickValue(8, 4, 1);
        sudoku.pickValue(8, 6, 9);
        sudoku.pickValue(8, 7, 4);
        assertEquals(1, sudoku.countSolutions());
        System.out.println(sudoku);
    }

    @SuppressWarnings({"JUnitTestMethodWithNoAssertions"})
    public void testBlank() throws Exception {
        Sudoku sudoku = new Sudoku();
        long time = System.currentTimeMillis();
        sudoku.getSolution();
        System.out.println(System.currentTimeMillis() - time);
    }

    @SuppressWarnings({"JUnitTestMethodWithNoAssertions"})
    public void testGenerate() throws Exception {
        int m = 10;
        int[] sizes = new int[m];
        for (int i = 0; i < m; i++) {
            Sudoku sudoku = new Sudoku(Sudoku.Difficulty.MEDIUM);
            System.out.println(sudoku);
            sizes[i] = sudoku.size();
        }
        System.out.println(Arrays.toString(sizes));
    }

    public void testEquals() throws Exception {
        Sudoku s1 = new Sudoku(2);
        Sudoku s2 = new Sudoku(2);
        System.out.println("Preparing s1");
        s1.pickValue(0, 0, 1);
        s1.pickValue(0, 1, 2);
        s1.pickValue(0, 2, 3);
        s1.pickValue(0, 3, 4);
        s1.pickValue(1, 0, 3);
        s1.pickValue(1, 2, 1);
        System.out.println("Preparing s2");
        s2.pickValue(0, 0, 1);
        s2.pickValue(0, 1, 2);
        s2.pickValue(0, 2, 3);
        s2.pickValue(0, 3, 4);
        s2.pickValue(1, 0, 3);
        s2.pickValue(1, 2, 1);
        s2.pickValue(3, 3, 2);
        s2.unpickValue();
        assertEquals("Expected : \n" + s1.toMatrixString() + " but was \n" + s2.toMatrixString(), s1, s2);
        s2.pickValue(3, 3, 1);
        assertEquals(1, s2.countSolutions(0));
    }
}