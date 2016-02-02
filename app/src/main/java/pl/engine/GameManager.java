package pl.engine;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;


public class GameManager {
	private int round;
	private int number;
	private int currentRound = 1;
	private int currentNumber = 1;
    private boolean isGameEnd = false;
    private boolean isReady = false;
    private Handler customHandler = new Handler();
    private Runnable runnable;
	private GameContext gc;
	private HashMap<String, Integer> teamA ;
	private HashMap<String, Integer> teamB ;
    private ResultScore resultScore;
    private Handler handler = new Handler();
    private long count = 0;
    private boolean isStarted = false;
    private boolean wasStarted = false; //ในอดีตเคย start ป่าว
    public GameManager(GameContext gc){
        this.gc = gc;
    }
	public GameManager(ResultScore resultScore, GameContext gc, int number, int round){
        this.resultScore = resultScore;
		this.gc = gc; 
		this.round = round;
		this.number = number;
		teamA = new HashMap<String, Integer>();
		teamB = new HashMap<String, Integer>();
		teamA.put("currentScore", 0);
		teamA.put("winRound", 0);
		teamB.put("currentScore", 0);
		teamB.put("winRound", 0);
	}
	public void initPlayerstoUI(List<Team> teams){
		Log.d("DEBUG_init_PL", "send leaw");

		String strs = "[";
		for (Team team: teams) {
			strs += "[";
			for(Player player : team.getPlayers()){
				strs += "{'id':" + player.getCliendId();
				strs += ",'name':'" + player.getName();
				strs += "'},";
			}
			strs = strs.substring(0,strs.length()-1);
			strs += "],";
		}
		strs = strs.substring(0,strs.length()-1);
		strs += "]";
		Log.d("DEBUG_init_PL", strs + "");
		gc.getGameLister().onIncommingEvent("initPlayer", new String[]{strs});
	}

	public void countDownGameReady(int times){
        Utils.debug("GAME READY ...");
        customHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Utils.debug("processs .....");
                processReady(true);
            }
        }, 1000* times);


	}

    public void summaryScoreByGame(GameEngine gameEngine, List<Team> teams){
        printScoreToWIN();
        if(getTeamWin() != null){
            Team team = getTeamWin().equals("teamA")?teams.get(0) :teams.get(1);
            resultScore.setResult(team, gameEngine.getName());
            gc.addResultScore(resultScore);
        }
       resetWinRound();
    }
    public static interface OnGameReadyListener {
        public void ready();
    }
    private OnGameReadyListener listener;
    public void setOnGameReadyListener(OnGameReadyListener listener) {
        this.listener = listener;
    }

    public void processReady(boolean isEnd){
        Utils.debug("isEnd ..... " + isEnd);
        isReady = isEnd;
        if (listener != null) {
            listener.ready();
        }
    }

	public void scoreManage(int clientId, int score){
		Team team = gc.getTeamByClientId(clientId);
		if(team != null){
			if(team.getName().equals("teamA")){
				teamA.put("currentScore", teamA.get("currentScore") + score);
			}else if(team.getName().equals("teamB")){
				teamB.put("currentScore", teamB.get("currentScore") + score);
			}
		}
	}
	public int getNumber(){
		return currentNumber;
	}
	public int getRound(){
		return currentRound;
	}

	public void printReportRound(){
		Utils.debug("### ROUND : "+(currentRound) + " ####");
        Utils.debug("==============================================");
        Utils.debug("number : "+currentNumber + " ");
        Utils.debug("==============================================");
		gc.getGameLister().onIncommingEvent("getRound",new String[]{String.valueOf(currentRound)});
	}

	public void versus(){
		if(teamA.get("currentScore") >= teamB.get("currentScore")){
			teamA.put("winRound", teamA.get("winRound") + 1);
		}
		if(teamB.get("currentScore") >= teamA.get("currentScore")){
			teamB.put("winRound", teamB.get("winRound") + 1);
		}
	}
	
	public String getTeamWin(){
		if(teamA.get("winRound") > teamB.get("winRound")){
			return "teamA";
		}
		if(teamB.get("winRound") > teamA.get("winRound")){
			return "teamB";
		}
		return null;
	}

	public void resetWinRound() {
		teamA.put("winRound", 0);
		teamB.put("winRound", 0);
	}
	
	public void resetScore(){
		teamA.put("currentScore", 0);
		teamB.put("currentScore", 0);
	}

    public void tick(int times,String changeEvent){
        //normal round
        Utils.print(count + " ");
        count++;
        if(count > times){
            if (isInRound()) {
                plusNumber();
                if (!changeEvent.isEmpty()) {
                    gc.sendGameEvent(changeEvent);
                }
            }else{
                checkRound();
            }
        }
        Utils.debug("");
    }
    public  void resetTimer(){
        count = 0;
    }

//    public void printStage(){
//        Utils.debug(isStarted + " <-- isStarted");
//        Utils.debug(wasStarted + " <-- wasStarted");
//    }
    public boolean timerWasStarted(){
        return wasStarted;
    }
    public void setIsStarted(boolean value){
        isStarted = value;
    }
    public void startTimer(final int times, final String event){
        isStarted = true;
        wasStarted = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                if(isStarted) {
                    tick(times, event);
                    handler.postDelayed(this, 1000); //เรียกให้วน loop
                }
            }
        };
        handler.postDelayed(runnable, 1000); // execute runable ครั้งเดียว
    }
    public void stopTimer(){
        handler.removeCallbacks(runnable);
        isStarted = false;
        wasStarted = false;
    }

    public void checkRound(){
        //stop before normal round
        if(!isInRound()){
            gc.getCurrentGameEngine().endEngine();
            stopTimer();
        }

    }
    public void plusNumber(){
        currentNumber++;
    }

    public boolean isInRound(){
        Log.d("DEBUG_isInRound", "....");
        if(currentNumber > number){
            currentRound++;
            versus();
            resetScore();
            printScoreToWIN();
            currentNumber = 1;
        }
        return currentRound <= round;
    }


	public void printScoreToNumber() {
		gc.getGameLister().onIncommingEvent("getCurrentScore",new String[]{
                String.valueOf(teamA.get("currentScore")),
                String.valueOf(teamB.get("currentScore"))
        });
		Utils.debug("TEAM A : " + teamA.get("currentScore") + " ====== TEAM B : " + teamB.get("currentScore"));
	}
	public void printScoreToWIN() {
		gc.getGameLister().onIncommingEvent("getWinRound",new String[]{
				String.valueOf(teamA.get("winRound")),
				String.valueOf(teamB.get("winRound"))
		});
		Utils.debug("======================ToWIN========================");
		Utils.debug("TEAM A -> WIN : " + teamA.get("winRound") );
		Utils.debug("TEAM B -> WIN : " + teamB.get("winRound") );
		Utils.debug("==============================================");
	}
}
