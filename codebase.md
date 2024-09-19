# README.md

```md
# DynamicTicTac
```

# tictactoe/TicTacToeModel.java

```java
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

```

# tictactoe/TicTacToeGUI.java

```java
package tictactoe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TicTacToeGUI extends JFrame {
  private TicTacToeModel game;
  private JButton[][] buttons;
  private int gridSize;
  private int winCondition;

  public TicTacToeGUI(int size, int winCondition) {
    this.gridSize = size;
    this.winCondition = winCondition;
    game = new TicTacToeModel(gridSize, winCondition);
    buttons = new JButton[gridSize][gridSize];
    setTitle(gridSize + "x" + gridSize + " Tic Tac Toe");
    setSize(400, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(gridSize, gridSize));

    // Create buttons for the grid
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        buttons[row][col] = new JButton();
        buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 40));
        buttons[row][col].setFocusPainted(false);
        final int r = row;
        final int c = col;

        // Add action listener for button clicks
        buttons[row][col].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            makeMove(r, c);
          }
        });

        add(buttons[row][col]);
      }
    }

    // Add key listener to handle "Q" and "R" key presses
    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // Check for "Q" or "R"
        if (e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
          System.exit(0); // Close the game when "Q" is pressed
        } else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
          resetGame(); // Restart the game when "R" is pressed
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // Not used
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // Not used
      }
    });

    setFocusable(true); // Set the frame to be focusable so it can listen for key events
  }

  private void makeMove(int row, int col) {
    try {
      game.move(row, col);
      updateButton(row, col);
      if (game.isGameOver()) {
        showWinner();
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      JOptionPane.showMessageDialog(this, e.getMessage());
    }
  }

  private void updateButton(int row, int col) {
    buttons[row][col].setText(game.getMarkAt(row, col) == Player.X ? "X" : "O");
    buttons[row][col].setEnabled(false); // Disable the button after the move
  }

  private void showWinner() {
    Player winner = game.getWinner();
    String message;

    if (winner != null) {
      message = "Winner: " + winner;
    } else {
      message = "It's a draw!";
    }

    JOptionPane.showMessageDialog(this, message);

    // Disable all buttons when the game is over
    for (int row = 0; row < buttons.length; row++) {
      for (int col = 0; col < buttons[row].length; col++) {
        buttons[row][col].setEnabled(false);
      }
    }

    // Show custom dialog with Play Again, Resize Grid, and Quit options
    showEndGameDialog();
  }

  private void showEndGameDialog() {
    JDialog dialog = new JDialog(this, "Game Over", true);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 3, 10, 10));

    // Create "Play Again" button
    JButton playAgainButton = new JButton("Play Again");
    playAgainButton.setFont(new Font("Arial", Font.BOLD, 20)); // Larger font for Play Again
    playAgainButton.setPreferredSize(new Dimension(150, 60)); // Bigger size for Play Again
    playAgainButton.addActionListener(e -> {
      resetGame();
      dialog.dispose();
    });

    // Create "Resize Grid" button
    JButton resizeButton = new JButton("Resize Grid");
    resizeButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller font for Resize Grid
    resizeButton.addActionListener(e -> {
      resizeGrid();
      dialog.dispose();
    });

    // Create "Quit" button
    JButton quitButton = new JButton("Quit");
    quitButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller font for Quit
    quitButton.addActionListener(e -> System.exit(0));

    // Add buttons to the panel
    panel.add(playAgainButton);
    panel.add(resizeButton);
    panel.add(quitButton);

    dialog.add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this); // Center the dialog on the main window
    dialog.setVisible(true);
  }

  private void resetGame() {
    game = new TicTacToeModel(gridSize, winCondition); // Reset with the stored grid size and win condition
    for (int row = 0; row < buttons.length; row++) {
      for (int col = 0; col < buttons[row].length; col++) {
        buttons[row][col].setText("");
        buttons[row][col].setEnabled(true);
      }
    }
  }

  private void resizeGrid() {
    // Ask the user for a new grid size and win condition
    gridSize = Integer.parseInt(JOptionPane.showInputDialog("Enter new grid size (e.g., 3 for 3x3):"));
    winCondition = Integer.parseInt(JOptionPane.showInputDialog("Enter new win condition (e.g., 3 for 3 in a row):"));

    // Remove all old buttons
    getContentPane().removeAll();
    setLayout(new GridLayout(gridSize, gridSize));

    // Create a new game with the new grid size and win condition
    game = new TicTacToeModel(gridSize, winCondition);
    buttons = new JButton[gridSize][gridSize];

    // Create new buttons for the grid
    for (int row = 0; row < gridSize; row++) {
      for (int col = 0; col < gridSize; col++) {
        buttons[row][col] = new JButton();
        buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 40));
        buttons[row][col].setFocusPainted(false);
        final int r = row;
        final int c = col;

        // Add action listener for button clicks
        buttons[row][col].addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            makeMove(r, c);
          }
        });

        add(buttons[row][col]);
      }
    }

    // Revalidate the layout to apply the changes
    revalidate();
    repaint();
  }
}

```

# tictactoe/DynamicTicTacToe.java

```java
package tictactoe;
import javax.swing.*;

public class DynamicTicTacToe {
  public static void main(String[] args) {
    // Get grid size and win condition from the user
    int gridSize = Integer.parseInt(JOptionPane.showInputDialog("Enter grid size (e.g., 3 for 3x3):"));
    int winCondition = Integer.parseInt(JOptionPane.showInputDialog("Enter win condition (e.g., 3 for three in a row):"));

    // Launch the GUI
    SwingUtilities.invokeLater(() -> {
      TicTacToeGUI gui = new TicTacToeGUI(gridSize, winCondition);
      gui.setVisible(true);
    });
  }
}

```

