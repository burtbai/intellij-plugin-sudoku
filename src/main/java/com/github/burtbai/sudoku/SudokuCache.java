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

import com.github.burtbai.sudoku.plugin.SudokuControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The <code>SudokuCache</code> class is used to asynchonously construct a
 * small number of sudoku puzzles of different difficulty levels. Its main
 * business logic runs in a separate <code>Thread</code> which fills up
 * a cache <code>Map</code> with sudokus such that a constant number of
 * puzzles of every defined difficulty is available for retrieval.
 */
public final class SudokuCache implements Runnable {

    private static final int CACHE_SIZE = 3;

    private Map<Sudoku.Difficulty, Queue<Sudoku>> cache = new EnumMap<Sudoku.Difficulty, Queue<Sudoku>>(Sudoku.Difficulty.class);
    private SudokuControl sudokuControl;

    /**
     * Public constructor.
     *
     * @param sudokuControl The <code>SudokuControl</code> associated with this
     *                      <code>SudokuCache</code> object. This <code>SudokuControl</code>
     *                      will be notified of additions of new sudokus (and may react to this
     *                      case by enabling the "New" button if necessary).
     */
    public SudokuCache(SudokuControl sudokuControl) {
        this.sudokuControl = sudokuControl;
        for (Sudoku.Difficulty difficulty : Sudoku.Difficulty.values()) {
            cache.put(difficulty, new ArrayBlockingQueue<Sudoku>(CACHE_SIZE));
        }
        new Thread(this, "SudokuCache").start();
    }

    /**
     * This method may be used to determine if (at least) one sudoku puzzle of
     * a given difficulty level is available in the cache.
     *
     * @param difficulty The difficulty level of the sudoku puzzle
     * @return <b>true</b> if at least one sudoku of the specified difficulty
     * level is available; <b>false</b> otherwise
     */
    public boolean isAvailable(Sudoku.Difficulty difficulty) {
        return !cache.get(difficulty).isEmpty();
    }

    /**
     * This method retrieves a sudoku puzzle of a given difficulty level from the
     * cache.
     *
     * @param difficulty The difficulty level of the sudoku puzzle to be
     *                   retrieved
     * @return A sudoku puzzle of the specified difficulty level
     */
    public Sudoku getSudoku(Sudoku.Difficulty difficulty) {
        Sudoku retVal = cache.get(difficulty).poll();
        System.out.println("Retrieving sudoku of difficulty " + difficulty + " from cache");
        return retVal;
    }

    /**
     * This method (which runs in its own thread) checks the state of the
     * internal cache <code>Map</code> and (if neceessary) constructs new sudokus
     * until a constant number of puzzles of each difficulty level is available
     * for retrieval.
     */
    public void run() {
        List<Sudoku.Difficulty> list = new ArrayList<Sudoku.Difficulty>(3);
        while (true) {
            list.clear();
            for (Sudoku.Difficulty difficulty : Sudoku.Difficulty.values()) {
                Queue<Sudoku> queue = cache.get(difficulty);
                if (queue.size() < CACHE_SIZE) {
                    list.add(difficulty);
                }
            }
            if (list.size() > 0) {
                Sudoku.Difficulty[] diffs = list.toArray(new Sudoku.Difficulty[list.size()]);
                StringBuilder msg = new StringBuilder("Searching for sudokus with difficulties ");
                System.out.println(msg.append(Arrays.toString(diffs)).toString());
                Sudoku sudoku = new Sudoku(3, diffs);
                Sudoku.Difficulty difficulty = sudoku.getDifficulty();
                cache.get(difficulty).offer(sudoku);
                msg = new StringBuilder("Adding sudoku of difficulty ");
                msg.append(difficulty).append(" to cache");
                System.out.println(msg.toString());
                this.sudokuControl.setSudokuAvailable(sudoku.getDifficulty());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
