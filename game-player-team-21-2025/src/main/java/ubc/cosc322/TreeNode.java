package ubc.cosc322;

public class TreeNode {
    private int[][] boardState; // The current board state
    private int queenStartRow;  // The starting row of the queen
    private int queenStartCol;  // The starting column of the queen
    private int queenEndRow;    // The ending row of the queen
    private int queenEndCol;    // The ending column of the queen
    private int arrowRow;       // The row where the arrow is shot
    private int arrowCol;       // The column where the arrow is shot

    /**
     * Constructor for TreeNode.
     * @param boardState The board state after the move.
     * @param queenStartRow The starting row of the queen.
     * @param queenStartCol The starting column of the queen.
     * @param queenEndRow The ending row of the queen.
     * @param queenEndCol The ending column of the queen.
     * @param arrowRow The row where the arrow is shot.
     * @param arrowCol The column where the arrow is shot.
     */
    public TreeNode(int[][] boardState, int queenStartRow, int queenStartCol, int queenEndRow, int queenEndCol, int arrowRow, int arrowCol) {
        this.boardState = boardState;
        this.queenStartRow = queenStartRow;
        this.queenStartCol = queenStartCol;
        this.queenEndRow = queenEndRow;
        this.queenEndCol = queenEndCol;
        this.arrowRow = arrowRow;
        this.arrowCol = arrowCol;
    }

    // Getters for the board state and move details
    public int[][] getBoardState() {
        return boardState;
    }

    public int getQueenStartRow() {
        return queenStartRow;
    }

    public int getQueenStartCol() {
        return queenStartCol;
    }

    public int getQueenEndRow() {
        return queenEndRow;
    }

    public int getQueenEndCol() {
        return queenEndCol;
    }

    public int getArrowRow() {
        return arrowRow;
    }

    public int getArrowCol() {
        return arrowCol;
    }

    // Optional: Add a method to print the move details for debugging
    public void printMoveDetails() {
        System.out.println("Queen moved from (" + queenStartRow + ", " + queenStartCol + ") to (" + queenEndRow + ", " + queenEndCol + ")");
        System.out.println("Arrow shot to (" + arrowRow + ", " + arrowCol + ")");
    }
}