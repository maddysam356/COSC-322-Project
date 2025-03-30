package ubc.cosc322;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Returns a single heuristic score for the board state
public class Heuristic {
    public static int evaluateBoard(int[][] board) {
        int mobilityScore = calculateWeightedMobility(board);
        int influenceScore = calculateInfluence(board);
        int territoryScore = calculateTerritoryControl(board);
        int opponentBlockingScore = calculateEnemyBlocking(board);
        int queenSafetyScore = calculateQueenSafety(board);

        // formula
        return (int) ((2.5 * mobilityScore) + (2 * influenceScore) + (1.5 * territoryScore) + 
                      (1.2 * opponentBlockingScore) - (1 * queenSafetyScore));
    }


    //mobitity 
    public static int calculateWeightedMobility(int[][] board) {
        int playerMoves = 0;
        int opponentMoves = 0;
        //loop through board
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 1) { // bot queen =1
                    playerMoves += weightMove(MoveGenerator.findMovesForQueen(board, i, j), i, j);
                } else if (board[i][j] == 2) { // enemy queen =2
                    opponentMoves += weightMove(MoveGenerator.findMovesForQueen(board, i, j), i, j);
                }
            }
        }
        return playerMoves - opponentMoves;
    }

    //center move high weight
    //edge moves low weight
    //more mobility in center so it is prioritised
    private static int weightMove(List<int[]> moves, int row, int col) {
        int weight = 0;
        for (int[] move : moves) {
            int r = move[0], c = move[1];
            // center moves
            if (r >= 3 && r <= 6 && c >= 3 && c <= 6) {
                weight += 2; 
            } else {
                weight += 1; // all other moves
            }
        }
        return weight;
    }

    //influence
    public static int calculateInfluence(int[][] board) {
        int[][] influenceMap = new int[10][10]; // new board for calculations

        //loop through board
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // check for bot queens and enemy queens
                if (board[i][j] == 1) {
                    updateInfluence(board, influenceMap, i, j, 1);
                } else if (board[i][j] == 2) {
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

    //assisting function
    private static void updateInfluence(int[][] board, int[][] influenceMap, int row, int col, int value) {
        int[] directions = {-1, 0, 1}; //possible values that can be taken
        //eg left by 1 is (-1,0)

        //by looping over go through all possible moves around it
        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue;//no move so skip
                int r = row + dr, c = col + dc;
                int influence = 3; //near queen, increase influence
                while (MoveGenerator.isValid(board, r, c)) {// until queen path is blocked
                    influenceMap[r][c] += (value * influence);
                    influence--; //decreament influence since further away
                    r += dr;
                    c += dc;
                }
            }
        }
    }


    public static int calculateTerritoryControl(int[][] board) {
        boolean[][] visited = new boolean[10][10];//1 if item present
        int playerScore = 0, opponentScore = 0; //to track control

        //loop through board
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 0 && !visited[i][j]) {//if empty area and not already checked
                    int areaSize = floodFill(board, visited, i, j);//find other empty area connected to tile
                    if (isNearPlayer(board, i, j)) {//check if newar bot and increase score
                        playerScore += areaSize;
                    } else if (isNearEnemy(board, i, j)) {//check if near enemy and increase score
                        opponentScore += areaSize;
                    }
                }
            }
        }
        return playerScore - opponentScore;
    }

    //connect neighbouring tiles- BFS
    private static int floodFill(int[][] board, boolean[][] visited, int row, int col) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});//add passed in empty tile
        visited[row][col] = true;//mark on map
        int count = 0;//amount of empty tiles

        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};//all directions- only check 4 way adjecency
        while (!queue.isEmpty()) {//while elements in queue
            int[] pos = queue.poll();//remove element
            count++;
            
            //check for each neighbour
            for (int d = 0; d < 4; d++) {
                int r = pos[0] + dr[d], c = pos[1] + dc[d];
                // if inside board & not visited &empty
                if (MoveGenerator.isValid(board, r, c) && !visited[r][c] && board[r][c] == 0) {
                    visited[r][c] = true;
                    queue.add(new int[]{r, c}); //add to queue
                }
            }
        }
        return count;//number of tiles in empty area
    }

    //check for bot =1
    private static boolean isNearPlayer(int[][] board, int row, int col) {
        return checkBoard(board, row, col, 1);
    }

    //check for enemy =2
    private static boolean isNearEnemy(int[][] board, int row, int col) {
        return checkBoard(board, row, col, 2);
    }

    private static boolean checkBoard(int[][] board, int row, int col, int player) {
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};//moveable directions - no diagonal
        for (int d = 0; d < 4; d++) {
            int r = row + dr[d], c = col + dc[d];
            if (MoveGenerator.isValid(board, r, c) && board[r][c] == player) {//check if 1 or 2 depending on call
                return true;
            }
        }
        return false;
    }

    //better move if enemy has less space
    public static int calculateEnemyBlocking(int[][] board) {
        int MoveCountBefore = MoveGenerator.generateAllMoves(board, 2).size();
        int MoveCountAfter = MoveGenerator.generateAllMoves(board, 2).size();
        return MoveCountBefore - MoveCountAfter;// better if less moves after move
    }

    //better is queens have more space
    public static int calculateQueenSafety(int[][] board) {
        int safetyPenalty = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 1) {
                    int moves = MoveGenerator.findMovesForQueen(board, i, j).size();
                    if (moves < 3) safetyPenalty += (4 - moves); //less space higher penalty
                }
            }
        }
        return safetyPenalty;
    }
}
    