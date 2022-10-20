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

/**
 * The <code>BorderCell</code> interface represents any "border" location
 * in a DLX Matrix, i.e. either a <code>Column</code> or <code>Row</code> cell.
 */
public interface BorderCell extends Cell {

  /**
   * Getter method for the number of (non-border) cells belonging to this
   * <code>BorderCell</code>.
   *
   * @return The number of non-border cells belonging to this
   *          <code>BorderCell</code>
   */
  public int getSize();

  /**
   * This method increments the number or non-border cells belonging to this
   * <code>BorderCell</code> by one.
   */
  public void incrementSize();

  /**
   * This method decrements the number or non-border cells belonging to this
   * <code>BorderCell</code> by one.
   */
  public void decrementSize();

  /**
   * Getter method for this <code>BorderCell</code>'s name.
   *
   * @return The name of this <code>BorderCell</code>
   */
  String getName();

  /**
   * Setter method for this <code>BorderCell</code>'s name.
   *
   * @param name The name of this <code>BorderCell</code>
   */
  void setName(String name);

  /**
   * This method appends a <code>Cell</code> to this <code>BorderCell</code>.
   *
   * @param cell The <code>Cell</code> to be added to this
   *         <code>BorderCell</code>
   */
  void append(Cell cell);

}
