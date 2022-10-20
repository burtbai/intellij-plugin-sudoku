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

import junit.framework.TestCase;

@SuppressWarnings({"JavaDoc"})
public class MatrixTest extends TestCase {

  private static Matrix getMatrix1() {
    Matrix matrix = new Matrix();
    Row r1 = matrix.newRow("r1");
    Row r2 = matrix.newRow("r2");
    Row r3 = matrix.newRow("r3");
    matrix.newColumn("c1", r2, r3);
    matrix.newColumn("c2", r3, r1);
    matrix.newColumn("c3", r1, r2);
    return matrix;
  }

  private static Matrix getMatrix2() {
    Matrix matrix2 = new Matrix();
    Row r1 = matrix2.newRow("r1");
    Column c1 = matrix2.newColumn("c1");
    Row r2 = matrix2.newRow("r2", c1);
    Column c2 = matrix2.newColumn("c2", r1);
    matrix2.newRow("r3", c1, c2);
    matrix2.newColumn("c3", r1, r2);
    return matrix2;
  }

  private static Matrix getKnuthMatrix() {
    Matrix retVal = new Matrix();
    Row r1 = retVal.newRow("r1");
    Row r2 = retVal.newRow("r2");
    Row r3 = retVal.newRow("r3");
    Row r4 = retVal.newRow("r4");
    Row r5 = retVal.newRow("r5");
    Row r6 = retVal.newRow("r6");
    retVal.newColumn("A", r2, r4);
    retVal.newColumn("B", r3, r5);
    retVal.newColumn("C", r1, r3);
    retVal.newColumn("D", r2, r4, r6);
    retVal.newColumn("E", r1, r6);
    retVal.newColumn("F", r1, r3);
    retVal.newColumn("G", r2, r5, r6);
    return retVal;
  }

  @SuppressWarnings({"OverlyStrongTypeCast"})
  public void testMatrix() throws Exception {

    Matrix matrix = getMatrix1();
    String str1 = matrix.toString();
    assertEquals(0, matrix.countSolutions());

    Matrix matrix2 = getMatrix2();
    String str2 = matrix2.toString();

    assertEquals(str1, str2);
    assertEquals(matrix, matrix2);
    System.out.println(str1);

    SolutionCollector collector = new SolutionCollector();
    matrix2.collectSolutions(collector);
    assertEquals(0, collector.size());

    Cell m2c2 = matrix2.getRight();
    assertTrue(m2c2 instanceof Column);
    Column col = ((Column) m2c2.getRight());
    col.cover();
    String str3 = matrix2.toString();

    Matrix matrix3 = new Matrix();
    Row r2 = matrix3.newRow("r2");
    matrix3.newColumn("c1", r2);
    matrix3.newColumn("c3", r2);
    String str5 = matrix3.toString();

    assertEquals(str3, str5);
    assertEquals(matrix2, matrix3);

    Cell m2r1 = matrix2.getDown();
    assertTrue(m2r1 instanceof Row);
    assertEquals(2, ((Row) m2r1).getSize());

    Cell m2c1 = matrix2.getLeft();
    assertTrue(m2c1 instanceof Column);
    assertEquals(1, ((Column) m2c1).getSize());

    assertEquals(1, ((Column) m2c2).getSize());
    assertSame(matrix2, m2r1.getDown());
    assertSame(matrix2, m2c1.getLeft().getLeft());

    col.uncover();
    String str4 = matrix2.toString();
    assertEquals(str1, str4);
    assertEquals(matrix, matrix2);

  }

  public void testKnuth() throws Exception {

    Matrix matrix = getKnuthMatrix();
    SolutionCollector collector = new SolutionCollector();
    matrix.collectSolutions(collector);
    assertEquals(1, collector.size());
    System.out.println(matrix.toString());

    Stack<Row> solution = collector.get(0);
    System.out.println(solution.toString());
    Row row = (Row) matrix.getDown();
    matrix.pickRow(row);
    assertNotSame(row, matrix.getDown());
    System.out.println(matrix.toString());

    collector.clear();
    matrix.collectSolutions(collector);
    assertEquals(1, collector.size());
    System.out.println(matrix.toString());

    solution = collector.get(0);
    assertEquals(1, matrix.getPickedRowCount());
    System.out.println(solution.toString());
    System.out.println(matrix.toString());

    Row row2 = matrix.unpickRow();
    assertEquals(row, row2);
    assertEquals(1, matrix.countSolutions());
    assertEquals(1, matrix.countSolutions(0));
    System.out.println(matrix.toString());

    matrix.pickRow((Row) row.getDown());
    assertEquals(0, matrix.countSolutions());
    System.out.println(matrix.toString());

    matrix.unpickRow();
    System.out.println(matrix.toString());
    assertEquals(1, matrix.countSolutions());

  }

  public void testEquals() throws Exception {
    Matrix matrix1 = getMatrix1();
    Matrix matrix2 = getMatrix1();

    Row row = (Row) matrix1.getDown();
    row.coverRow();
    assertFalse(matrix1.equals(matrix2));
    row.uncoverRow();
    assertEquals(matrix1, matrix2);

  }

  public void testIllegalRows() throws Exception {
    Matrix m = getKnuthMatrix();
    Row r2 = (Row) m.getDown().getDown();
    Row r4 = (Row) r2.getDown().getDown();
    m.pickRow(r2);
    try {
      m.pickRow(r4);
      fail("IllegalStateException expected.");
    }
    catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

}