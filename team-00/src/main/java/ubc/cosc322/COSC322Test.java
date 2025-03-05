
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test("cosc322", "cosc322");
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
		this.gamegui = new BaseGameGUI(this);

    	//and implement the method getGameGUI() accordingly
    	//this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {

		List<Room> arr = gameClient.getRoomList();
		for(int i = 0; i<arr.size(); i++)
		 System.out.println(arr.get(i));
		 gameClient.joinRoom("Kalamalka Lake");
    	
		userName = gameClient.getUserName();
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

    }

    @Override
public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    System.out.println("Received message: " + messageType);
    System.out.println("Details: " + msgDetails);

    if (messageType.equals(GameMessage.GAME_STATE_BOARD)) {
        // Extract the board state from the message
        ArrayList<Integer> state = (ArrayList<Integer>) msgDetails.get("game-state");
        gamegui.setGameState(state);
        System.out.println("Board size: " + state.size()); 
        System.out.println("Full Game State: " + state);
        
        // Debugging: Check the board size
        System.out.println("Board size received: " + state.size());
       

        // Convert 1D list into a 10x10 board
        int[][] board = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = state.get(i * 11 + j+12);
            }
        }

        // Assume player 1 (1) or player 2 (2) – replace with actual player ID
        int player = 1; 
        int count=0;
        int best = 0;
        int[] bestMove={};
        // Generate and print all moves
        List<int[]> moves = MoveGenerator.generateAllMoves(board, player);
        System.out.println("Available Moves:");
        System.out.println(moves.size());
        for (int[] move : moves) {
            int heuristic = Heuristic.evaluateBoard(MoveGenerator.makeMove(board, move));
            if(heuristic>best){best=heuristic; bestMove = move;}
            if(count==1000){break;}else{
                if(count>500){
            System.out.printf("Queen moves from (%d, %d) to (%d, %d), Arrow at (%d, %d) heuristic: %d\n",
                    move[0], move[1], move[2], move[3], move[4], move[5],heuristic);}
                    count++;
                    }
        }
        System.out.printf("Best Queen moves from (%d, %d) to (%d, %d), Arrow at (%d, %d) heuristic: %d\n",
        bestMove[0], bestMove[1], bestMove[2], bestMove[3], bestMove[4], bestMove[5],best);
    }

    return true;
}
    
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return  this.gamegui;
		
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}
	

 
}//end of class