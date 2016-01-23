package pl.engine;

import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

public class NumericEngine extends GameEngine{
	private int playerAmount;
	private Integer answer;
	private int cntPlayer = 0;
	private int topicPerPlayer = 4;
    private boolean firstTime = false;
	private boolean isPlaying = false;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private ResultScore resultScore = new ResultScore();
	public NumericEngine(GameContext gc,int playerAmount, String name, String activityName) {
		super(gc, name, activityName);
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
	}

	@Override
	public void endEngine() {
		Utils.debug("END GAME..");
		gameManager.printScoreToWIN();
		if(gameManager.getTeamWin() != null){
			Team team = gameManager.getTeamWin().equals("teamA")?teams.get(0) :teams.get(1);
			resultScore.setResult(team, this);
			gc.addResultScore(resultScore);
		}
		gameManager.resetWinRound();
		gc.nextEngine();
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if (event.equals("NumericServerUI_Start")){
            Log.d("DEBUG_onincome ", "numereic start");
            gameManager.initPlayerstoUI(teams);
            gc.sendGameEvent("numeric_start");
		}
		else if(event.equals("numeric_again")){
			cntPlayer++;
			Log.d("debug", "\t\tonIncomingEvent: " + cntPlayer);
			onPlayerReady(playerAmount);
		}
		else if(event.equals("numeric_ans") && isPlaying){
//			Utils.debug("clientID :" +clientId + " ---- ans :"+ params[0]);
			gameManager.printScoreToNumber();
			if(answer == Integer.parseInt(params[0])){
				isPlaying = false;
				gameManager.scoreManage(clientId, 2);
				gameManager.stopTimer();
				gc.sendGameEvent("numeric_change");
				
			}else{
				gameManager.scoreManage(clientId, -1);
			}
		}

	}

	@Override
	public void onPlayerReady(int playerAmount) {
		if(cntPlayer == playerAmount){
            if(gameManager.getNumber() == 1){
					gc.sendGameEvent("numeric_newRound", new String[]{});
					gc.getGameLister().onIncommingEvent("getQuestion", new String[]{"ready"});
                gameManager.countDownGameReady(5);
                gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
                    @Override
                    public void ready() {
                        gc.sendGameEvent("numeric_ready", new String[]{});
                        gameManager.printReportRound();
                        sendEventToTeams();
						}
					});
            }else{
				gc.sendGameEvent("numeric_ready", new String[]{});
                gameManager.printReportRound();
                sendEventToTeams();
            }


		}
	}
	
	public void sendEventToTeams(){
		String[] ans = randomQuestion();
        sendEventToTeam(ans);

		gameManager.countdown("numeric_change", 5, true);
		cntPlayer = 0;
		isPlaying = true;
	}

	public void sendEventToTeam(String[] ans){
		gc.sendGameEvent("numeric_question", ans);
	}

	public String[] randomQuestion(){
		Character[] chars = {'+', '-'};
		int randomSymbol = new Random().nextInt(chars.length);
		char symbol = chars[randomSymbol];
		int maxRandQuestion = 10;
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
        gc.getGameLister().onIncommingEvent("getQuestion",new String[]{
                String.valueOf(number1),String.valueOf(number2), String.valueOf(symbol), String.valueOf(ans)
        });
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
