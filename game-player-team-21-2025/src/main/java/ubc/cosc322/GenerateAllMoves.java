package ubc.cosc322;

import java.util.ArrayList;

public class GenerateAllMoves {

    public void generateAllMoves(ArrayList<Integer> state, int playerColor) {
        System.out.println("Generating all moves for player " );
        // Convert state to a 2D board
        int[][] boardState = new int[11][11];
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                boardState[i][j] = state.get(i * 11 + j);
            }
        }

        // For each queen belonging to playerColor, find all valid moves
        for (int row = 0; row <= 10; row++) {
            for (int col = 0; col <= 10; col++) {
                if (boardState[row][col] == playerColor) {
                    // Check all squares in the board for valid moves
                    for (int newRow = 0; newRow <= 10; newRow++) {
                        for (int newCol = 0; newCol <= 10; newCol++) {
                            if (isValidMove(boardState, row, col, newRow, newCol)) {
                                // Create a copy of boardState and move the queen
                                int[][] newBoard = copyBoard(boardState);
                                newBoard[newRow][newCol] = newBoard[row][col];
                                newBoard[row][col] = 0;

                                // Print this new board state
                                printBoard(newBoard);
                                System.out.println("--HEHE---");
                            }
                        }
                    }
                }
            }
        }
    }

    // Validates queen movement: vertical, horizontal, diagonal, unobstructed
    private static boolean isValidMove(int[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        if (oldRow == newRow && oldCol == newCol) return false;  // No movement
        if (!isValidPosition(newRow, newCol)) return false;      // Out of bounds
        if (board[newRow][newCol] != 0) return false;            // Occupied square

        // Determine the step direction
        int rowStep = Integer.compare(newRow, oldRow);
        int colStep = Integer.compare(newCol, oldCol);

        // Check if queen moves in a straight line (vertical / horizontal / diagonal)
        if (rowStep == 0 && colStep == 0) return false;

        // Traverse toward newRow, newCol and ensure no obstruction
        int r = oldRow + rowStep;
        int c = oldCol + colStep;
        while (r != newRow || c != newCol) {
            // Check if the current position is within bounds
            if (!isValidPosition(r, c)) return false;

            // Check if the path is obstructed
            if (board[r][c] != 0) return false;

            r += rowStep;
            c += colStep;
        }
        return true;
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row <= 10 && col >= 0 && col <= 10;
    }

    private static int[][] copyBoard(int[][] original) {
        int[][] copy = new int[11][11];
        for (int i = 0; i < 11; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 11);
        }
        return copy;
    }

    private static void printBoard(int[][] board) {
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}