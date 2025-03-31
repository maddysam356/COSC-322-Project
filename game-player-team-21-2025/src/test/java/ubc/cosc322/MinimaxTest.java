package ubc.cosc322;

public class MinimaxTest {


    //Yes this test passes and returns a value consisting of the best moves.
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
        if(bestMove == null) {
            System.out.println("No move found.");
        } else {
            System.out.println("Best move for player 1: ");
            System.out.println("From: (" + bestMove[0] + "," + bestMove[1] + ")");
            System.out.println("To: ("   + bestMove[2] + "," + bestMove[3] + ")");
            System.out.println("Arrow: (" + bestMove[4] + "," + bestMove[5] + ")");
        }
    }
}