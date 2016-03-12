package pl.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultScore {
	private Team team;
	private String gameName;
	private int winRoundA;
	private int winRoundB;
	public ResultScore(){
		winRoundA = 0;
		winRoundB = 0;
	}
	
	public void setResult(Team team, String gameName,int winRoundA,int winRoundB){
		this.team = team;
		this.gameName = gameName;
		this.winRoundA = winRoundA;
		this.winRoundB = winRoundB;
	}

	public Team getTeam(){
		return team;
	}

	public String getGameName(){
		return gameName;
	}

	public int getWinRoundA(){ return winRoundA;};
	public int getWinRoundB(){ return winRoundB;};
	public void printResult(){
		Utils.debug("TEAM : "+ team.getName() + " ----"+ gameName);
	}

	public static String getTeamWin(List<ResultScore> resultScores){
		int counterTeamA = 0;
		int counterTeamB = 0;
		for(ResultScore resultScore: resultScores){
			if(resultScore.getTeam().getName().equals("teamA")){
				counterTeamA++;
			}else if(resultScore.getTeam().getName().equals("teamB")){
				counterTeamB++;
			}
		}

		if(counterTeamA == counterTeamB){
			return "Draw";
		}else if(counterTeamA > counterTeamB){
			return "teamA";
		}
		return "teamB";
	}
	
}