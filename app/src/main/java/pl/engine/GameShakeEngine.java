package pl.engine;

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
	public GameShakeEngine(GameContext gc, int playerAmount, String name) {
		super(gc, name);
		this.playerAmount = playerAmount;
		this.gameManager = new GameManager(resultScore, gc, 1, 3);
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if(event.equals("shake_ready")){
			cntPlayer++;
			onPlayerReady(playerAmount);
		}
		else if(event.equals("shake_game")){
			gameManager.printScoreToNumber();
			gameManager.scoreManage(clientId, Integer.parseInt(params[1]) );
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
		gc.sendGameEvent("numeric_start");
		gc.nextEngine();
		
	}
	
	@Override
	public void onPlayerReady(int playerAmount) {
		if(cntPlayer == playerAmount){
			
			if(gameManager.checkGameRound()){
				sendEventToTeams();
			}
			else{
				Utils.debug("END GAME..");
				gameManager.printScoreToWIN();
				if(gameManager.getTeamWin() != null){
					Team team = gameManager.getTeamWin().equals("teamA")?teams.get(0) :teams.get(1);
					resultScore.setResult(team, this);
					gc.addResultScore(resultScore);
				}
				gameManager.resetWinRound();
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
		gameManager.countdown("change_shake", randomTime, true);
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
		}
		
	}
	
	
	
}
