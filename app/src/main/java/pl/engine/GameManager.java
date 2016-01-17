package pl.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameManager {
	private int round;
	private int number;
	private int currentRound = 0;
	private int currentNumber = 0;
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
	
	public void gameReady(){
		Utils.debug("GAME READY ...");
		countdown("", 3, false);
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
	public int getCurrentScoreByTeam(char team){
		if(team == 'A'){
			return teamA.get("currentScore");
		}else {
			return teamB.get("currentScore");
		}
	}
	public int getWinRoundByTeam(char team){
		if(team == 'A'){
			return teamA.get("winRound");
		}else{
			return teamB.get("winRound");
		}
	}
	
	public boolean checkGameRound(){
		currentNumber++;
		Utils.debug("==============================================");
		Utils.debug("number : "+currentNumber + " ");
		Utils.debug("==============================================");
		if(currentNumber > number){
			Utils.debug("### ROUND : "+(currentRound+1) + " ####");
			currentNumber = 0;
			currentRound++;
			
			// calc winRound
			versus();
			printScoreToWIN();
			resetScore();
			
		}
		return currentRound < round;
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
	}
	
	public void countdown(final String changeEvent,final int randomTime,final boolean isEvent){
		timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = randomTime;
            public void run() {
            	Utils.print(i + " ");
            	i--;
                if (i< 0){
                	if(isEvent)
                		gc.sendGameEvent(changeEvent);
        			Utils.debug("");
        			i = randomTime;
        			timer.cancel();
                }
                	
            }
        }, 0, 1000);
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
