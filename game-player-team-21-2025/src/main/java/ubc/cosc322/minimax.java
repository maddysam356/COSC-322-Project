package ubc.cosc322;

import java.util.List;

public class minimax {
    
    private static final int MAX_DEPTH = 10; //big number for late game 
    private static final long TIME_LIMIT = 20000; // 29s

    private long startTime; 

    //find best move
    public int[] findBestMove(int[][] board, int player) {
        int[] bestMove = null;
        startTime = System.currentTimeMillis();

        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            int[] currentBestMove = minimaxRoot(board, depth, player);

            if (System.currentTimeMillis() - startTime >= TIME_LIMIT) {
                System.out.println("time limit reached. using depth: " + (depth - 1)); //print the 
                break;
            }

            if (currentBestMove != null) {
                bestMove = currentBestMove; //temp best move
            }
        }
        return bestMove;
    }

    // minimax root -minimax for the root node (player turn)
     
    private int[] minimaxRoot(int[][] board, int depth, int player) {
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, player);//need to make generateAllMoves method
        int[] bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int[] move : possibleMoves) {
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
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, isMaximizing ? player : opponent);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, player);
                int eval = minimax(newBoard, depth - 1, false, alpha, beta, player);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Beta prune
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
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
    private int[][] applyMove(int[][] board, int[] move, int player) {
        // Create a deep copy of the board to simulate the move
        int[][] newBoard = deepCopyBoard(board);
    
        int fromRow = move[0];
        int fromCol = move[1];
        int toRow = move[2];
        int toCol = move[3];
        int arrowRow = move[4];
        int arrowCol = move[5];
    
        // Move the queen
        newBoard[toRow][toCol] = newBoard[fromRow][fromCol];
        newBoard[fromRow][fromCol] = 0;
    
        // Place the arrow
        newBoard[arrowRow][arrowCol] = 3; // 3 = arrow
    
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

