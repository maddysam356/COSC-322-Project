package ubc.cosc322;

import java.util.ArrayList;

public class GenerateAllMoves {

    public ArrayList<ArrayList<Integer>> generateAllMoves(ArrayList<Integer> state, int playerColor) {
        System.out.println("Generating all moves (queen + arrow) for player " + playerColor);

        // Convert the 1D state to a 2D board (11x11)
        int[][] boardState = new int[11][11];
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                boardState[i][j] = state.get(i * 11 + j);
            }
        }

        ArrayList<ArrayList<Integer>> allMoveStates = new ArrayList<>();

        // Locate all queens for the specified playerColor
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                if (boardState[row][col] == playerColor) {
                    // Directions for queen movement
                    int[] rowSteps = {0, 0, -1, 1, -1, 1, -1, 1};
                    int[] colSteps = {-1, 1, 0, 0, -1, -1, 1, 1};

                    // Generate possible queen moves
                    for (int dirIndex = 0; dirIndex < rowSteps.length; dirIndex++) {
                        int countDir = countMovesInDirection(boardState, row, col, rowSteps[dirIndex], colSteps[dirIndex]);
                        int r = row;
                        int c = col;
                        // Move the queen step by step in this direction
                        for (int step = 0; step < countDir; step++) {
                            r += rowSteps[dirIndex];
                            c += colSteps[dirIndex];

                            // Copy board and move queen
                            int[][] newBoardAfterQueen = copyBoard(boardState);
                            newBoardAfterQueen[r][c] = newBoardAfterQueen[row][col]; // move queen
                            newBoardAfterQueen[row][col] = 0;

                            // Now, from the queen's new position (r,c), generate arrow shots
                            generateArrowShotsFrom(newBoardAfterQueen, r, c, allMoveStates);
                        }
                    }
                }
            }
        }

        // Print all generated states
        int index = 1;
        for (ArrayList<Integer> s : allMoveStates) {
            System.out.println("State " + index + ":");
            printState(s);
            index++;
        }

        System.out.println("Total number of (queen + arrow) states generated: " + allMoveStates.size());
        return allMoveStates;
    }

    /**
     * For the queen at (row, col) in newBoardAfterQueen,
     * generate all valid arrow shots (represented as a 3 on the board),
     * and add the resulting board states to allMoveStates.
     */
    private void generateArrowShotsFrom(int[][] newBoardAfterQueen, int row, int col,
                                        ArrayList<ArrayList<Integer>> allMoveStates) {

        // Arrow movement uses the same steps as the queen
        int[] arrowRowSteps = {0, 0, -1, 1, -1, 1, -1, 1};
        int[] arrowColSteps = {-1, 1, 0, 0, -1, -1, 1, 1};

        for (int i = 0; i < arrowRowSteps.length; i++) {
            int countArrowDir = countMovesInDirection(newBoardAfterQueen, row, col, arrowRowSteps[i], arrowColSteps[i]);
            int r = row;
            int c = col;
            for (int step = 0; step < countArrowDir; step++) {
                r += arrowRowSteps[i];
                c += arrowColSteps[i];

                // Copy board for each arrow shot
                int[][] finalBoard = copyBoard(newBoardAfterQueen);
                finalBoard[r][c] = 3; // place arrow

                // Convert final board to state format and add it to the list
                ArrayList<Integer> newState = convertBoardToState(finalBoard);
                allMoveStates.add(newState);
            }
            // Reset r,c for next direction
            r = row;
            c = col;
        }
    }

    /**
     * Counts the number of consecutive, valid, and unoccupied squares
     * in a given direction (rowStep, colStep) starting from (row, col).
     */
    private int countMovesInDirection(int[][] board, int row, int col, int rowStep, int colStep) {
        int count = 0;
        int r = row + rowStep;
        int c = col + colStep;
        while (isValidPosition(r, c) && board[r][c] == 0) {
            count++;
            r += rowStep;
            c += colStep;
        }
        return count;
    }

    private static boolean isValidPosition(int row, int col) {
        return (row >= 1 && row <= 10) && (col >= 1 && col <= 10);
    }

    // Creates a deep copy of the 2D board
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    // Convert the 2D board back to a 1D state representation
    private ArrayList<Integer> convertBoardToState(int[][] board) {
        ArrayList<Integer> state = new ArrayList<>();
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                state.add(board[i][j]);
            }
        }
        return state;
    }

    // Print the 1D state as a 2D grid
    private void printState(ArrayList<Integer> state) {
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(state.get(i * 11 + j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}