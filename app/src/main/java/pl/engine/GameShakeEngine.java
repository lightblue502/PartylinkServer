package pl.engine;

import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameShakeEngine extends GameEngine{
	private int playerAmount;
	private int cntPlayer = 0;
	private Player playerA;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private ResultScore resultScore = new ResultScore();
	public GameShakeEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
		super(gc, name,activityClass,clientStart);
		this.playerAmount = playerAmount;
		this.gameManager = new GameManager(resultScore, gc, 3, 3);
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if(event.equals("shakeUI_Start")){
			gameManager.initPlayerstoUI(teams);
		}
		else if(event.equals("shake_ready")){
			cntPlayer++;
			onPlayerReady(playerAmount);
		}
		else if(event.equals("shake_game")){
			gameManager.printScoreToNumber();
			gameManager.scoreManage(clientId, 10 );
		}
//		Utils.debug("clientId " + clientId + ", event" + event);
	}
	
	@Override
	public void startEngine() {
		Utils.debug("========================================");
		Utils.debug("======= 	SHAKE GAME START !!! ========");
		Utils.debug("========================================");
		for (Team team: teams) {
			team.printPlayers();
		}
		gc.sendGameEvent("shake-start");

	}

	@Override
	public void endEngine() {
		gameManager.stopTimer();
        gameManager.summaryScoreByGame(this, teams);
//		gc.sendGameEvent("numeric_start");
		// gc.sendGameEvent("qa_start");
		gc.nextEngine();
		
	}
	
	@Override
	public void onPlayerReady(int playerAmount) {
		resetPlayerShaketoUI();
		if(cntPlayer == playerAmount){

			if(gameManager.getRound() <= 3) {
				if (gameManager.getNumber() == 1) {
					gameManager.countDownGameReady(5);
					gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
						@Override
						public void ready() {
							gameManager.printReportRound();
							sendEventToTeams();
						}
					});
				}
				else{
					gameManager.printReportRound();
					sendEventToTeams();
                }
			}
			else{
				Utils.debug("END GAME..");
				endEngine();
			}
		}
	}
	
	
	public void sendEventToTeams(){
		int playerAmountTeamA = teams.get(0).getPlayerAmount();
		int playerAmountTeamB = teams.get(1).getPlayerAmount();
		int playerAmount =  (playerAmountTeamA != 0 )?playerAmountTeamA : playerAmountTeamB;
		int randomPlayerAmount = new Random().nextInt(playerAmount) + 1;
		Utils.debug("randomPlayerAmount :" + randomPlayerAmount );
		
		for (Team team : teams) {
			sendEventToTeam(team, randomPlayerAmount);
		}
		int randomTime = new Random().nextInt((5 - 2) + 1) + 2;
        gameManager.resetTimer();
        if(!gameManager.timerWasStarted())
		    gameManager.startTimer(randomTime, "change_shake");
//		gameManager.countdown(, randomTime, true);
		cntPlayer = 0;
	}
	public Player getCurrentPlayer(){
		return playerA;
	}
	public void sendEventToTeam(Team team, int randomPlayerAmount){
		for (int i = 0; i < randomPlayerAmount; i++) {
			int randomPlayers = new Random().nextInt(team.getPlayerAmount());
			Utils.debug("send to randomPlayerAmount: " + randomPlayerAmount );
			Utils.debug("send to randomPlayers: " + randomPlayers );
			playerA = team.getPlayers().get(randomPlayers);
			gc.sendGameEvent(playerA, "this_shake");
			//change UI
			gc.getGameLister().onIncommingEvent("shake", new String[]{ 
				String.valueOf(playerA.getCliendId()), team.getName()
			});
		}
		
	}

	public void resetPlayerShaketoUI(){
		if(cntPlayer <= 1){
			if(gameManager.getRound() <=1 && gameManager.getNumber()<=1 )
				return;

	        Log.d("DEBUG_reset_player_UI", "reset leaw");
			gc.getGameLister().onIncommingEvent("resetStage", new String[]{});
		}
	}
	
	
}
