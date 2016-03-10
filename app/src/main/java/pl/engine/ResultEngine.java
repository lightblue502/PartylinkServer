package pl.engine;

import android.util.Log;

import java.util.List;

public class ResultEngine extends GameEngine{
	private List<ResultScore> resultScoreLists = gc.getResultScores();
    private List<Team> teams = gc.getTeams();
    private int playerAmount;
    private int cntPlayer = 0;
    private int cntResumePlayer = 0;
    private boolean gamePaused = false;
    private GameManager gameManager;
    public ResultEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
        super(gc, name, activityClass, clientStart);
        this.playerAmount = playerAmount;
        this.gameManager = new GameManager(gc);
	}

	@Override
	public void startEngine() {
        gamePaused = false;

	}

	@Override
	public void endEngine() {
		gc.nextEngine();
		
	}

    public void sendGameEventToClient( String event, String[] param ){
        if(!gamePaused){
            gc.sendGameEvent(event, param);
        }
    }
    public void sendGameEventToClient(Player player, String event, String[] param ){
        if(!gamePaused){
            gc.sendGameEvent(player, event, param);
        }
    }
    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        Utils.debug("gamePaused: " + gamePaused);
        Utils.debug("ResultEngine : " + event);
        if(!gamePaused) {
            if(event.equals("resultUI_Start")){
                sendGameEventToClient(gc.getCurrentGameEngine().getClientStart(), new String[]{});
                gameManager.initPlayerstoUI(teams);
            }else if(event.equals("result_ready")){
                cntPlayer++;
                showResultScore();
            }else if(event.equals("playerConfirm")){
                playerConfirm(clientId);
                cntPlayer++;
                onPlayerReady(playerAmount);
            }
        }
        if(event.equals("game_pause")) {
            sendGameEventToClient("game_pause", new String[]{});
            gc.getGameLister().onIncommingEvent("game_pause", new String[]{});
            gamePaused = true;
        }
        else if(event.equals("game_resume")){
            cntResumePlayer++;
            Player player = new Player(clientId, params[0]);
            //for disable resume button
            gc.sendGameEvent(player, "resume_ok");
            if(super.onPlayerResumeReady(playerAmount,cntResumePlayer)) {
                cntResumePlayer = 0;
                gamePaused = false;
                sendGameEventToClient("game_resume", new String[]{});
                gc.getGameLister().onIncommingEvent("game_resume", new String[]{});
            }
        }

	}

    @Override
    public void onPlayerReady(int playerAmount) {
        if(cntPlayer == playerAmount){
            Log.d("DEBUG", "ResultEngine : Player Ready");
            gameManager.countDownGameReady(5);
            gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
                @Override
                public void ready() {
                    Log.d("DEBUG","call endEngine");
                    endEngine();
                }
            });
        }

    }

    public void playerConfirm(int clientId){
//        Player player = gc.getPlayerByClientID(clientId);
//        String name = player.getName();
//        String teamName = gc.getTeamByClientId(clientId).getName();
//        String str = "{'clientId':"+clientId+",'name':'"+name+"','team':'"+teamName+"'}";

        gc.getGameLister().onIncommingEvent("playerConfirm",new String[]{String.valueOf(clientId)});
    }

    public void showResultScore(){
        Log.d("DEBUG","showResultScore, Call function |player amount:" + cntPlayer + "| playerAmount:"+playerAmount);
        if(cntPlayer == playerAmount){
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
            cntPlayer = 0;
        }

    }


}
