package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    public static List<int[]> generateAllMoves(int[][] board, int player) {
        List<int[]> moves = new ArrayList<>();

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == player) {
                    List<int[]> queenMoves = findMovesForQueen(board, r, c);

                    for (int[] move : queenMoves) {
                        int[][] tempBoard = makeMove(board, new int[]{r, c, move[0], move[1],0,0});
                        List<int[]> arrowMoves = findMovesForQueen(tempBoard, move[0], move[1]);

                        for (int[] arrow : arrowMoves) {
                            moves.add(new int[]{r, c, move[0], move[1], arrow[0], arrow[1]});
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
        newBoard[move[4]][move[5]] = -1; // Arrow shot
        return newBoard;
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 10);
        }
        return newBoard;
    }
}