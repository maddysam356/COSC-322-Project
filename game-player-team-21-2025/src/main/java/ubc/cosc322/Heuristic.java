package ubc.cosc322;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Heuristic {
    public static int evaluateBoard(int[][] board, int player) {
        int mobilityScore = calculateWeightedMobility(board, player);     // mobility
        int influenceScore = calculateInfluence(board, player);          // influence
        int territoryScore = calculateTerritoryControl(board, player);   // territory
        int opponentBlockingScore = calculateEnemyBlocking(board, player); // less enemy mobility = better
        int queenSafetyScore = calculateQueenSafety(board, player);      // penalty if queens are trapped

        // Adjusted weighting: slightly more emphasis on mobility, slightly less penalty for safety.
        return (int)(
              3.5 * mobilityScore      // was 3.0
            + 3.0 * territoryScore     // keep territory
            + 2.0 * influenceScore
            + 1.5 * opponentBlockingScore
            - 0.5 * queenSafetyScore   // was -1.0
        );
    }

    // Mobility difference between our queens and the opponent's queens.
    public static int calculateWeightedMobility(int[][] board, int player) {
        int playerMoves = 0;
        int opponentMoves = 0;
        int enemy = (player == 1) ? 2 : 1;

        // Loop through board to count moves for both sides.
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == player) {
                    // Add weighted mobility for our queens
                    playerMoves += weightMove(MoveGenerator.findMovesForQueen(board, i, j), i, j);
                } else if (board[i][j] == enemy) {
                    // Add weighted mobility for opponent queens
                    opponentMoves += weightMove(MoveGenerator.findMovesForQueen(board, i, j), i, j);
                }
            }
        }
        return playerMoves - opponentMoves;
    }

    // Give higher weight to center moves (row/col ~ 3..6), which are typically better positions.
    private static int weightMove(List<int[]> moves, int row, int col) {
        int weight = 0;
        for (int[] move : moves) {
            // In this fix, we do not give an extra bonus for center squares.
            // Every potential move just adds +1. This distributes queens more evenly.
            weight += 1;
        }
        return weight;
    }

    // Influence: tries to measure how many squares each side can project control onto.
    public static int calculateInfluence(int[][] board,int player) {
        int[][] influenceMap = new int[10][10];
        int enemy = (player == 1) ? 2 : 1;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == player) {
                    updateInfluence(board, influenceMap, i, j, 1);
                } else if (board[i][j] == enemy) {
                    updateInfluence(board, influenceMap, i, j, -1);
                }
            }
        }

        int influenceScore = 0;
        for (int[] row : influenceMap) {
            for (int cell : row) {
                influenceScore += cell;
            }
        }
        return influenceScore;
    }

    private static void updateInfluence(int[][] board, int[][] influenceMap, int row, int col, int value) {
        int[] directions = {-1, 0, 1}; // includes diagonal increments if adapted, but here we remain cardinal

        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr, c = col + dc;
                int influence = 3; // near queen => higher influence
                while (MoveGenerator.isValid(board, r, c)) {
                    influenceMap[r][c] += (value * influence);
                    influence--;
                    r += dr;
                    c += dc;
                }
            }
        }
    }

    // Territory: uses flood-fill to see how many empty spaces are near each side's queens.
    public static int calculateTerritoryControl(int[][] board, int player) {
        boolean[][] visited = new boolean[10][10];
        int playerScore = 0, opponentScore = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 0 && !visited[i][j]) {
                    int areaSize = floodFill(board, visited, i, j);
                    if (isNearPlayer(board, i, j, player)) {
                        playerScore += areaSize;
                    } else if (isNearEnemy(board, i, j, player)) {
                        opponentScore += areaSize;
                    }
                }
            }
        }
        return playerScore - opponentScore;
    }

    private static int floodFill(int[][] board, boolean[][] visited, int row, int col) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        visited[row][col] = true;
        int count = 0;

        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            count++;
            for (int d = 0; d < 4; d++) {
                int r = pos[0] + dr[d], c = pos[1] + dc[d];
                if (MoveGenerator.isValid(board, r, c) && !visited[r][c] && board[r][c] == 0) {
                    visited[r][c] = true;
                    queue.add(new int[]{r, c});
                }
            }
        }
        return count;
    }

    private static boolean isNearPlayer(int[][] board, int row, int col, int player) {
        return checkBoard(board, row, col, player);
    }

    private static boolean isNearEnemy(int[][] board, int row, int col, int player) {
        int enemy = (player == 1) ? 2 : 1;
        return checkBoard(board, row, col, enemy);
    }

    private static boolean checkBoard(int[][] board, int row, int col, int player) {
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        for (int d = 0; d < 4; d++) {
            int r = row + dr[d], c = col + dc[d];
            // Found a neighbor belonging to 'player'
            if (MoveGenerator.isValid(board, r, c) && board[r][c] == player) {
                return true;
            }
        }
        return false;
    }

    // Instead of comparing before/after moves (no difference in the same state),
    // we just reduce the score if the enemy currently has a high move count.
    public static int calculateEnemyBlocking(int[][] board, int player) {
        int enemy = (player == 1) ? 2 : 1;
        int enemyMoves = MoveGenerator.generateAllMoves(board, enemy).size();
        // The more moves the enemy has, the worse for us.
        // Return negative so fewer enemy moves => bigger final score.
        return -enemyMoves;
    }

    // Applies a penalty if any queens have <3 moves left (danger of being blocked).
    public static int calculateQueenSafety(int[][] board, int player) {
        int safetyPenalty = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == player) {
                    int moves = MoveGenerator.findMovesForQueen(board, i, j).size();
                    if (moves < 3) {
                        safetyPenalty += (4 - moves);
                    }
                }
            }
        }
        return safetyPenalty;
    }
}
