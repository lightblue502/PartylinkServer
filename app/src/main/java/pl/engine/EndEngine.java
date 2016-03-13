package pl.engine;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by nuhwi_000 on 8/2/2559.
 */
public class EndEngine extends GameEngine {
    private int playerAmount;
    private GameManager gameManager;
    private List<Team> teams = gc.getTeams();
    private int cntPlayer;

    public EndEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
        super(gc, name, activityClass, clientStart);
        this.playerAmount = playerAmount;
        this.gameManager = new GameManager(gc);
    }

    @Override
    public void startEngine() {

    }

    @Override
    public void endEngine() {
        gc.nextEngine();
    }

    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(event.equals("endUI_Start")){
            gc.sendGameEvent(gc.getCurrentGameEngine().getClientStart());
            gameManager.initPlayerstoUI(teams);
        }else if(event.equals("end_ready")){
            summaryScore();
        }else if(event.equals("playerRestartGame")){
            playerConfirm(clientId);
            onPlayerReady(playerAmount);
        }
    }

    public void summaryScore(){
        if(++cntPlayer == playerAmount){
            String teamWin = ResultScore.getTeamWin(gc.getResultScores());
            resultScore();

            Utils.debug("TEAM :" + teamWin + " WINNER!!!!");
            gc.getGameLister().onIncommingEvent("getTeamWin", new String[]{teamWin});
            cntPlayer = 0;
        }

    }

    public void resultScore(){
        Utils.debug("========================================");
        Utils.debug("==========  SUMMARY SCORE !!! ==========");
        Utils.debug("========================================");
        for (ResultScore resultScore : gc.getResultScores()) {
            resultScore.printResult();
        }

        String strs = "[";
        for (ResultScore resultScore : gc.getResultScores()) {
            strs += "{'team':'" + resultScore.getTeam().getName();
            strs += "','gameName':'" + resultScore.getGameName();
            strs += "','winRoundA':'" + resultScore.getWinRoundA();
            strs += "','winRoundB':'" + resultScore.getWinRoundB();
            strs += "'},";
        }
        strs = strs.substring(0, strs.length() - 1);
        strs += "]";
        gc.getGameLister().onIncommingEvent("getResultScores", new String[]{strs});
    }

    public void playerConfirm(int clientId){
        gc.getGameLister().onIncommingEvent("playerConfirm", new String[]{String.valueOf(clientId)});
    }

    @Override
    public void onPlayerReady(int playerAmount) {
        if(++cntPlayer == playerAmount){
//            gc.stopGameEngine();
            Log.d("DEBUG", "ResultEngine : Player Ready");
            gameManager.countDownGameReady(5);
            gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
                @Override
                public void ready() {
                    Log.d("DEBUG","call endEngine");
//                    gc.sendGameEvent("restartGame");
//                    gc.getGameLister().onIncommingEvent("restartGame", new String[]{});

                    endEngine();
                }
            });
        }
    }


}
