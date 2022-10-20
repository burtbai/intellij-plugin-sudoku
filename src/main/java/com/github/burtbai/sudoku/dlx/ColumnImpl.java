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
 * This class is the standard implementation of the <code>Column</code>
 * interface.
 */
@SuppressWarnings({"EqualsAndHashcode"})
public class ColumnImpl extends BorderCellImpl implements Column {

  /**
   * This protected constructor creates an "unlinked" instance of the
   * <code>ColumnImpl</code> class.
   */
  protected ColumnImpl() {
    setColumn(this);
  }

  /**
   * Public constructor. Sets a link from this <code>ColumnImpl</code> to the
   * given <code>Matrix</code>, but not vice versa.
   *
   * @param matrix The DLX <code>Matrix</code> to which this new
   *         <code>ColumnImpl</code> is added.
   */
  public ColumnImpl(Matrix matrix) {
    this();
    setRow(matrix);
  }

  /**
   * This method appends a <code>Cell</code> to this <code>ColumnImpl</code>.
   *
   * @param cell The <code>Cell</code> to be added to this
   *         <code>ColumnImpl</code>
   */
  public void append(Cell cell) {
    insertUp(cell);
  }

  /**
   * This method "covers" this <code>ColumnImpl</code>.
   */
  @SuppressWarnings({"ObjectEquality"})
  public void cover() {
    getLeft().setRight(getRight());
    getRight().setLeft(getLeft());
    Cell cell = getDown();
    while (cell != this) {
      Cell cell2 = cell.getRight();
      while (cell2 != cell) {
        cell2.getDown().setUp(cell2.getUp());
        cell2.getUp().setDown(cell2.getDown());
        cell2.getColumn().decrementSize();
        cell2 = cell2.getRight();
      }
      cell = cell.getDown();
    }
  }

  /**
   * This method "uncovers" this <code>ColumnImpl</code>.
   */
  @SuppressWarnings({"ObjectEquality"})
  public void uncover() {
    Cell cell = getUp();
    while (cell != this) {
      Cell cell2 = cell.getLeft();
      while (cell2 != cell) {
        cell2.getColumn().incrementSize();
        cell2.getDown().setUp(cell2);
        cell2.getUp().setDown(cell2);
        cell2 = cell2.getLeft();
      }
      cell = cell.getUp();
    }
    getRight().setLeft(this);
    getLeft().setRight(this);
  }

  /**
   * This method generates a <code>String</code> representation of this
   * <code>ColumnImpl</code>.
   *
   * @return A <code>String</code> representation of this
   *          <code>ColumnImpl</code>
   */
  @Override
  public String toString() {
    return "[Col " + getName() + ']';
  }

  /**
   * This method tests this <code>ColumnImpl</code> for equality with another
   * obejct.
   *
   * @param object The object against which this <code>ColumnImpl</code> is to
   *         be compared
   * @return <b>true</b> if the other object is an instance of
   *          <code>ColumnImpl</code> and has the same name as this one;
   *          <b>false</b> otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Column)) {
      return false;
    }
    Column that = (Column) object;
    if (!this.getName().equals(that.getName())) {
      return false;
    }
    Cell cell1 = this.getDown();
    Cell cell2 = that.getDown();
    while (cell1 != this) {
      if (!cell1.getRow().equals(cell2.getRow())) {
        return false;
      }
      cell1 = cell1.getDown();
      cell2 = cell2.getDown();
    }
    return cell2 == that;
  }

}
