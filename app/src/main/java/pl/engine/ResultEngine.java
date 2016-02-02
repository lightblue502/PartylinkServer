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
		Utils.debug("========================================");
		Utils.debug("==========  SUMMARY SCORE !!! ==========");
		Utils.debug("========================================");
		for (ResultScore resultScore : resultScoreLists) {
			resultScore.printResult();
		}


	}

	@Override
	public void endEngine() {
		gc.nextEngine();
		
	}

    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(event.equals("result_ready")){
            cntPlayer++;
            onPlayerReady(playerAmount);
        }
		
	}

    @Override
    public void onPlayerReady(int playerAmount) {
        if(cntPlayer == playerAmount) {
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


}
