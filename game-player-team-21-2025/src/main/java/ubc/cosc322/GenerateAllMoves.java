package ubc.cosc322;

import java.util.ArrayList;

public class GenerateAllMoves {

    public void generateAllMoves(ArrayList<Integer> state, int playerColor) {
        System.out.println("Generating all moves for player " + playerColor);

        // Convert state (1D ArrayList) to a 2D board (11x11)
        int[][] boardState = new int[11][11];
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                boardState[i][j] = state.get(i * 11 + j);
            }
        }

        int counter = 0; // Track the number of valid moves (board states) printed

        // For each square, check if it has a queen of the specified playerColor
        for (int row = 0; row <= 10; row++) {
            for (int col = 0; col <= 10; col++) {
                // Only move the queen if it matches the given playerColor
                if (boardState[row][col] == playerColor) {
                    // Check every possible square to see if it's a valid new position
                    for (int newRow = 0; newRow <= 10; newRow++) {
                        for (int newCol = 0; newCol <= 10; newCol++) {
                            if (isValidMove(boardState, row, col, newRow, newCol)) {
                                // Copy the board and move the queen to newRow, newCol
                                int[][] newBoard = copyBoard(boardState);
                                newBoard[newRow][newCol] = newBoard[row][col];
                                newBoard[row][col] = 0;

                                // Print the resulting board state
                                printBoard(newBoard);
                                System.out.println("-----");
                                counter++;
                            }
                        }
                    }
                }
            }
        }

        // Print the total number of valid moves found
        System.out.println("Total number of states printed: " + counter);
    }

    // Validates queen movement: vertical, horizontal, diagonal, unobstructed
    private static boolean isValidMove(int[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        // 1) No "move" if the old and new positions are the same
        if (oldRow == newRow && oldCol == newCol) return false;
        // 2) Position must be within the valid board area (excluding first column & last row)
        if (!isValidPosition(newRow, newCol)) return false;
        // 3) Destination square must be empty
        if (board[newRow][newCol] != 0) return false;

        // Determine row and column step to check for straight-line movement
        int rowStep = Integer.compare(newRow, oldRow);
        int colStep = Integer.compare(newCol, oldCol);

        // Must move either vertically, horizontally, or diagonally
        if (rowStep == 0 && colStep == 0) return false;

        // Traverse from (oldRow, oldCol) to (newRow, newCol) to check for obstructions
        int r = oldRow + rowStep;
        int c = oldCol + colStep;
        while (r != newRow || c != newCol) {
            if (!isValidPosition(r, c)) return false;
            if (board[r][c] != 0) return false;
            r += rowStep;
            c += colStep;
        }
        return true;
    }

    /**
     * Restricts valid positions so that col=0 and row=10 are considered "out of bounds."
     * Adjust the range as needed if your board representation differs.
     */
    private static boolean isValidPosition(int row, int col) {
        // row must be between 0 and 9, col must be between 1 and 10
        return (row >= 0 && row < 10) && (col > 0 && col <= 10);
    }

    private static int[][] copyBoard(int[][] original) {
        int[][] copy = new int[11][11];
        for (int i = 0; i < 11; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 11);
        }
        return copy;
    }

    private static void printBoard(int[][] board) {
        // Print rows from top (10) down to 0 for a more standard view
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}