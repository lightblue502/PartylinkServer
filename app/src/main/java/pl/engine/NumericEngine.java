package pl.engine;

import java.util.List;
import java.util.Random;

public class NumericEngine extends GameEngine{
	private int playerAmount;
	private Integer answer;
	private int cntPlayer = 0;
	private int topicPerPlayer = 2;
	private boolean isPlaying = false;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private ResultScore resultScore = new ResultScore();
	public NumericEngine(GameContext gc,int playerAmount, String name) {
		super(gc, name);
		this.answer = null;
		this.playerAmount = playerAmount;
		this.gameManager = new GameManager(resultScore, gc, topicPerPlayer*playerAmount, 3);
	}

	@Override
	public void startEngine() {
		Utils.debug("========================================");
		Utils.debug("=======  NUMERIC GAME START !!! ========");
		Utils.debug("========================================");
		for (Team team: teams) {
			team.printPlayers();
		}
		gc.sendGameEvent("numeric_start");
	}

	@Override
	public void endEngine() {
		gc.nextEngine();
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if(event.equals("numeric_ready")){
			cntPlayer++;
			onPlayerReady(playerAmount);
		}
		else if(event.equals("numeric_ans") && isPlaying){
//			Utils.debug("clientID :" +clientId + " ---- ans :"+ params[0]);
			gameManager.printScoreToNumber();
			if(answer == Integer.parseInt(params[0])){
				isPlaying = false;
				gameManager.scoreManage(clientId, 100);
				gameManager.stopTimer();
				gc.sendGameEvent("numeric_change");
				
			}else{
				gameManager.scoreManage(clientId, -50);
			}
		}
	}

	@Override
	public void onPlayerReady(int playerAmount) {
		if(cntPlayer == playerAmount){
			
			if(gameManager.checkGameRound()){
				sendEventToTeams();
			}
			else{
//				gc.sendGameEvent("change_game");
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
		String[] ans = randomQuestion();
		for (Team team : teams) {
			sendEventToTeam(team, playerAmount, ans);
		}
		gameManager.countdown("numeric_change", 5, true);
		cntPlayer = 0;
		isPlaying = true;
	}
	
	public void sendEventToTeam(Team team, int PlayerAmount, String[] ans){
		
		for (int i = 0; i < PlayerAmount; i++) {
			if(team.getPlayers().size() != 0)
				gc.sendGameEvent(team.getPlayers().get(i), "numeric_question", ans);
		}
		
	}
	
	public String[] randomQuestion(){
		Character[] chars = {'+', '-', '*', '%'};
		int randomSymbol = new Random().nextInt(chars.length);
		char symbol = chars[randomSymbol];
		int maxRandQuestion = 20;
		int number1 = new Random().nextInt(maxRandQuestion)+1;
		int number2 = new Random().nextInt(maxRandQuestion)+1;
		int ans = 0;
		switch (symbol) {
		case '+':ans = number1 + number2;
			break;
		case '-':ans = number1 - number2;
			break;
		case '*':ans = number1 * number2;
			break;
		case '/':ans = number1 / number2;
			break;

		default:
			break;
		}
		answer = ans;
		Utils.debug(number1+ " "+symbol+" "+number2+" = ?? --- > ans =="+ans + " -- "+answer);
		return randomAnswer(ans);
	}
	
	public String[] randomAnswer(int ans) {
		int max = 9;
		int maxRandAnswer = 100;
		String[] randomAnswerArr = new String[9];
		
		randomAnswerArr[0] = String.valueOf(ans);
		int curr = 1;
		while ( curr < max) {
			int randomValue = new Random().nextInt(maxRandAnswer) + 1;
			int j;
			for (j = 0; j < curr; j++)
				if(Integer.valueOf(randomAnswerArr[j]) == randomValue)
					break;
			if(j == curr){
				randomAnswerArr[curr] = String.valueOf(randomValue);
				curr++;
			}
		}
		
		return randomAnswerArr;
	}

	


}
