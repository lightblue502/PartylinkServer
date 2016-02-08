package pl.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultScore {
	private Team team;
	private String gameName;
	
	public ResultScore(){
		
	}
	
	public void setResult(Team team, String gameName){
		this.team = team;
		this.gameName = gameName;
	}

	public Team getTeam(){
		return team;
	}

	public String getGameName(){
		return gameName;
	}
	
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
		return counterTeamA > counterTeamB ? "teamA": "teamB";
	}
	
}