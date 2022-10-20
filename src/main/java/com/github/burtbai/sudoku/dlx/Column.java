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
 * The <code>Column</code> class represents a <code>BorderCell</code> located
 * at the top of a DLX matrix column.
 */
public interface Column extends BorderCell {

  /**
   * This method "covers" this <code>Column</code>.
   */
  public void cover();

  /**
   * This method "uncovers" this <code>Column</code>.
   */
  public void uncover();

}
