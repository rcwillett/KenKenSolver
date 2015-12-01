# KenKenSolver
This is my solution (in progress) for Challenge #240 on the https://www.reddit.com/r/dailyprogrammer/ page. 

Problem Description: KenKen are trademarked names for a style of arithmetic and logic puzzle invented in 2004 by Japanese math teacher Tetsuya Miyamoto, who intended the puzzles to be an instruction-free method of training the brain. KenKen now appears in more than 200 newspapers in the United States and worldwide. As in sudoku, the goal of each puzzle is to fill a grid with digits –– 1 through 4 for a 4x4 grid, 1 through 5 for a 5x5, etc. –– so that no digit appears more than once in any row or any column (a Latin square). Grids range in size from 3x3 to 9x9. Additionally, KenKen grids are divided into heavily outlined groups of cells –– often called “cages” –– and the numbers in the cells of each cage must produce a certain “target” number when combined using a specified mathematical operation (either addition, subtraction, multiplication or division). For example, a linear three-cell cage specifying addition and a target number of 6 in a 4x4 puzzle must be satisfied with the digits 1, 2, and 3. Digits may be repeated within a cage, as long as they are not in the same row or column. No operation is relevant for a single-cell cage: placing the "target" in the cell is the only possibility (thus being a "free space"). The target number and operation appear in the upper left-hand corner of the cage. Because we can't use the same layout that a printed KenKen board does, we will have to express the board in a lengthier fashion. The board layout will be given as row and column, with rows as numbers and columns as letters. A 6x6 board, therefore, looks like this:

    A B C D E F G 
  1 . . . . . . .  
  2 . . . . . . . 
  3 . . . . . . . 
  4 . . . . . . .  
  5 . . . . . . . 
  6 . . . . . . . 

Cages will be described as the target value, the operator to use, and then the cells to include. For example, if the upper left corner's cage covered A1 and A2 and should combine using the addition operator to a sum of 11, we would write: 11 + A1 A2 We will use standard ASCII notation for mathematical operators: +, -, /, *, and =. The equals sign basically says "this square is this value" - a gimme.
