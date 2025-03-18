package ubc.cosc322;

import java.util.List;

public class minimax {
    
    private static final int MAX_DEPTH = 10; // 
    private static final long TIME_LIMIT = 29000; // 29s

    private long startTime; 

    //find best move
    public Move findBestMove(int[][] board, int player) {
        Move bestMove = null;
        startTime = System.currentTimeMillis();

        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            Move currentBestMove = minimaxRoot(board, depth, player);

            if (System.currentTimeMillis() - startTime >= TIME_LIMIT) {
                System.out.println("Time limit reached. Using last completed depth: " + (depth - 1));
                break;
            }

            if (currentBestMove != null) {
                bestMove = currentBestMove; //temp best move
            }
        }
        return bestMove;
    }

    // minimax root -minimax for the root node (player turn)
     
    private Move minimaxRoot(int[][] board, int depth, int player) {
        List<Move> possibleMoves = Move.generateAllMoves(board, player);//need to make generateAllMoves method
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Move move : possibleMoves) {
            int[][] newBoard = applyMove(board, move, player); //create new board with new move
            int moveValue = minimax(newBoard, depth - 1, false, alpha, beta, player);// recursive call for every move -> move to alpha beta minimax
            //find the biggest move of the current biggest move and new move calculated
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
            //find max of alpha and best value
            alpha = Math.max(alpha, bestValue);
        }
        return bestMove;
    }

    
}

