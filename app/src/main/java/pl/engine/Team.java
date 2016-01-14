package pl.engine;

import java.util.ArrayList;
import java.util.List;

public class Team{
	private String name;
	private List<Player> players = new ArrayList<Player>();
	private int maxPlayerAmount;
	public Team(String name, int maxPlayerAmount){
		this.maxPlayerAmount = maxPlayerAmount;
		this.name = name;
	}
	
	public void addPlayer(Player player){
		players.add(player);
		Utils.debug("new player => " + player.getName());
	}
	
	public int getMaxPlayerAmount() {
		return maxPlayerAmount;
	}
	
	public void printPlayers(){
		Utils.debug(" ---- " + name + "----");
		for (Player player : players) {
			Utils.debug(player.getName());
		}
	}

	public int getPlayerAmount() {
		return players.size();
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isPlayer(Player player){
		return players.contains(player);
	}
	
}
