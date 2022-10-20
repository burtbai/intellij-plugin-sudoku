package com.github.burtbai.sudoku.dlx;

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

import java.util.Stack;
import java.util.Vector;

/**
 * The instances of the <code>SolutionCollector</code> class represent
 * <code>Vector</code>s specifically designed for collecting solutions to
 * {@link Matrix} problems.
 */
public class SolutionCollector extends Vector<Stack<Row>> {

  private int maxCount;

  /**
   * Public constructor.
   *
   * @param maxCount The maximum number of different {@link Matrix} solutions
   * that this <code>SolutionCollector</code> accepts.
   */
  public SolutionCollector(int maxCount) {
    this.maxCount = maxCount;
  }

  /**
   * Public constructor. Instances constructed with this constructor will
   * accept an unlimited number of {@link Matrix} solutions.
   */
  public SolutionCollector() {
    this(-1);
  }

  /**
   * This method will attempt to add a {@link Matrix} solution (represented by
   * a <code>Stack</code> of {@link Row} elements) to the vector. If the
   * maximum number of solutions is exceeded, a
   * {@link SolutionCountExceededException} is thrown.
   *
   * @param e A <code>Stack</code> of {@link Row} elements representing a
   *         solution to a {@link Matrix} problem
   * @return <code>true</code> always
   * @throws SolutionCountExceededException if the maximum number of allowed
   *          {@link Matrix} solutions is exceeded
   */
  @Override
  public synchronized boolean add(Stack<Row> e) {
    if (size() == maxCount) {
      throw new SolutionCountExceededException((Stack<Row>) e.clone());
    }
    return super.add((Stack<Row>) e.clone());
  }

}
