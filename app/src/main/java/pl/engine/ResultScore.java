package pl.engine;

import java.util.ArrayList;

public class ResultScore {
	private Team team;
	private GameEngine game;
	
	public ResultScore(){
		
	}
	
	public void setResult(Team team, GameEngine game){
		this.team = team;
		this.game = game;
	}
	
	public void printResult(){
		Utils.debug("TEAM : "+ team.getName() + " ----"+ game.getName());
	}
	
}