package tictactoe;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TicTacToeModel implements TicTacToe {
  private Player[][] grid;
  private Player curTurn;
  private int winCondition;
  private int gridSize;

  public TicTacToeModel(int size, int winCondition) {
    this.gridSize = size;
    this.winCondition = winCondition;
    grid = new Player[gridSize][gridSize];
    curTurn = Player.X; // Start with Player X
  }

  private boolean checkLine(Player[] line) {
    if (line[0] == null) return false;
    for (Player p : line) {
      if (p != line[0]) return false;
    }
    return true;
  }

  private boolean checkWin(int row, int col) {
    // Check row
    if (col <= gridSize - winCondition) {
      Player[] rowLine = Arrays.copyOfRange(grid[row], col, col + winCondition);
      if (checkLine(rowLine)) return true;
    }
    // Check column
    if (row <= gridSize - winCondition) {
      Player[] colLine = new Player[winCondition];
      for (int i = 0; i < winCondition; i++) {
        colLine[i] = grid[row + i][col];
      }
      if (checkLine(colLine)) return true;
    }
    // Check diagonals
    if (row <= gridSize - winCondition && col <= gridSize - winCondition) {
      Player[] diagLine1 = new Player[winCondition];
      for (int i = 0; i < winCondition; i++) {
        diagLine1[i] = grid[row + i][col + i];
      }
      if (checkLine(diagLine1)) return true;
    }
    if (row <= gridSize - winCondition && col >= winCondition - 1) {
      Player[] diagLine2 = new Player[winCondition];
      for (int i = 0; i < winCondition; i++) {
        diagLine2[i] = grid[row + i][col - i];
      }
      if (checkLine(diagLine2)) return true;
    }
    return false;
  }

  private boolean isBoardFull() {
    for (Player[] row : grid) {
      for (Player p : row) {
        if (p == null) {
          return false; // There's an empty space
        }
      }
    }
    return true; // No empty spaces left
  }

  @Override
  public void move(int r, int c) {
    if (r < 0 || r >= gridSize || c < 0 || c >= gridSize) {
      throw new IllegalArgumentException("Out of bounds: " + r + ", " + c);
    }
    if (grid[r][c] != null) {
      throw new IllegalArgumentException("Space invalid: " + r + ", " + c);
    }
    if (isGameOver()) {
      throw new IllegalStateException("Game over");
    }
    grid[r][c] = getTurn();
    changeTurn();
  }

  private void changeTurn() {
    curTurn = (curTurn == Player.X) ? Player.O : Player.X;
  }

  @Override
  public Player getTurn() {
    return curTurn;
  }

  @Override
  public boolean isGameOver() {
    return isBoardFull() || checkForWin();
  }
  private boolean checkForWin() {
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        if (grid[row][col] != null && checkWin(row, col)) {
          return true; // Found a winning condition
        }
      }
    }
    return false; // No win found
  }

  @Override
  public Player getWinner() {
    if (!isGameOver()) {
      return null; // Game is still ongoing
    }

    // Check which player has won, if any
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        if (grid[row][col] != null && checkWin(row, col)) {
          return grid[row][col]; // Return the winning player
        }
      }
    }
    return null; // If no player has won, it means it's a draw
  }

  @Override
  public Player[][] getBoard() {
    return grid;
  }

  @Override
  public Player getMarkAt(int r, int c) {
    if (r < 0 || r >= gridSize || c < 0 || c >= gridSize) {
      throw new IllegalArgumentException("Out of bounds: " + r + ", " + c);
    }
    return grid[r][c]; // Returns null if the position is empty
  }

  @Override
  public String toString() {
    return Arrays.stream(grid)
            .map(row -> " " + Arrays.stream(row)
                    .map(p -> p == null ? " " : p.toString())
                    .collect(Collectors.joining(" | ")))
            .collect(Collectors.joining("\n-----------\n"));
  }
}
