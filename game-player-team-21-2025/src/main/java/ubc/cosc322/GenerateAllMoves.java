package ubc.cosc322;

import java.util.ArrayList;

public class GenerateAllMoves {

    /**
     * Generates all possible successor states for the given game state.
     * @param currentState The current game state represented as a TreeNode.
     * @param isWhiteTurn True if it's the white player's turn, false if it's the black player's turn.
     * @return An ArrayList of TreeNode objects representing all possible successor states.
     */
    public ArrayList<TreeNode> generateMoves(TreeNode currentState, boolean isWhiteTurn) {
        ArrayList<TreeNode> successors = new ArrayList<>();
        int[][] board = currentState.getBoardState(); // Get the current board state
        int player = isWhiteTurn ? 1 : 2; // 1 for white, 2 for black

        // Find all queens belonging to the current player
        ArrayList<int[]> playerQueens = findPlayerQueens(board, player);

        // For each queen, generate all possible moves
        for (int[] queen : playerQueens) {
            int queenRow = queen[0];
            int queenCol = queen[1];

            // Get all valid moves for the selected queen
            ArrayList<int[]> queenMoves = findValidMoves(board, queenRow, queenCol);

            // For each queen move, generate all possible arrow shots
            for (int[] queenMove : queenMoves) {
                int[][] tempBoard = copyBoard(board);
                tempBoard[queenRow][queenCol] = 0; // Remove the queen from the original position
                tempBoard[queenMove[0]][queenMove[1]] = player; // Place the queen in the new position

                // Get all valid arrow shots from the new queen position
                ArrayList<int[]> arrowShots = findValidMoves(tempBoard, queenMove[0], queenMove[1]);

                for (int[] arrowShot : arrowShots) {
                    int[][] newBoard = copyBoard(tempBoard);
                    newBoard[arrowShot[0]][arrowShot[1]] = 3; // Mark the arrow shot position

                    // Create a new TreeNode for the successor state
                    TreeNode successor = new TreeNode(newBoard, queenRow, queenCol, queenMove[0], queenMove[1], arrowShot[0], arrowShot[1]);
                    successors.add(successor);
                }
            }
        }

        return successors;
    }

    /**
     * Finds all queens belonging to the specified player on the board.
     * @param board The current board state.
     * @param player The player (1 for white, 2 for black).
     * @return A list of positions (row, col) of the player's queens.
     */
    private ArrayList<int[]> findPlayerQueens(int[][] board, int player) {
        ArrayList<int[]> queens = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == player) {
                    queens.add(new int[]{row, col});
                }
            }
        }
        return queens;
    }

    /**
     * Finds all valid moves for a piece at the given position.
     * @param board The current board state.
     * @param row The row of the piece.
     * @param col The column of the piece.
     * @return A list of valid moves (row, col) for the piece.
     */
    private ArrayList<int[]> findValidMoves(int[][] board, int row, int col) {
        ArrayList<int[]> validMoves = new ArrayList<>();
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Up, Down, Left, Right
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            while (isValidPosition(board, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
                newRow += direction[0];
                newCol += direction[1];
            }
        }

        return validMoves;
    }

    /**
     * Checks if a position is valid for a move.
     * @param board The current board state.
     * @param row The row to check.
     * @param col The column to check.
     * @return True if the position is valid, false otherwise.
     */
    private boolean isValidPosition(int[][] board, int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[row].length && board[row][col] == 0;
    }

    /**
     * Creates a deep copy of the board.
     * @param board The board to copy.
     * @return A new 2D array that is a copy of the original board.
     */
    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }
        return newBoard;
    }
}
