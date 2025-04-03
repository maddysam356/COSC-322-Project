package ubc.cosc322;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Heuristic {

    /**
     * Master board evaluation: tries normal heuristics unless we detect an endgame.
     */
    public static int evaluateBoard(int[][] board, int player) {
        // 1) Check if we are in an endgame scenario:
        // e.g., fewer than 15 empty squares
        int emptyCount = countEmptySquares(board);
        if (emptyCount < 40) {
            // 2) If "endgame," do territory-based scoring
            int endgameScore = calculateEndgameTerritory(board, player);
            if (endgameScore != 0) {
                return endgameScore;
            } else {
                // If territory is balanced, nudge advantage to the side that moves next
                return (player == 1) ? 1 : -1;
            }
        }

        // 3) Otherwise, use normal midgame scoring logic.
        int mobilityScore = calculateWeightedMobility(board, player);
        int influenceScore = calculateInfluence(board, player);
        int territoryScore = calculateTerritoryControl(board, player);
        int opponentBlockingScore = calculateEnemyBlocking(board, player);
        int queenSafetyScore = calculateQueenSafety(board, player);

        // Existing "contest" logic
        int contestScore = calculateContestAreas(board, player);

        // Penalize clustering
        int clusteringPenalty = calculateClusteringPenalty(board, player);

        // Weighted sum
        return (int)(
              3.5 * mobilityScore
            + 3.0 * territoryScore
            + 2.0 * influenceScore
            + 2.5 * opponentBlockingScore
            - 0.5 * queenSafetyScore
            + 2.0 * contestScore
            - 1.0 * clusteringPenalty
        );
    }

    /**
     * Simple “endgame” check: if empty squares < threshold, switch to territory-based scoring.
     */
    private static int countEmptySquares(int[][] board) {
        int count = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Basic territory-based scoring for endgame:
     *  - For each connected region of empty squares, see which player’s queen(s) can reach it.
     *  - If only one side can reach a region, that side “owns” it.
     *  - Score the difference in total territory for each side.
     */
    private static int calculateEndgameTerritory(int[][] board, int player) {
        boolean[][] visited = new boolean[10][10];
        int playerTerritory = 0;
        int enemyTerritory = 0;
        int enemy = (player == 1) ? 2 : 1;

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (!visited[r][c] && board[r][c] == 0) {
                    // Flood-fill this empty region to get its size
                    int regionSize = floodFillCount(board, visited, r, c);
                    // Check if this region is near me and/or the enemy
                    boolean nearMe = isNearPlayer(board, r, c, player);
                    boolean nearEnemy = isNearEnemy(board, r, c, player);

                    if (nearMe && !nearEnemy) {
                        playerTerritory += regionSize;
                    }
                    else if (!nearMe && nearEnemy) {
                        enemyTerritory += regionSize;
                    }
                    // If region is reachable by both or by neither, treat it as contested => no single side “owns” it
                }
            }
        }

        // Higher means better for “player,” lower means better for “enemy”
        return playerTerritory - enemyTerritory;
    }

    /**
     * Flood-fill to find connected empty squares count.
     */
    private static int floodFillCount(int[][] board, boolean[][] visited, int startR, int startC) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC});
        visited[startR][startC] = true;
        int count = 0;

        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            count++;
            for (int i = 0; i < 4; i++) {
                int nr = cell[0] + dr[i], nc = cell[1] + dc[i];
                if (nr >= 0 && nr < 10 && nc >= 0 && nc < 10) {
                    if (!visited[nr][nc] && board[nr][nc] == 0) {
                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }
        return count;
    }

    

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

    private static int weightMove(List<int[]> moves, int row, int col) {
        int weight = 0;
        for (int[] move : moves) {
            weight += 1;
        }
        return weight;
    }

    public static int calculateInfluence(int[][] board, int player) {
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

    public static int calculateEnemyBlocking(int[][] board, int player) {
        int enemy = (player == 1) ? 2 : 1;
        int enemyMoves = MoveGenerator.generateAllMoves(board, enemy).size();
        // The more moves the enemy has, the worse for us.
        // Return negative so fewer enemy moves => bigger final score.
        return -enemyMoves;
    }

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

    private static int calculateContestAreas(int[][] board, int player) {
        int enemy = (player == 1) ? 2 : 1;
        boolean[][] visited = new boolean[10][10];
        int totalContestScore = 0;

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                // Look for an unvisited empty cell => potential territory block
                if (board[r][c] == 0 && !visited[r][c]) {
                    // Flood-fill to find the size of this empty block
                    int blockSize = floodFillCount(board, visited, r, c);
                    // Check if it's near the enemy, near us, or both.
                    boolean nearUs = isNearPlayer(board, r, c, player);
                    boolean nearThem = isNearEnemy(board, r, c, player);

                    // If near enemy only, large block => big penalty
                    if (nearThem && !nearUs && blockSize >= 10) {
                        // The bigger the region, the bigger the penalty
                        totalContestScore -= (blockSize);
                    }
                    // If near us only, large block => small bonus
                    else if (nearUs && !nearThem && blockSize >= 10) {
                        totalContestScore += (blockSize / 2);
                    }
                    // If near both or small => no penalty or small effect
                }
            }
        }
        return totalContestScore;
    }

    private static int calculateClusteringPenalty(int[][] board, int player) {
        List<int[]> queens = new ArrayList<>();
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == player) {
                    queens.add(new int[]{r, c});
                }
            }
        }

        int penalty = 0;
        // Sum of 1/dist between each pair (Manhattan)
        for (int i = 0; i < queens.size(); i++) {
            for (int j = i + 1; j < queens.size(); j++) {
                int dist = Math.abs(queens.get(i)[0] - queens.get(j)[0])
                         + Math.abs(queens.get(i)[1] - queens.get(j)[1]);
                if (dist < 3) {
                    // If super close (distance < 3), add a heavier penalty
                    penalty += (4 - dist); 
                } else if (dist < 5) {
                    // Mild penalty for moderately close
                    penalty += 1;
                }
            }
        }
        return penalty;
    }
}
