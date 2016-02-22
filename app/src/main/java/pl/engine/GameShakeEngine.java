package pl.engine;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameShakeEngine extends GameEngine{
	private int playerAmount;
	private int cntPlayer = 0;
	private int cntResumePlayer = 0;
	private Player playerA;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private boolean gamePaused = false;
	private ResultScore resultScore = new ResultScore();
	public GameShakeEngine(GameContext gc, int playerAmount, String name, Class activityClass, String clientStart) {
		super(gc, name,activityClass,clientStart);
		this.playerAmount = playerAmount;
		this.gameManager = new GameManager(resultScore, gc, 1, 3);
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if(!gamePaused) {
			if (event.equals("shakeUI_Start")) {
				gameManager.initPlayerstoUI(teams);
			} else if (event.equals("shake_ready")) {
				cntPlayer++;
				onPlayerReady(playerAmount);
			} else if (event.equals("shake_game")) {
				gameManager.printScoreToNumber();
				gameManager.scoreManage(clientId, 10);
			}
		}
		if(event.equals("game_pause")) {
			sendGameEventToClient("game_pause", new String[]{});
			gc.getGameLister().onIncommingEvent("game_pause", new String[]{});
			gameManager.stopTimer();
			gamePaused = true;
		}else if(event.equals("game_resume")){
			cntResumePlayer++;
			Player player = new Player(clientId, params[0]);
			gc.sendGameEvent(player, "resume_ok");
			if(super.onPlayerResumeReady(playerAmount,cntResumePlayer)) {
				cntResumePlayer = 0;
				gamePaused = false;
				gameManager.runTimerAgain();
				sendGameEventToClient("game_resume", new String[]{});
				gc.getGameLister().onIncommingEvent("game_resume", new String[]{});
			}
		}
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
	public void onPlayerReady(int playerAmount) {
		resetPlayerShaketoUI();
		if(cntPlayer == playerAmount){

			if(gameManager.getRound() <= 3) {
				if (gameManager.getNumber() == 1) {
					gameManager.countDownGameReady(5);
					gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
						@Override
						public void ready() {
							if(gameManager.timerWasStarted())
								gameManager.runTimerAgain();
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
		int playerAmount =  Math.min(playerAmountTeamA, playerAmountTeamB);
		if(playerAmount == 0) playerAmount+=1;
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
		randomPlayerAmount = Math.min(randomPlayerAmount, team.getPlayerAmount());
		ArrayList<Integer> sendedPlayer = new ArrayList<Integer>();
		while (sendedPlayer.size() < randomPlayerAmount) {
			int randomPlayers = new Random().nextInt(team.getPlayerAmount());
			if(!sendedPlayer.contains(randomPlayers)) {
				sendedPlayer.add(randomPlayers);
				Utils.debug("send to randomPlayerAmount: " + randomPlayerAmount);
				Utils.debug("send to randomPlayers: " + randomPlayers);
				playerA = team.getPlayers().get(randomPlayers);
				sendGameEventToClient(playerA, "this_shake", new String[]{});
				//change UI
				gc.getGameLister().onIncommingEvent("shake", new String[]{
						String.valueOf(playerA.getCliendId()), team.getName()
				});
			}
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
