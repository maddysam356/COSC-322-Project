package ubc.cosc322;

import java.util.Arrays;

public class MinimaxTest {

    public static void main(String[] args) {
        // Example 10x10 board:
        // 0 = empty, 1 = player 1 queen, 2 = player 2 queen, 3 = arrow
        // You can modify it to test different configurations
        int[][] board = {
            {0, 0, 0, 2, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 1, 0, 0, 0}
        };

        minimax minimaxPlayer = new minimax();
        // Evaluate best move for player 1
        int[] bestMove = minimaxPlayer.findBestMove(board, 1);

        // Print the chosen move
        if (bestMove == null) {
            System.out.println("No move found.");
        } else {
            System.out.println("Best move for player 1: ");
            System.out.println("From: (" + bestMove[0] + "," + bestMove[1] + ")");
            System.out.println("To: (" + bestMove[2] + "," + bestMove[3] + ")");
            System.out.println("Arrow: (" + bestMove[4] + "," + bestMove[5] + ")");

            // Apply the move to the board
            applyMove(board, bestMove);

            // Print the updated board
            System.out.println("Updated Board:");
            printBoard(board);
        }
    }

    // Method to apply the move to the board
    private static void applyMove(int[][] board, int[] move) {
        int fromRow = move[0];
        int fromCol = move[1];
        int toRow = move[2];
        int toCol = move[3];
        int arrowRow = move[4];
        int arrowCol = move[5];

        // Move the queen
        int player = board[fromRow][fromCol];
        board[fromRow][fromCol] = 0; // Clear the original position
        board[toRow][toCol] = player; // Place the queen in the new position

        // Place the arrow
        board[arrowRow][arrowCol] = 3; // 3 represents an arrow
    }

    // Method to print the board
    private static void printBoard(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }
}