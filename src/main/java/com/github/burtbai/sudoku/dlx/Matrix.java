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

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 * This class represents a DLX matrix. This construct is used to perform a
 * depth-first search for all solutions to an <b>exact cover problem</b>
 * as described in Donald E. Knuth's <em>Dancing Links</em> paper.
 *
 * @see <a href="http://xxx.lanl.gov/PS_cache/cs/pdf/0011/0011047.pdf">Donald
 *       E. Knuth's <em>Dancing Links</em> paper</a>
 */
@SuppressWarnings({"EqualsAndHashcode"})
public class Matrix extends ColumnImpl implements Row {

  /**
   * A (transient) solution to the problem represented by this
   * <code>Matrix</code>.
   */
  protected Stack<Row> solution = new Stack<Row>();

  @SuppressWarnings({"ObjectEquality"})
  private Column newColumn(Collection<Row> rows) {
    Column retVal = new ColumnImpl(this);
    Row row = (Row) getDown();
    while (row != this) {
      if (rows.contains(row)) {
        Cell cell = new CellImpl(retVal, row);
        retVal.append(cell);
        row.append(cell);
      }
      row = (Row) row.getDown();
    }
    this.insertLeft(retVal);
    return retVal;
  }

  /**
   * This method creates a new {@link Column} consisting of a
   * <code>Collection</code> of non-zero entries (marked by their respective
   * <code>Row</code>s) and adds it as the last column of this
   * <code>Matrix</code>.
   *
   * @param name The name of the newly created <code>Column</code>
   * @param rows A collection of <code>Row</code>s marking the non-zero
   *         entries of the newly created <code>Column</code>
   */
  protected void newColumn(String name, Collection<Row> rows) {
    Column retVal = newColumn(rows);
    retVal.setName(name);
  }

  private Column newColumn(Row... rows) {
    return newColumn(Arrays.asList(rows));
  }

  /**
   * This method creates a new {@link Column} consisting of an array of
   * non-zero entries (marked by their respective <code>Row</code>s) and adds it
   * as the last column of this <code>Matrix</code>.
   *
   * @param name The name of the newly created <code>Column</code>
   * @param rows An array of <code>Row</code>s marking the non-zero entries of
   *         the newly created <code>Column</code>
   * @return The newly created <code>Column</code>
   */
  public Column newColumn(String name, Row... rows) {
    Column retVal = newColumn(rows);
    retVal.setName(name);
    return retVal;
  }

  @SuppressWarnings({"ObjectEquality"})
  private Row newRow(Collection<Column> columns) {
    Row retVal = new RowImpl(this);
    Column col = (Column) getRight();
    while (col != this) {
      if (columns.contains(col)) {
        Cell cell = new CellImpl(col, retVal);
        retVal.append(cell);
        col.append(cell);
      }
      col = (Column) col.getRight();
    }
    this.insertUp(retVal);
    return retVal;
  }

  private Row newRow(Column... columns) {
    return newRow(Arrays.asList(columns));
  }

  /**
   * This method creates a new {@link Row} consisting of an array of
   * non-zero entries (marked by their respective <code>Column</code>s) and adds
   * it as the last row of this <code>Matrix</code>.
   *
   * @param name The name of the newly created <code>Column</code>
   * @param columns An array of <code>Column</code>s marking the non-zero
   *         entries of the newly created <code>Row</code>
   * @return The newly created <code>Row</code>
   */
  protected Row newRow(String name, Column... columns) {
    Row retVal = newRow(columns);
    retVal.setName(name);
    return retVal;
  }

  @SuppressWarnings({"ObjectEquality"})
  void collectSolutions(SolutionCollector collector) {
    if (this.getRight() == this) {
      collector.add(solution);
      return;
    }
    Column col = getPivotColumn();
    col.cover();
    Cell cell = col.getDown();
    try {
      while (cell != col) {
        solution.push(cell.getRow());
        cell.coverRow();
        try {
          collectSolutions(collector);
        }
        finally {
          cell.uncoverRow();
          solution.pop();
        }
        cell = cell.getDown();
      }
    }
    finally {
      col.uncover();
    }
  }

  @SuppressWarnings({"ObjectEquality"})
  private Column getPivotColumn() {
    Column retVal = null;
    Column col = (Column) getRight();
    while (col != this) {
      if (retVal == null || col.getSize() < retVal.getSize()) {
        retVal = col;
      }
      col = (Column) col.getRight();
    }
    return retVal;
  }

  /**
   * This method simply returns the result of the
   * {@link #toMatrixString()} method.
   *
   * @return A summary <code>String</code> of this DLX <code>Matrix</code>
   */
  @Override
  public String toString() {
    return toMatrixString();
  }

  /**
   * This method generates a tabular view of this <code>Matrix</code>.
   *
   * @return A tabular <code>String</code> representation of this
   *          <code>Matrix</code>
   */
  @SuppressWarnings({"ObjectEquality"})
  public String toMatrixString() {
    int rowWidth = 0;
    Row row = (Row) getDown();
    while (row != this) {
      String name = row.getName();
      if (name != null && name.length() > rowWidth) {
        rowWidth = name.length();
      }
      row = (Row) row.getDown();
    }
    StringBuilder header = new StringBuilder("%1$").append(String.valueOf(rowWidth)).append("s|");
    StringBuilder pattern = new StringBuilder("\n").append(header.toString());
    Column col = (Column) getRight();
    int ct = 2;
    while (col != this) {
      String name = col.getName();
      header.append(name != null ? name : " ").append('|');
      pattern.append('%').append(String.valueOf(ct++)).append('$');
      pattern.append(name != null ? String.valueOf(name.length()) : "1").append("s|");
      col = (Column) col.getRight();
    }
    StringBuilder retVal = new StringBuilder();
    Formatter out = new Formatter(retVal);
    out.format(header.toString(), "");
    row = (Row) getDown();
    while (row != this) {
      List<Object> args = new Vector<Object>();
      String name = row.getName();
      args.add(name != null ? name : "");
      Cell cell = row.getRight();
      col = (Column) getRight();
      while (col != this) {
        if (cell.getColumn() == col) {
          args.add("X");
          cell = cell.getRight();
        }
        else {
          args.add("");
        }
        col = (Column) col.getRight();
      }
      out.format(pattern.toString(), args.toArray());
      row = (Row) row.getDown();
    }
    return retVal.append('\n').toString();
  }

  /**
   * Unlike its parent implementation, this method does nothing.
   */
  @Override
  public void cover() {
  }

  /**
   * Unlike its parent implementation, this method does nothing.
   */
  @Override
  public void uncover() {
  }

  /**
   * This method counts the number of distinct solutions to the exact cover
   * problem represented by this <code>Matrix</code>.
   *
   * @param maxCount If this number is non-negative, the search for solutions
   *         will stop if <code>maxCount</code> is exceeded; if
   *         <code>maxCount</code> is negative, the search will continue until
   *         all solutions are found.
   * @return The actual number of solutions to the exact cover problem
   *          represented by this <code>Matrix</code> <b>unless</b>
   *          <ol>
   *            <li><code>maxCount</code> is non-negative and</li>
   *            <li>the actual number of solutions exceeds <code>maxCount</code>
   *          <ol><br/>
   *          (in which case <code>maxCount + 1</code> is returned)
   */
  public int countSolutions(int maxCount) {
    SolutionCollector collector = new SolutionCollector(maxCount);
    try {
      collectSolutions(collector);
      return collector.size();
    }
    catch (SolutionCountExceededException e) {
      return maxCount + 1;
    }
  }

  /**
   * This method counts the number of distinct solutions to the exact cover
   * problem represented by this <code>Matrix</code>.
   *
   * @return The exact number of distinct solutions to the exact cover
   *          problem represented by this <code>Matrix</code>
   */
  public int countSolutions() {
    return countSolutions(-1);
  }

  /**
   * This method retrieves the first solution of this <code>Matrix</code>
   * that it finds.
   *
   * @return The first solution found or <b>null</b> if no solution exists
   */
  @Nullable
  public Stack<Row> getSolution() {
    try {
      SolutionCollector collector = new SolutionCollector(0);
      collectSolutions(collector);
      return null;
    }
    catch (SolutionCountExceededException e) {
      return e.getSolution();
    }
  }

  /**
   * This method "picks" a <code>Row</code> of this <code>Matrix</code> for
   * possible inclusion in a solution.
   *
   * @param row The <code>Row</code> to be picked for inclusion in the
   *         solution
   */
  @SuppressWarnings({"ObjectEquality"})
  protected void pickRow(Row row) {
    Cell r = getDown();
    while (r != this) {
      if (r == row) {
        solution.push(row);
        row.coverRow();
        return;
      }
      r = r.getDown();
    }
    throw new IllegalStateException("Illegal row " + row);
  }

  /**
   * This method "unpicks" the most recently picked <code>Row</code> of this
   * <code>Matrix</code> from inclusion in a solution.
   *
   * @return The unpicked <code>Row</code>
   */
  protected Row unpickRow() {
    Row row = solution.pop();
    row.uncoverRow();
    return row;
  }

  /**
   * This method retrieves the number of currently "picked" rows.
   *
   * @return The number of rows currently "picked" for inclusion in the
   *          solution
   */
  public int getPickedRowCount() {
    return solution.size();
  }

  /**
   * This method determines if this <code>Matrix</code> equals another matrix.
   *
   * @param object An object representing the matrix to which this
   *         <code>Matrix</code> is compared for equality
   * @return <b>true</b> if this <code>Matrix</code> contains the same lists of
   *          <code>Row</code>s and <code>Column</code>s (in the same orders);
   *          <b>false</b> otherwise
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Matrix)) {
      return false;
    }
    Matrix that = (Matrix) object;
    Row row1 = (Row) this.getDown();
    Row row2 = (Row) that.getDown();
    while (row1 != this) {
      if (!row1.equals(row2)) {
        return false;
      }
      row1 = (Row) row1.getDown();
      row2 = (Row) row2.getDown();
    }
    if (row2 != that) {
      return false;
    }
    Column col1 = (Column) this.getRight();
    Column col2 = (Column) that.getRight();
    while (col1 != this) {
      if (!col1.equals(col2)) {
        return false;
      }
      col1 = (Column) col1.getRight();
      col2 = (Column) col2.getRight();
    }
    return col2 == that;
  }

}