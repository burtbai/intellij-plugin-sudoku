# Sudoku

![Build](https://github.com/burtbai/sudoku/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/20195.svg)](https://plugins.jetbrains.com/plugin/20195)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/20195.svg)](https://plugins.jetbrains.com/plugin/20195)


<!-- Plugin description -->

An IntelliJ platform plugin for playing Sudoku.

## How to play

Go to **View -> Tools Windows -> Sudoku**

Numbers are entered by using the number keys on the keyboard;
Manually entered values may be deleted using the SPACE/DEL key.
Within the tabular grid, the arrow keys move the focus up, down, left or right.

<!-- Plugin description end -->


## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "sudoku"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/burtbai/sudoku/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## [What are the basic rules of Sudoku?](https://sudoku.com/sudoku-rules/)

- Sudoku grid consists of 9x9 spaces.
- You can use only numbers from 1 to 9.
- Each 3×3 block can only contain numbers from 1 to 9.
- Each vertical column can only contain numbers from 1 to 9.
- Each horizontal row can only contain numbers from 1 to 9.
- Each number in the 3×3 block, vertical column or horizontal row can be used only once.
- The game is over when the whole Sudoku grid is correctly filled with numbers.



---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
