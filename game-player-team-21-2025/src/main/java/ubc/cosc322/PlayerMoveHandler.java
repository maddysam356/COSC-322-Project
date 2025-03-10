package ubc.cosc322;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import ygraph.ai.smartfox.games.BaseGameGUI;

public class PlayerMoveHandler {
    private COSC322Test gameInstance;
    private int[][] boardState = new int[11][11]; // Store board state
    public boolean queenMoved  = false;
    public boolean arrowSelected = false;
    // Track selected queen position
    private int[] selectedQueen = null;
    private int[] newQueen = null;
    private int[] selectedArrow = null;
    public PlayerMoveHandler(COSC322Test gameInstance) {
        this.gameInstance = gameInstance;
        enablePlayerMovement();
    }

    /**
     * Updates the board state when received from the server.
     */
    public void updateBoardState(ArrayList<Integer> state) {
        if (state == null || state.size() < 100) {
            System.out.println("Error: Invalid game state received.");
            return;
        }

        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                boardState[i][j] = state.get(i * 11 + j);
            }
        }

        // Debugging: Print the board state
        System.out.println("Updated Board State:");
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(boardState[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Enables player movement by adding a mouse listener.
     */
    public void enablePlayerMovement() {
        gameInstance.getGameGUI().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / 50; // Adjust based on board tile size
                int col = e.getX() / 50;

                if (selectedQueen == null) { // Step 1: Select Queen
                    selectQueen(Math.abs(row -11), col);
                }  
                else if (!queenMoved) { // Step 2: Move Queen
                    moveQueen(Math.abs(row -11), col);
                    
                }  
                else if (!arrowSelected && queenMoved) { // Step 3: Select Arrow after moving queen
                    selectArrow(Math.abs( row-11), col);
                    
                }  
                if (queenMoved && arrowSelected) { // Step 4: Only update game state once both are selected
                    UpdateGameState();
            }
                }
        });
    }

    /**
     * Selects a queen if it belongs to the player.
     */
    private void selectQueen(int row, int col) {
        System.out.println("Clicked on: " + row + ", " + col);

        if (!isValidPosition(row, col)) {
            System.out.println("Invalid selection. Out of bounds.");
            return;
        }

        System.out.println("Board Value: " + boardState[row][col]); // Debugging

        if (isPlayerQueen(row, col)) {
            selectedQueen = new int[]{row, col};
            System.out.println("Selected Queen at: " + row + ", " + col);
        } else {
            System.out.println("Invalid selection. Choose a valid queen.");
        }
    }
    
    private void selectArrow(int row, int col) {
        System.out.println("Clicked on: " + row + ", " + col);

        if (!isValidPosition(row, col)) {
            System.out.println("Invalid selection. Out of bounds.");
            return;
        }

        if (isAvailable(row, col)) {
            if (newQueen != null && isValidArrowMove(newQueen[0], newQueen[1], row, col)) {
                selectedArrow = new int[]{row, col};
                System.out.println("Selected arrow at: " + row + ", " + col);
                arrowSelected = true;
            } else {
                System.out.println("Invalid arrow placement");
            }
        } else {
            System.out.println("Invalid selection. Choose a valid square.");
        }
    }
    /**
     * Moves the queen if the move is valid.
     */
    private void moveQueen(int newRow, int newCol)  {
        if (selectedQueen == null) return;

        if (!isValidPosition(newRow, newCol)) {
            System.out.println("Invalid move. Out of bounds.");
            return;
        }

        if (isValidMove(selectedQueen[0], selectedQueen[1], newRow, newCol)) {
            
            
            
            
            newQueen = new int[]{newRow, newCol};
            
            
            
           
           
            
            
            System.out.println("Queen moved to: " + newRow + ", " + newCol);
            queenMoved = true;
            
           
        } else {
            System.out.println("Invalid move. Try again.");
            queenMoved = false;
        }
    }
    private void UpdateGameState () {
    	
    	ArrayList<Integer> queenCurrent = new ArrayList<>();
        ArrayList<Integer> queenNew = new ArrayList<>();
        ArrayList<Integer> arrowNew = new ArrayList<>();
        queenCurrent.add(selectedQueen[0]);
        queenCurrent.add(selectedQueen[1]);
        queenNew.add(newQueen[0]);
        queenNew.add(newQueen[1]);
        arrowNew.add(selectedArrow[0]);
        arrowNew.add(selectedArrow[1]);
        
        
        boardState[newQueen[0]][newQueen[1]] = boardState[selectedQueen[0]][selectedQueen[1]];
        boardState[selectedQueen[0]][selectedQueen[1]] = 0;
        boardState[selectedArrow[0]][selectedArrow[1]] = 3;
        
        gameInstance.getGameGUI().updateGameState(queenCurrent, queenNew, arrowNew); // No arrow placement yet
        selectedQueen = null; // Reset selection
        selectedArrow = null;
        newQueen = null;
        queenMoved = false;
        arrowSelected = false;
    }

    /**
     * Checks if a selected piece belongs to the player.
     */
    private boolean isPlayerQueen(int row, int col) {
        if (!isValidPosition(row, col)) return false;
        return (boardState[row][col] == 1 || boardState[row][col] == 2); // 1 = White, 2 = Black
    }
    
    private boolean isAvailable(int row, int col) {
    	
    	
    	if (boardState[row][col] == 0) {
    		
    		return true;
    		
    	}
    	return false;
    	
    }

    /**
     * Validates if the move follows the game's rules.
     */
    private boolean isValidMove(int oldRow, int oldCol, int newRow, int newCol) {
        if (oldRow == newRow && oldCol == newCol) return false; // No movement

        if (!isValidPosition(newRow, newCol)) return false; // Out of bounds

        if (boardState[newRow][newCol] != 0) return false; // ❌ Prevent moving onto occupied squares

        int rowStep = Integer.compare(newRow, oldRow);
        int colStep = Integer.compare(newCol, oldCol);

        int r = oldRow + rowStep;
        int c = oldCol + colStep;

        while (isValidPosition(r, c) && (r != newRow || c != newCol)) {
            if (boardState[r][c] != 0) return false; // ❌ Obstacle detected
            r += rowStep;
            c += colStep;
        }
        return true;
    }


    /**
     * Ensures row and column are within valid board range.
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row <= 10 && col >= 0 && col <= 10;
    }
    /**
     * Ensures the arrow can only be placed in a valid straight-line direction from the queen's new position.
     */
    private boolean isValidArrowMove(int queenRow, int queenCol, int arrowRow, int arrowCol) {
        // Check if it's in the same row, column, or diagonal
        if (queenRow == arrowRow || queenCol == arrowCol || Math.abs(queenRow - arrowRow) == Math.abs(queenCol - arrowCol)) {
            
            int rowStep = Integer.compare(arrowRow, queenRow);
            int colStep = Integer.compare(arrowCol, queenCol);

            int r = queenRow + rowStep;
            int c = queenCol + colStep;

            // Ensure there are no obstacles in the path
            while (isValidPosition(r, c) && (r != arrowRow || c != arrowCol)) {
                if (boardState[r][c] != 0) return false; // Obstacle detected
                r += rowStep;
                c += colStep;
            }
            return true;
        }
        return false; // Not a valid straight-line move
    }

    
    
}