package cs3500.tictactoe;
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
