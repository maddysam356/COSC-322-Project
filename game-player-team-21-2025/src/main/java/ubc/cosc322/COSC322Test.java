
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;



public class COSC322Test extends GamePlayer {

    private GameClient gameClient = null;
    private BaseGameGUI gamegui = null;
    private static PlayerMoveHandler playerMoveHandler; // New handler for player movement

    private String userName = null;
    private String passwd = null;

    
     

  public static void main(String[] args) {
      COSC322Test player = new COSC322Test("cosc322", "cosc322");

        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(() -> player.Go());
        }
        
        playerMoveHandler.Timer();
    }

   
     

  public COSC322Test(String userName, String passwd) {
      this.userName = userName;
      this.passwd = passwd;
      this.gamegui = new BaseGameGUI(this);

        // Initialize player movement handler
        this.playerMoveHandler = new PlayerMoveHandler(this);
    }

    
     

  public void onLogin() {
      gameClient.joinRoom("Okanagan Lake");
      userName = gameClient.getUserName();
      if (gamegui != null) {
          gamegui.setRoomInformation(gameClient.getRoomList());}}

    /**
     
Handles game messages from the server.*/@Override
  public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
      System.out.println(messageType);
      System.out.println(msgDetails);

        if (messageType.equals(GameMessage.GAME_STATE_BOARD)|| playerMoveHandler.queenMoved == true) {
            ArrayList<Integer> state = (ArrayList<Integer>) msgDetails.get("game-state");
            playerMoveHandler.updateBoardState(state); // Delegate board updates
            gamegui.setGameState(state);
            playerMoveHandler.queenMoved = false;
        }
        return true;
    }

    @Override
    public String userName() {
        return userName;
    }

    @Override
    public GameClient getGameClient() {
        return this.gameClient;
    }

    @Override
    public BaseGameGUI getGameGUI() {
        return this.gamegui;
    }

    @Override
    public void connect() {
        gameClient = new GameClient(userName, passwd, this);
    }
}
