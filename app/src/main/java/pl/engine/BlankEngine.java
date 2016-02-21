package pl.engine;

import android.util.Log;

import java.util.List;

/**
 * Created by nuhwi_000 on 8/2/2559.
 */
public class BlankEngine extends GameEngine {
    private int playerAmount;
    private GameManager gameManager;
    private List<Team> teams = gc.getTeams();
    private int cntPlayer;

    public BlankEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
        super(gc, name, activityClass, clientStart);
        this.playerAmount = playerAmount;
        this.gameManager = new GameManager(gc);
    }

    @Override
    public void startEngine() {

    }

    @Override
    public void endEngine() {

    }

    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(event.equals("blankUI_Start")){
            gc.sendGameEvent(gc.getCurrentGameEngine().getClientStart());
            gc.getGameLister().onIncommingEvent("setDisplay", new String[]{""});
//            gameManager.initPlayerstoUI(teams);
        }
        else if(event.equals("blank_ready")){
            cntPlayer++;
            Log.d("DEBUG_blankReady","");

        }
//        else if(event.equals("playerConfirm")){
//            playerConfirm(clientId);
//            cntPlayer++;
//            onPlayerReady(playerAmount);
//        }
    }

//    public void playerConfirm(int clientId){
//        gc.getGameLister().onIncommingEvent("playerConfirm",new String[]{String.valueOf(clientId)});
//    }

    @Override
    public void onPlayerReady(int playerAmount) {
//        if(cntPlayer == playerAmount){
//            Log.d("DEBUG", "BlankEngine : Player Ready");
//            gameManager.countDownGameReady(5);
//            gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
//                @Override
//                public void ready() {
//                    Log.d("DEBUG","call blankEngine");
//                    endEngine();
//                }
//            });
//        }
    }
}
