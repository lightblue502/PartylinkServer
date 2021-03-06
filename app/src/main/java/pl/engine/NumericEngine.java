package pl.engine;

import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

public class NumericEngine extends GameEngine{
	private int playerAmount;
	private int roundAmount = 3;
	private Integer answer;
	private int cntPlayer = 0;
	private int cntResumePlayer = 0;
	private int topicPerPlayer = 2;
    private boolean firstTime = false;
	private boolean isPlaying = false;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private ResultScore resultScore = new ResultScore();
	private boolean gamePaused = false;
	public NumericEngine(GameContext gc,int playerAmount, String name, Class activityClass,String clientStart) {
		super(gc, name, activityClass,clientStart);
		this.answer = null;
		this.playerAmount = playerAmount;
		this.gameManager = new GameManager(resultScore, gc, topicPerPlayer*playerAmount, roundAmount);
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
		gameManager.stopTimer();
		gameManager.summaryScoreByGame(this, teams);
	}

	public void sendGameEventToClient(String event, String[] param ){
		Utils.debug("GamePause: " + gamePaused + " event : " + event);
		if(!gamePaused){
			gc.sendGameEvent(event, param);
		}
	}
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if(!gamePaused) {
			if (event.equals("NumericServerUI_Start")) {
				gameManager.initPlayerstoUI(teams);
				gameManager.scoreManage(clientId, 0);
				sendGameEventToClient(gc.getCurrentGameEngine().getClientStart(), new String[]{});
			} else if (event.equals("numeric_ready")) {
				cntPlayer++;
				onPlayerReady(playerAmount);
			} else if (event.equals("numeric_ans") && isPlaying) {
				if (answer == Integer.parseInt(params[0])) {
					gameManager.scoreManage(clientId, 2);
					String team = gc.getTeamByClientId(clientId).getName();
					gc.getGameLister().onIncommingEvent("getSolves", new String[]{
							String.valueOf(answer),
							String.valueOf(clientId),
							team
					});
					isPlaying = false;

					//check for change question or change game
					gameManager.resetTimer();
					gameManager.countDownGameReady(3);
					gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
						@Override
						public void ready() {
							gameManager.resetTimer();
							gameManager.checkNumber();
							sendGameEventToClient("numeric_again", new String[]{});
						}
					});


				} else {
					gameManager.scoreManage(clientId, -1);
				}
				gameManager.printScoreToNumber();
			}
		}
		if(event.equals("game_pause")) {
			sendGameEventToClient("game_pause", new String[]{});
			gc.getGameLister().onIncommingEvent("game_pause", new String[]{});
			gameManager.stopTimer();
			gamePaused = true;
		}else if(event.equals("game_resume")){
			cntResumePlayer++;
			Player player = new Player(clientId, params[0],null);
			gc.sendGameEvent(player, "resume_ok");
			if(super.onPlayerResumeReady(playerAmount, cntResumePlayer)) {
				cntResumePlayer = 0;
				gamePaused = false;
				gc.sendGameEvent("game_resume");
				gc.getGameLister().onIncommingEvent("game_resume", new String[]{});
				gameManager.countDownGameReady(5);
			}
		}

	}

	@Override
	public void onPlayerReady(int playerAmount) {
		if(cntPlayer == playerAmount){
			if(gameManager.getRound() <= roundAmount) {
				if (gameManager.getNumber() == 1) {
					Log.d("DEBUG","NEW ROUND");
					sendGameEventToClient("numeric_newRound", new String[]{});
					gameManager.printReportRound();
					gameManager.countDownGameReady(5);
					gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
						@Override
						public void ready() {
                            if(gameManager.timerWasStarted())
                                gameManager.runTimerAgain();
							sendEventToTeams();
						}
					});
				} else {
					gameManager.printReportRound();
					sendEventToTeams();
				}
			}else{
				endEngine();
			}


		}
	}
	
	public void sendEventToTeams(){
        //reset timer prepare count
        gameManager.resetTimer();
		String[] ans = randomQuestion();
        sendEventToTeam(ans);

        //counter++; from 0;finish 5 seconds;
        if(!gameManager.timerWasStarted())
            gameManager.startTimer(5, "numeric_again");
		cntPlayer = 0;
		isPlaying = true;
	}

	public void sendEventToTeam(String[] ans){
	sendGameEventToClient("numeric_question", ans);
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
