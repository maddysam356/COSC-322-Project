package ubc.cosc322;

import java.util.List;

public class minimax {
    
    private static final int MAX_DEPTH = 10; //big number for late game 
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

    //Minimax function with alpha-beta pruning.
    private int minimax(int[][] board, int depth, boolean isMaximizing, int alpha, int beta, int player) {
        if (depth == 0 || isTimeUp()) {//return best hurstic value with move
            return Heuristic.evaluateBoard(board); //temperory class in use
        }

        //check if it is our turn or not
        int opponent = (player == 1) ? 2 : 1;
        //calculate all possible moves
        List<Move> possibleMoves = Move.generateAllMoves(board, isMaximizing ? player : opponent);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, player);
                int eval = minimax(newBoard, depth - 1, false, alpha, beta, player);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Beta prune
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, opponent);
                int eval = minimax(newBoard, depth - 1, true, alpha, beta, player);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // Alpha prune
            }
            return minEval;
        }
    }

    //check for time
    private boolean isTimeUp() {
        return (System.currentTimeMillis() - startTime) >= TIME_LIMIT;
    }

    //applys move onto temp board
    //not complete - basic for time being need to cnosider which different queens
    private int[][] applyMove(int[][] board, Move move, int player) {
        int[][] newBoard = deepCopyBoard(board);//copy board
        newBoard[move.startRow][move.startCol] = 0;  // remove queen 
        newBoard[move.endRow][move.endCol] = player; // move queen to new position
        newBoard[move.arrowRow][move.arrowCol] = -1; // show arrow
        return newBoard;
    }

    //copy board
    //use of 10 X 10 instead of 11 that is passed in 
    private int[][] deepCopyBoard(int[][] board) {
        int[][] copy = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 10);
        }
        return copy;
    }
}

