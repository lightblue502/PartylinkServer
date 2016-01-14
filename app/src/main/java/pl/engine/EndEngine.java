package pl.engine;

import java.util.List;

public class EndEngine extends GameEngine{
	private List<ResultScore> resultScoreLists = gc.getResultScores();
	public EndEngine(GameContext gc) {
		super(gc);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerReady(int playerAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		// TODO Auto-generated method stub
		
	}

}
