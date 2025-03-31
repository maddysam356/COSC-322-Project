package ubc.cosc322;

import java.util.List;

public class MoveGeneratorTest {

    public static void main(String[] args) {
        testIsValid();              //pass
        testFindMovesForQueen();    //pass
        testMakeMove();             //pass
        testGenerateAllMoves();     //pass
        testCopyBoard();            //pass
    }

    public static void testIsValid() {
        System.out.println("Running testIsValid...");
        int[][] board = new int[10][10];
        board[5][5] = 1; // occupy one cell to ensure it's invalid

        System.out.println(MoveGenerator.isValid(board, 2, 3) ? "PASS" : "FAIL: Cell (2,3) should be valid if empty");
        System.out.println(!MoveGenerator.isValid(board, 5, 5) ? "PASS" : "FAIL: Cell (5,5) should be invalid because it's occupied");
        System.out.println(!MoveGenerator.isValid(board, -1, 0) ? "PASS" : "FAIL: Negative row index should be invalid");
        System.out.println(!MoveGenerator.isValid(board, 10, 10) ? "PASS" : "FAIL: Out-of-bounds indices should be invalid");
    }

    public static void testFindMovesForQueen() {
        System.out.println("Running testFindMovesForQueen...");
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

        // Pick the location of one of the queens, e.g., (6, 0)
        int queenRow = 9;
        int queenCol = 3;

        // Call findMovesForQueen for the selected queen
        List<int[]> moves = MoveGenerator.findMovesForQueen(board, queenRow, queenCol);

        // Check if moves are generated
        System.out.println(!moves.isEmpty() ? "PASS" : "FAIL: There should be valid moves for the queen at (6,0).");

        // Print the moves for manual verification
        System.out.println("Moves for queen at (" + queenRow + "," + queenCol + "):");
        for (int[] move : moves) {
            System.out.println("Move to: (" + move[0] + "," + move[1] + ")");
        }

        // Print the total number of moves
        System.out.println("Total number of moves: " + moves.size());

        // Ensure no moves go out of bounds or into invalid cells
        boolean allValid = true;
        for (int[] move : moves) {
            if (move[0] < 0 || move[0] >= 10 || move[1] < 0 || move[1] >= 10 || board[move[0]][move[1]] != 0) {
                allValid = false;
                break;
            }
        }
        System.out.println(allValid ? "PASS" : "FAIL: Some moves are invalid or out of bounds.");
    }

    public static void testMakeMove() {
        System.out.println("Running testMakeMove...");
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

        // Apply the move {6,0,5,0,6,0}
        int[] move = {6, 0, 5, 0, 6, 0}; // Move queen from (6,0) to (5,0) and shoot arrow at (6,0)
        int[][] newBoard = MoveGenerator.makeMove(board, move);

        // Print the original board
        System.out.println("Original Board:");
        printBoard(board);

        // Print the new board
        System.out.println("New Board:");
        printBoard(newBoard);

        // Check that original board is unchanged
        System.out.println(board[6][0] == 1 ? "PASS" : "FAIL: Original board should remain unchanged at (6,0).");
        System.out.println(board[5][0] == 0 ? "PASS" : "FAIL: Original board should remain empty at (5,0).");

        // Check that new board has updated positions
        System.out.println(newBoard[6][0] == 3 ? "PASS" : "FAIL: Arrow should be marked at (6,0) in new board.");
        System.out.println(newBoard[5][0] == 1 ? "PASS" : "FAIL: Queen should be at (5,0) in new board.");
        System.out.println(newBoard[6][0] == 3 ? "PASS" : "FAIL: Arrow should be marked at (6,0) in new board.");
    }

    // Helper method to print the board
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public static void testGenerateAllMoves() {
        System.out.println("Running testGenerateAllMoves...");
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

        // Generate all moves for player 1
        List<int[]> moves = MoveGenerator.generateAllMoves(board, 1);

        // Check if moves are generated
        System.out.println(!moves.isEmpty() ? "PASS" : "FAIL: Should generate moves for player 1's queens.");

        // Print all moves for manual verification
        // System.out.println("Generated moves for player 1:");
        // for (int[] move : moves) {
        //     System.out.println("Move: [Start: (" + move[0] + "," + move[1] + "), End: (" + move[2] + "," + move[3] + "), Arrow: (" + move[4] + "," + move[5] + ")]");
        // }

        // Print the total number of moves
        System.out.println("Total number of moves for player 1: " + moves.size());

        // Ensure all moves are valid
        boolean allValid = true;
        for (int[] move : moves) {
            if (move.length != 6 || move[0] < 0 || move[0] >= 10 || move[1] < 0 || move[1] >= 10 ||
                move[2] < 0 || move[2] >= 10 || move[3] < 0 || move[3] >= 10 ||
                move[4] < 0 || move[4] >= 10 || move[5] < 0 || move[5] >= 10) {
                allValid = false;
                break;
            }
        }
        System.out.println(allValid ? "PASS" : "FAIL: Some moves are invalid or out of bounds.");
    }

    public static void testCopyBoard() {
        System.out.println("Running testCopyBoard...");
        int[][] board = new int[10][10];
        board[0][0] = 1;
        int[][] copy = MoveGenerator.makeMove(board, new int[]{0, 0, 1, 1, 2, 2}); 
        // makeMove internally calls copyBoard

        // Check original board remains unchanged
        System.out.println(board[0][0] == 1 ? "PASS" : "FAIL: Original board should remain unchanged.");
        System.out.println(board[1][1] == 0 ? "PASS" : "FAIL: Original board should remain empty at (1,1).");

        // Check copied board has new piece
        System.out.println(copy[1][1] == 1 ? "PASS" : "FAIL: Copied board should place queen at (1,1).");
        System.out.println(copy[2][2] == 3 ? "PASS" : "FAIL: Copied board should place arrow at (2,2).");
    }
}