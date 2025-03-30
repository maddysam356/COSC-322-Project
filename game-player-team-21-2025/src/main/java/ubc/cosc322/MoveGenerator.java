package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    //takes as input a 10 by 10 board and a player number (1 or 2)
    public static List<int[]> generateAllMoves(int[][] board, int player) {
        List<int[]> moves = new ArrayList<>();

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == player) {
                    // Step 1: find all queen moves from (r,c)
                    List<int[]> queenMoves = findMovesForQueen(board, r, c);
                    for (int[] qm : queenMoves) {
                        // Move the queen only (no arrow)
                        int[][] boardAfterQueen = moveQueenOnly(board, r, c, qm[0], qm[1]);

                        // Step 2: from the new position (qm[0], qm[1]),
                        // find all possible arrow placements
                        List<int[]> arrowMoves = findMovesForQueen(boardAfterQueen, qm[0], qm[1]);

                        // For each arrow target, record the complete move
                        for (int[] am : arrowMoves) {
                            moves.add(new int[] { r, c, qm[0], qm[1], am[0], am[1] });
                        }
                    }
                }
            }
        }
        return moves;
    }

    public static List<int[]> findMovesForQueen(int[][] board, int row, int col) {
        List<int[]> moves = new ArrayList<>();
        int[] directions = {-1, 0, 1};

        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr, c = col + dc;

                while (isValid(board, r, c)) {
                    moves.add(new int[]{r, c});
                    r += dr;
                    c += dc;
                }
            }
        }
        return moves;
    }

    public static boolean isValid(int[][] board, int r, int c) {
        return r >= 0 && r < 10 && c >= 0 && c < 10 && board[r][c] == 0;
    }

    public static int[][] makeMove(int[][] board, int[] move) {
        int[][] newBoard = copyBoard(board);
        newBoard[move[2]][move[3]] = newBoard[move[0]][move[1]];
        newBoard[move[0]][move[1]] = 0;
        newBoard[move[4]][move[5]] = 3; // Arrow shot
        return newBoard;
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 10);
        }
        return newBoard;
    }

    private static int[][] moveQueenOnly(int[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        int[][] newBoard = copyBoard(board);
        newBoard[newRow][newCol] = newBoard[oldRow][oldCol];
        newBoard[oldRow][oldCol] = 0;
        return newBoard;
    }
}