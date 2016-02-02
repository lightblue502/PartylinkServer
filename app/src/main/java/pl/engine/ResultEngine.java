package pl.engine;

import android.util.Log;

import java.util.List;

public class ResultEngine extends GameEngine{
	private List<ResultScore> resultScoreLists = gc.getResultScores();
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
        }else if(event.equals("result_ready")){
            cntPlayer++;
            onPlayerReady(playerAmount);
        }
		
	}

    @Override
    public void onPlayerReady(int playerAmount) {
        if(cntPlayer == playerAmount) {
            process();
            Log.d("DEBUG", "Result Engine--");
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

    public void process(){
        Utils.debug("========================================");
        Utils.debug("==========  SUMMARY SCORE !!! ==========");
        Utils.debug("========================================");
        for (ResultScore resultScore : resultScoreLists) {
            resultScore.printResult();
        }

        String strs = "[";
        for (ResultScore resultScore : resultScoreLists) {
            strs += "{'team':'" + resultScore.getTeam().getName();
            strs += "','gameName':'" + resultScore.getGameName();
            strs += "'},";
        }
        strs = strs.substring(0, strs.length() - 1);
        strs += "]";
        gc.getGameLister().onIncommingEvent("getResultScores", new String[]{strs});
    }


}
