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

/**
 * This <code>RuntimeException</code> class is used to mark an excess of
 * expected solutions to a DLX <code>Matrix</code> problem.
 */
public class SolutionCountExceededException extends RuntimeException {

  private final Stack<Row> solution;

  /**
   * Public constructor.
   *
   * @param solution The excess solution to the problem
   */
  public SolutionCountExceededException(Stack<Row> solution) {
    this.solution = solution;
  }

  /**
   * Getter method for the solution which exceeded the maximum allowed
   * number.
   *
   * @return This <code>SolutionCountExceededException</code> violating
   *          DLX <code>Matrix</code> solution
   */
  public Stack<Row> getSolution() {
    return solution;
  }

}
