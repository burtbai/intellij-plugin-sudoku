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
 * This class is the standard implementation of the <code>Cell</code> interface.
 */
public class CellImpl implements Cell {

  private Cell up;
  private Cell down;
  private Cell left;
  private Cell right;
  private Column column;
  private Row row;

  /**
   * Protected zero-argument constructor. Links this <code>CellImpl</code> with
   * itself in all directions.
   */
  protected CellImpl() {
    reset();
  }

  /**
   * Public constructor which adds this <code>CellImpl</code> to a given
   * <code>Column</code> and <code>Row</code>.
   *
   * @param column The <code>Column</code> at which to add this
   *         <code>CellImpl</code>
   * @param row The <code>Row</code> at which to add this <code>CellImpl</code>
   */
  public CellImpl(Column column, Row row) {
    this();
    this.column = column;
    this.row = row;
    row.incrementSize();
    column.incrementSize();
  }

  /**
   * This method resets this <code>CellImpl</code>'s pointers in all four
   * directions to itself.
   */
  protected void reset() {
    up = this;
    down = this;
    left = this;
    right = this;
  }

  /**
   * Getter method for the <code>CellImpl</code> above this one.
   *
   * @return The neighboring <code>CellImpl</code> above this <code>CellImpl</code>
   */
  public Cell getUp() {
    return up;
  }

  /**
   * Setter method for the <code>CellImpl</code> above this one.
   *
   * @param up The neighboring <code>CellImpl</code> above this <code>CellImpl</code>
   */
  public void setUp(Cell up) {
    this.up = up;
  }

  /**
   * Getter method for the <code>CellImpl</code> below this one.
   *
   * @return The neighboring <code>CellImpl</code> below this <code>CellImpl</code>
   */
  public Cell getDown() {
    return down;
  }

  /**
   * Setter method for the <code>CellImpl</code> below this one.
   *
   * @param down The neighboring <code>CellImpl</code> below this <code>CellImpl</code>
   */
  public void setDown(Cell down) {
    this.down = down;
  }

  /**
   * Getter method for the <code>CellImpl</code> to the left of this one.
   *
   * @return The neighboring <code>CellImpl</code> to the left of this
   *          <code>CellImpl</code>
   */
  public Cell getLeft() {
    return left;
  }

  /**
   * Setter method for the <code>CellImpl</code> to the left of this one.
   *
   * @param left The neighboring <code>CellImpl</code> to the left of this
   *         <code>CellImpl</code>
   */
  public void setLeft(Cell left) {
    this.left = left;
  }

  /**
   * Getter method for the <code>CellImpl</code> to the right of this one.
   *
   * @return The neighboring <code>CellImpl</code> to the right of this
   *          <code>CellImpl</code>
   */
  public Cell getRight() {
    return right;
  }

  /**
   * Setter method for the <code>CellImpl</code> to the right of this one.
   *
   * @param right The neighboring <code>CellImpl</code> to the right of this
   *         <code>CellImpl</code>
   */
  public void setRight(Cell right) {
    this.right = right;
  }

  /**
   * Getter method for the <code>Column</code> cell of this
   * <code>CellImpl</code>.
   *
   * @return The <code>Column</code> cell of this one
   */
  public Column getColumn() {
    return column;
  }

  /**
   * Setter method for the <code>Column</code> cell of this
   * <code>CellImpl</code>.
   *
   * @param column The <code>Column</code> cell of this one
   */
  protected void setColumn(Column column) {
    this.column = column;
  }

  /**
   * Getter method for the <code>Row</code> cell of this <code>CellImpl</code>.
   *
   * @return The <code>Row</code> cell of this one
   */
  public Row getRow() {
    return row;
  }

  /**
   * Setter method for the <code>Row</code> cell of this <code>CellImpl</code>.
   *
   * @param row The <code>Row</code> cell of this one
   */
  protected void setRow(Row row) {
    this.row = row;
  }

  /**
   * This method inserts a given <code>Cell</code> immediately above this
   * <code>CellImpl</code>.
   *
   * @param cell The <code>Cell</code> to be inserted above this
   *         <code>CellImpl</code>
   */
  protected void insertUp(Cell cell) {
    cell.setDown(this);
    cell.setUp(getUp());
    getUp().setDown(cell);
    setUp(cell);
  }

  /**
   * This method inserts a given <code>Cell</code> immediately to the left of
   * this <code>CellImpl</code>.
   *
   * @param cell The <code>Cell</code> to be inserted to the left of this
   *         <code>CellImpl</code>
   */
  protected void insertLeft(Cell cell) {
    cell.setRight(this);
    cell.setLeft(getLeft());
    getLeft().setRight(cell);
    setLeft(cell);
  }

  /**
   * This method covers the <code>column</code>s of all cells contained in the
   * same <code>Row</code> as this <code>CellImpl</code>.
   */
  @SuppressWarnings({"ObjectEquality"})
  public void coverRow() {
    Cell cell = getRight();
    while (cell != this) {
      cell.getColumn().cover();
      cell = cell.getRight();
    }
  }

  /**
   * This method uncovers the <code>column</code>s of all cells contained in the
   * same <code>Row</code> as this <code>CellImpl</code>.
   */
  @SuppressWarnings({"ObjectEquality"})
  public void uncoverRow() {
    Cell cell = getLeft();
    while (cell != this) {
      cell.getColumn().uncover();
      cell = cell.getLeft();
    }
  }

  /**
   * This method generates a descriptive <code>String</code> representation of
   * this <code>CellImpl</code>.
   *
   * @return A <code>String</code> representation of this <code>CellImpl</code>
   *          made up of the names of its row and column.
   */
  @Override
  public String toString() {
    StringBuilder retVal = new StringBuilder("[");
    retVal.append(getRow() != null ? getRow().getName() : "(row)").append('|');
    retVal.append(getColumn() != null ? getColumn().getName() : "(col)").append(']');
    return retVal.toString();
  }

}
