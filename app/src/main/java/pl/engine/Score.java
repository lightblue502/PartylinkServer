package pl.engine;

import java.util.ArrayList;
import java.util.List;

public class Score {
	private int currentScore;
	private int winRound;
	private ArrayList<Integer> teamScore;
	
	public Score(){
		this.currentScore = 0;
		this.winRound = 0;
		teamScore = new ArrayList<Integer>();
	}
	
	public void addScore(){
		currentScore++;
	}
	
	public void win(){
		winRound++;
	}
	
	public int getScore(){
		return currentScore;
	}
	
	public void resetScore(){
		currentScore = 0;
	}
	
	public int getWinRound(){
		return winRound;
	}
	
	public void resetWinRound(){
		winRound = 0;
	}
	
	public void addTeamScore(int score){
		teamScore.add(score);
	}
	
	
//	public void versus(Team teamB){
//		if(this.currentScore >= teamB.getScore()){
//			this.win();
//		}
//		if(teamB.getScore() >= this.currentScore){
//			teamB.win();
//		}
//	}
//	
//	public void summaryTeamScore(Team teamB){
//		if(this.winRound > teamB.getWinRound()){
//			addTeamScore(1);
//			teamB.addTeamScore(0);
//		}else if(this.winRound < teamB.getWinRound()){
//			addTeamScore(0);
//			teamB.addTeamScore(1);
//		}else {
//			addTeamScore(1);
//			teamB.addTeamScore(1);
//		}
//	}
	
	
}
