package pl.engine;

import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

public class NumericEngine extends GameEngine{
	private int playerAmount;
	private Integer answer;
	private int cntPlayer = 0;
	private int topicPerPlayer = 2;
    private boolean firstTime = false;
	private boolean isPlaying = false;
	private List<Team> teams = gc.getTeams();
	private GameManager gameManager;
	private ResultScore resultScore = new ResultScore();
	public NumericEngine(GameContext gc,int playerAmount, String name, Class activityClass,String clientStart) {
		super(gc, name, activityClass,clientStart);
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
        gameManager.stopTimer();
		gameManager.summaryScoreByGame(this, teams);
		gc.nextEngine();
	}
	
	@Override
	public void onIncomingEvent(int clientId, String event, String[] params) {
		if (event.equals("NumericServerUI_Start")){
            gameManager.initPlayerstoUI(teams);
            gameManager.scoreManage(clientId, 0);
            //send event to client;
            gc.sendGameEvent("numeric_start");
		}
		else if(event.equals("numeric_ready")){
			cntPlayer++;
			onPlayerReady(playerAmount);
		}
		else if(event.equals("numeric_ans") && isPlaying){
            gameManager.resetTimer();
            Log.d("DEBUG","(coundownReady -- 3)");
            if (gameManager.isInRound()) {
                gameManager.checkNumber();
            } else {
                endEngine();
            }
			//Utils.debug("in numericANS -- Score :" + clientId + " ---- ans :" + params[0]);
			if(answer == Integer.parseInt(params[0])){
                gameManager.scoreManage(clientId, 2);
                String team = gc.getTeamByClientId(clientId).getName();
                gc.getGameLister().onIncommingEvent("getSolves", new String[]{
                        String.valueOf(answer),
                        String.valueOf(clientId),
                        team
                });
				isPlaying = false;

                //check for change question or change game
                gameManager.countDownGameReady(3);
                gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
                    @Override
                    public void ready() {
//                        Log.d("DEBUG","(coundownFinish)");
                        gc.sendGameEvent("numeric_again");


                    }
                });


			}else{
				gameManager.scoreManage(clientId, -1);
			}
			gameManager.printScoreToNumber();
		}

	}

	@Override
	public void onPlayerReady(int playerAmount) {
		if(cntPlayer == playerAmount){
			if(gameManager.getRound() <= 3) {
				if (gameManager.getNumber() == 1) {
                    Log.d("DEBUG","NEW ROUND");
					gc.sendGameEvent("numeric_newRound", new String[]{});
					gc.getGameLister().onIncommingEvent("getQuestion", new String[]{"ready"});
					gameManager.countDownGameReady(5);
					gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
						@Override
						public void ready() {
							gameManager.printReportRound();
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
