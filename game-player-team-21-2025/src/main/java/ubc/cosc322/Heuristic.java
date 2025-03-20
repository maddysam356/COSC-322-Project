package ubc.cosc322;

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

    public static int calculateMobility(int[][] board){
        int playerMoves = MoveGenerator.generateAllMoves(board, 1).size();
        int opponentMoves = MoveGenerator.generateAllMoves(board, 2).size();
        return playerMoves-opponentMoves;
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
    
