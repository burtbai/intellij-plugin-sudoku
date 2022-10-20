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
 * This class is the standard implementation of the <code>Row</code> interface.
 */
@SuppressWarnings({"EqualsAndHashcode"})
public class RowImpl extends BorderCellImpl implements Row {

  /**
   * Public constructor. Adds this <code>RowImpl</code> to the given DLX matrix.
   * Note that it creates a link from this <code>RowImpl</code> to the given
   * matrix, but not vice versa.
   *  
   * @param matrix The DLX <code>Matrix</code> to which this new
   *         <code>RowImpl</code> is added.
   */
  public RowImpl(Matrix matrix) {
    setRow(this);
    setColumn(matrix);
  }

  /**
   * This method appends a <code>Cell</code> to this <code>RowImpl</code>.
   *
   * @param cell The <code>Cell</code> to be added to this <code>RowImpl</code>
   */
  public void append(Cell cell) {
    insertLeft(cell);
  }

  /**
   * This method generates a <code>String</code> representation of this
   * <code>RowImpl</code>.
   *
   * @return A <code>String</code> representation of this <code>RowImpl</code>
   */
  @Override
  public String toString() {
    return "[Row " + getName() + ']';
  }

  /**
   * This method tests this <code>RowImpl</code> for equality with another
   * obejct.
   *
   * @param object The object against which this <code>RowImpl</code> is to be
   *         compared
   * @return <b>true</b> if the other object is an instance of
   *          <code>RowImpl</code> and has the same name as this one;
   *          <b>false</b> otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Row)) {
      return false;
    }
    Row that = (Row) object;
    String name = this.getName();
    return name != null ? name.equals(that.getName()) : that.getName() == null;
  }

}
