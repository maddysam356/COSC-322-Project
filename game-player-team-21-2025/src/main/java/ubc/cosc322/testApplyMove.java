package ubc.cosc322;

import java.util.Arrays;

public class testApplyMove {
    public static void main(String[] args) {
        // Sample board: 10x10 empty, with a player queen at (3,3)
        int[][] board = new int[10][10];
        board[3][3] = 1; // Player 1's queen

        // Sample move: move queen from (3,3) to (5,5), shoot arrow at (2,2)
        int[] move = {3, 3, 5, 5, 2, 2};

        // Instantiate minimax and apply move
        minimax mm = new minimax();
        int[][] newBoard = mmTestApplyMove(mm, board, move, 1);

        // Print the result
        System.out.println("Original board:");
        printBoard(board);
        System.out.println("\nNew board after move:");
        printBoard(newBoard);
    }

    // Helper method to access applyMove (since it's private in original class)
    public static int[][] mmTestApplyMove(minimax mm, int[][] board, int[] move, int player) {
        try {
            java.lang.reflect.Method method = minimax.class.getDeclaredMethod("applyMove", int[][].class, int[].class, int.class);
            method.setAccessible(true);
            return (int[][]) method.invoke(mm, board, move, player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility to print a 2D board
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }
}
