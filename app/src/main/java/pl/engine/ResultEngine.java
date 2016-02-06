package pl.engine;

import android.util.Log;

import java.util.List;

public class ResultEngine extends GameEngine{
	private List<ResultScore> resultScoreLists = gc.getResultScores();
    private List<Team> teams = gc.getTeams();
    private int playerAmount;
    private int cntPlayer = 0;
    private GameManager gameManager;
    public ResultEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
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
        if(event.equals("resultUI_Start")){
            gc.sendGameEvent(gc.getCurrentGameEngine().getClientStart());
            gameManager.initPlayerstoUI(teams);
        }else if(event.equals("result_ready")){
            showResultScore();
        }else if(event.equals("playerConfirm")){
            playerConfirm(clientId);
            cntPlayer++;
            onPlayerReady(playerAmount);
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
            strs += "'},";
        }
        strs = strs.substring(0, strs.length() - 1);
        strs += "]";
        gc.getGameLister().onIncommingEvent("getResultScores", new String[]{strs});
    }


}
