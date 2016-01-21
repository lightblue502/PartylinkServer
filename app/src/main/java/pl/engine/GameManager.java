package pl.engine;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class GameManager {
	private int round;
	private int number;
	private int currentRound = 1;
	private int currentNumber = 1;
    private boolean isGameEnd = false;
    private boolean isReady = false;
    private Handler customHandler = new Handler();
    private Runnable updateTimerThread;
    private Timer timer;
	private GameContext gc;
	private HashMap<String, Integer> teamA ;
	private HashMap<String, Integer> teamB ;
	public GameManager(ResultScore resultScore, GameContext gc, int number, int round){
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
	
	public void countDownGameReady(){
        Utils.debug("GAME READY ...");
        customHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Utils.debug("processs .....");
                processReady(true);
            }
        }, 5000);


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
	public void stopTimer(){
        timer.cancel();
		if(!isInRound())
            gc.getCurrentGameEngine().endEngine();

	}
	
	public void countdown(final String changeEvent,final int randomTime,final boolean isEvent){
		timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = randomTime;

            public void run() {
                Utils.print(i + " ");
                i--;
                if (i < 0) {
                    if (isInRound()) {
                        if (isEvent) {
                            gc.sendGameEvent(changeEvent);
                        }
                    }else{
                        gc.getCurrentGameEngine().endEngine();
                    }
                    Utils.debug("");
                    i = randomTime;
                    timer.cancel();
                }

            }
        }, 0, 1000);
	}

    public boolean isInRound(){
        currentNumber++;
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
