package ubc.cosc322;

import java.util.ArrayList;

public class GenerateAllMoves {

    public ArrayList<ArrayList<Integer>> generateAllMoves(ArrayList<Integer> state, int playerColor) {
        System.out.println("Generating all moves for player " + playerColor);

        // Convert 1D state to a 2D board (11x11)
        int[][] boardState = new int[11][11];
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                boardState[i][j] = state.get(i * 11 + j);
            }
        }

        ArrayList<ArrayList<Integer>> allMoveStates = new ArrayList<>();
        int totalMoves = 0;

        // Locate all queens for the specified playerColor
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                if (boardState[row][col] == playerColor) {

                    // Count & generate moves in each direction
                    int[] rowSteps = {0, 0, -1, 1, -1, 1, -1, 1};
                    int[] colSteps = {-1, 1, 0, 0, -1, -1, 1, 1};
                    String[] directions = {
                        "Left", "Right", "Up", "Down",
                        "Left-Up", "Left-Down", "Right-Up", "Right-Down"
                    };

                    System.out.println("Queen at (" + row + ", " + col + "):");

                    int queenMoves = 0;
                    for (int iDir = 0; iDir < rowSteps.length; iDir++) {
                        int countDir = countMovesInDirection(boardState, row, col, rowSteps[iDir], colSteps[iDir]);
                        System.out.println("  " + directions[iDir] + ": " + countDir);

                        // Generate new states for each valid step in this direction
                        int r = row;
                        int c = col;
                        for (int step = 0; step < countDir; step++) {
                            r += rowSteps[iDir];
                            c += colSteps[iDir];

                            // Copy board and move queen
                            int[][] newBoard = copyBoard(boardState);
                            newBoard[r][c] = newBoard[row][col];
                            newBoard[row][col] = 0;

                            // Convert newBoard back to ArrayList<Integer> format
                            ArrayList<Integer> newState = convertBoardToState(newBoard);
                            allMoveStates.add(newState);
                        }
                        queenMoves += countDir;
                    }

                    System.out.println("  Total moves for this queen: " + queenMoves);
                    totalMoves += queenMoves;
                }
            }
        }

        // Print all generated states
        int index = 1;
        for (ArrayList<Integer> newState : allMoveStates) {
            System.out.println("State " + index + ":");
            printState(newState);
            index++;
        }

        System.out.println("Total number of states generated: " + allMoveStates.size());
        System.out.println("Total number of moves for all queens: " + totalMoves);

        return allMoveStates;
    }

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