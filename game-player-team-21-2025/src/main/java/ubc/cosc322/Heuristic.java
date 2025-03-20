package ubc.cosc322;

import java.util.List;

public class Heuristic {
    public static int evaluateBoard(int[][] board) {
        int mobilityScore = calculateWeightedMobility(board);
        int influenceScore = calculateInfluence(board);
        int territoryScore = calculateTerritoryControl(board);
        int opponentBlockingScore = calculateOpponentBlocking(board);
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







    public static int calculateInfluence(int[][] board){
        int playerInfluence = 0;
        int opponentInfluence = 0;
        for(int i = 0; i<10;i++){
            for(int j = 0; j<10;j++){
                if(board[i][j]==1){
                    playerInfluence+=MoveGenerator.findMovesForQueen(board, i, j).size();
                }else if(board[i][j]==2){
                    opponentInfluence+=MoveGenerator.findMovesForQueen(board, i, j).size();
                }
            }
        }
return playerInfluence-opponentInfluence;
    }

    public static int calculateControl(int[][] board){
        int[][] controlMap = new int[10][10];
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (board[r][c] == 0) {
                    int playerControl = countAttackers(board, r, c, 1);
                    int opponentControl = countAttackers(board, r, c, 2);
                    if (playerControl > opponentControl) controlMap[r][c] = 1;
                    else if (opponentControl > playerControl) controlMap[r][c] = -1;
                }
            }
        }

        int territoryScore = 0;
        for (int[] row : controlMap) {
            for (int cell : row) {
                territoryScore += cell;
            }
        }
        return territoryScore;
    }
    public static int countAttackers(int[][] board, int row, int col, int player){
        int attackers = 0;
        int[] directions = {-1, 0, 1};

        for (int dr : directions) {
            for (int dc : directions) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr, c = col + dc;

                while (MoveGenerator.isValid(board, r, c)) {
                    r += dr;
                    c += dc;
                }
                if (MoveGenerator.isValid(board, r, c) && board[r][c] == player) {
                    attackers++;
                }
            }
        }
        return attackers;
    }
}
    
