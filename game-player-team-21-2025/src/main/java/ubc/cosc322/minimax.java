package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class minimax {

    private static final int MAX_DEPTH = 10;
    private static final long TIME_LIMIT = 29000;
    
    private long startTime;

    public int[] findBestMove(int[][] board, int player) {
        int[] bestMove = null;
        startTime = System.currentTimeMillis();

        // Iterative deepening up to MAX_DEPTH
        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            int[] currentBestMove = minimaxRoot(board, depth, player);
            
            // Check time limit
            if (System.currentTimeMillis() - startTime >= TIME_LIMIT) {
                System.out.println("Time limit reached. using depth: " + (depth - 1));
                break;
            }
            
            if (currentBestMove != null) {
                bestMove = currentBestMove;
            }
        }
        
        return bestMove;
    }

    /**
     * The root call that selects the best move by iterating over all moves
     * for 'player' and using alpha-beta minimax.
     */
    private int[] minimaxRoot(int[][] board, int depth, int player) {
        // Generate all possible root moves
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, player);

        // Move ordering (sort descending by static evaluation of the resulting board)
        possibleMoves.sort((m1, m2) -> {
            int score1 = Heuristic.evaluateBoard(applyMove(board, m1, player), player);
            int score2 = Heuristic.evaluateBoard(applyMove(board, m2, player), player);
            return Integer.compare(score2, score1);
        });

        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Try each possible move
        for (int[] move : possibleMoves) {
            // Apply move
            int[][] newBoard = applyMove(board, move, player);

            // Evaluate resulting position with minimax
            // next player = (player==1)?2:1
            int value = minimax(newBoard, depth - 1, (player == 1 ? 2 : 1), alpha, beta, player);

            // Track best
            if (value > bestValue) {
                bestValue = value;
                bestMove = move; 
            }

            // Update alpha
            alpha = Math.max(alpha, bestValue);

            // Prune if possible
            if (alpha >= beta) {
                break;
            }

            // Check time
            if (isTimeUp()) {
                break;
            }
        }

        return bestMove;
    }

    /**
     * Standard alpha-beta minimax. 
     * @param currentPlayer — whose move it is in this call
     * @param aiPlayer      — the AI's color (1 or 2); used for evaluating boards 
     *                       from the AI's perspective only.
     */
    private int minimax(int[][] board, int depth, int currentPlayer, int alpha, int beta, int aiPlayer) {
        // Base conditions
        if (depth == 0 || isTimeUp()) {
            return Heuristic.evaluateBoard(board, aiPlayer);
        }

        int otherPlayer = (currentPlayer == 1) ? 2 : 1;

        // If it's the AI's turn => we maximize
        boolean isMaximizing = (currentPlayer == aiPlayer);

        // Generate all moves for the current player
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, currentPlayer);

        // If no moves exist, we might treat it as a losing position or do a 
        // direct evaluation. Up to you.
        if (possibleMoves.isEmpty()) {
            // This can be treated as a major penalty or direct check:
            return -999999; 
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, currentPlayer);
                int eval = minimax(newBoard, depth - 1, otherPlayer, alpha, beta, aiPlayer);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break; // alpha-beta cutoff
                }

                if (isTimeUp()) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, currentPlayer);
                int eval = minimax(newBoard, depth - 1, otherPlayer, alpha, beta, aiPlayer);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    break; // alpha-beta cutoff
                }

                if (isTimeUp()) {
                    break;
                }
            }
            return minEval;
        }
    }

    private boolean isTimeUp() {
        return (System.currentTimeMillis() - startTime) >= TIME_LIMIT;
    }

    private int[][] applyMove(int[][] board, int[] move, int player) {
        int[][] newBoard = deepCopyBoard(board);
        int fromRow = move[0], fromCol = move[1];
        int toRow = move[2], toCol = move[3];
        int arrowRow = move[4], arrowCol = move[5];

        // Move the queen
        newBoard[toRow][toCol] = newBoard[fromRow][fromCol];
        newBoard[fromRow][fromCol] = 0;

        // Place the arrow
        newBoard[arrowRow][arrowCol] = 3;

        return newBoard;
    }

    private int[][] deepCopyBoard(int[][] board) {
        int[][] copy = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 10);
        }
        return copy;
    }
}
