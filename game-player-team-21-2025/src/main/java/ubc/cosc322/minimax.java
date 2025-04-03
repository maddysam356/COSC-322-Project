package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class minimax {
    private static final int MAX_DEPTH = 10;
    private static final long TIME_LIMIT = 29000;
    private static final int THREAD_COUNT = 8; // 8 threads

    private long startTime;

    public int[] findBestMove(int[][] board, int player) {
        int[] bestMove = null;
        startTime = System.currentTimeMillis();

        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            int[] currentBestMove = minimaxRoot(board, depth, player);
            if (System.currentTimeMillis() - startTime >= TIME_LIMIT) {
                System.out.println("time limit reached. using depth: " + (depth - 1));
                break;
            }
            if (currentBestMove != null) {
                bestMove = currentBestMove;
            }
        }
        return bestMove;
    }

    // Parallelized minimax root
    private int[] minimaxRoot(int[][] board, int depth, int player) {
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, player);

        // Sort moves by heuristic value (descending)
        possibleMoves.sort((m1, m2) -> {
            int score1 = Heuristic.evaluateBoard(applyMove(board, m1, player), player);
            int score2 = Heuristic.evaluateBoard(applyMove(board, m2, player), player);
            return Integer.compare(score2, score1);
        });

        // Use an ExecutorService with 8 threads
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = null;
        AtomicInteger alpha = new AtomicInteger(Integer.MIN_VALUE);
        int beta = Integer.MAX_VALUE;
        
        try {
            // Submit each move to be evaluated in parallel
            List<Future<int[]>> futures = new ArrayList<>();
            for (int[] move : possibleMoves) {
                futures.add(executor.submit(() -> {
                    int[][] newBoard = applyMove(board, move, player);
                    boolean isMaximizing = (player == 2);
                    int value = minimax(newBoard, depth - 1, !isMaximizing, alpha.get(), beta, player);
                    // Return [value, move_index = -1, ...actual move coords...]
                    int[] result = new int[move.length + 1];
                    result[0] = value; 
                    System.arraycopy(move, 0, result, 1, move.length);
                    return result;
                }));
            }

            // Collect results and find the best
            for (Future<int[]> f : futures) {
                int[] outcome = f.get();
                int value = outcome[0];
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = new int[outcome.length - 1];
                    System.arraycopy(outcome, 1, bestMove, 0, outcome.length - 1);
                }
                alpha.set(Math.max(alpha.get(), bestValue));
                if (alpha.get() >= beta) break;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int alpha, int beta, int player) {
        if (depth == 0 || isTimeUp()) {
            return Heuristic.evaluateBoard(board, player);
        }
        int opponent = (player == 1) ? 2 : 1;
        List<int[]> possibleMoves = MoveGenerator.generateAllMoves(board, isMaximizing ? player : opponent);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, player);
                int eval = minimax(newBoard, depth - 1, false, alpha, beta, player);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                int[][] newBoard = applyMove(board, move, opponent);
                int eval = minimax(newBoard, depth - 1, true, alpha, beta, player);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
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

        newBoard[toRow][toCol] = newBoard[fromRow][fromCol];
        newBoard[fromRow][fromCol] = 0;
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

