package ubc.cosc322;

import java.util.ArrayList;

public class TestGenerateAllMoves {
    public static void main(String[] args) {
        // Initialize the game state with the provided ArrayList
        ArrayList<Integer> state = new ArrayList<>();
        int[] initialState = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
        };

        for (int value : initialState) {
            state.add(value);
        }

        // Print the initial state
        System.out.println("Initial Game State:");
        printState(state);

        // Test the GenerateAllMoves class
        System.out.println("\nGenerating all moves for Black (playerColor = 1):");
        GenerateAllMoves generator = new GenerateAllMoves();
        generator.generateAllMoves(state, 1); // Test for black queens
    }

    // Helper method to print the 1D state as a 2D board
    private static void printState(ArrayList<Integer> state) {
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(state.get(i * 11 + j) + " ");
            }
            System.out.println();
        }
    }
}