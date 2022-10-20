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
 * This class is the standard implementation of the <code>BorderCell</code>
 * interface.
 */
public abstract class BorderCellImpl extends CellImpl implements BorderCell {

  private int size;
  private String name;

  /**
   * Getter method for the number of (non-border) cells belonging to this
   * <code>BorderCellImpl</code>.
   *
   * @return The number of non-border cells belonging to this
   *          <code>BorderCellImpl</code>
   */
  public int getSize() {
    return size;
  }

  /**
   * This method increments the number or non-border cells belonging to this
   * <code>BorderCellImpl</code> by one.
   */
  public void incrementSize() {
    size++;
  }

  /**
   * This method decrements the number or non-border cells belonging to this
   * <code>BorderCellImpl</code> by one.
   */
  public void decrementSize() {
    size--;
  }

  /**
   * Getter method for this <code>BorderCellImpl</code>'s name.
   *
   * @return The name of this <code>BorderCellImpl</code>
   */
  public String getName() {
    return name;
  }

  /**
   * Setter method for this <code>BorderCellImpl</code>'s name.
   *
   * @param name The name of this <code>BorderCellImpl</code>
   */
  public void setName(String name) {
    this.name = name;
  }
}
