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
 * The <code>Cell</code> interface represents any non-empty location
 * in a DLX Matrix.
 */
public interface Cell {

  /**
   * Getter method for the <code>Cell</code> above this one.
   *
   * @return The neighboring <code>Cell</code> above this <code>Cell</code>
   */
  public Cell getUp();

  /**
   * Setter method for the <code>Cell</code> above this one.
   *
   * @param up The neighboring <code>Cell</code> above this <code>Cell</code>
   */
  public void setUp(Cell up);

  /**
   * Getter method for the <code>Cell</code> below this one.
   *
   * @return The neighboring <code>Cell</code> below this <code>Cell</code>
   */
  public Cell getDown();

  /**
   * Setter method for the <code>Cell</code> below this one.
   *
   * @param down The neighboring <code>Cell</code> below this <code>Cell</code>
   */
  public void setDown(Cell down);

  /**
   * Getter method for the <code>Cell</code> to the left of this one.
   *
   * @return The neighboring <code>Cell</code> to the left of this
   *          <code>Cell</code>
   */
  public Cell getLeft();

  /**
   * Setter method for the <code>Cell</code> to the left of this one.
   *
   * @param left The neighboring <code>Cell</code> to the left of this
   *         <code>Cell</code>
   */
  public void setLeft(Cell left);

  /**
   * Getter method for the <code>Cell</code> to the right of this one.
   *
   * @return The neighboring <code>Cell</code> to the right of this
   *          <code>Cell</code>
   */
  public Cell getRight();

  /**
   * Setter method for the <code>Cell</code> to the right of this one.
   *
   * @param right The neighboring <code>Cell</code> to the right of this
   *         <code>Cell</code>
   */
  public void setRight(Cell right);

  /**
   * Getter method for the <code>Column</code> cell of this <code>Cell</code>.
   *
   * @return The <code>Column</code> cell of this one
   */
  public Column getColumn();

  /**
   * Getter method for the <code>Row</code> cell of this <code>Cell</code>.
   *
   * @return The <code>Row</code> cell of this one
   */
  public Row getRow();

  /**
   * This method covers the <code>column</code>s of all cells contained in the
   * same <code>Row</code> as this <code>Cell</code>.
   */
  public void coverRow();

  /**
   * This method uncovers the <code>column</code>s of all cells contained in the
   * same <code>Row</code> as this <code>Cell</code>.
   */
  public void uncoverRow();

}
