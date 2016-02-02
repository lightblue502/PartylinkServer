package pl.engine;

import java.util.ArrayList;

public class ResultScore {
	private Team team;
	private String gameName;
	
	public ResultScore(){
		
	}
	
	public void setResult(Team team, String gameName){
		this.team = team;
		this.gameName = gameName;
	}
	
	public void printResult(){
		Utils.debug("TEAM : "+ team.getName() + " ----"+ gameName);
	}
	
}