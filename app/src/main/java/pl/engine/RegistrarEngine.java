package pl.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RegistrarEngine extends GameEngine{
	private int playerAmount;
	private int numOfTeam;
	private List<Team> teams = gc.getTeams();
	private List<Player> players = new ArrayList<Player>();
	public RegistrarEngine(GameContext gc, int playerAmount, String name, Class activityClass){
		super(gc,name,activityClass);
		this.playerAmount = playerAmount;
		this.numOfTeam = 2;
		int maxPlayerAmount =  (playerAmount+1)/numOfTeam;
		Team teamA = new Team("teamA",maxPlayerAmount);
		Team teamB = new Team("teamB",maxPlayerAmount);
		gc.addTeams(teamA);
		gc.addTeams(teamB);
	}
	
	
	
	public void onIncomingEvent(int clientId, String event, String[] params){		
		if("register".equals(event)){
			Player player = new Player(clientId, params[0]);
			players.add(player);
			gc.sendGameEvent(player, "register_ok");
			
		}
		
		onPlayerReady(playerAmount);
		
	}
	
	public void randomTeam(List<Player> players){
		List<Player> playerHaveTeam = new ArrayList<Player>();
		do{
			for (Player player : players) {
				if(!playerHaveTeam.contains(player)){
					int randomTeam = new Random().nextInt(teams.size());
					Team team = teams.get(randomTeam);
					if(team.getPlayerAmount() <  team.getMaxPlayerAmount()){
						playerHaveTeam.add(player);
						team.addPlayer(player);
					}
				}
				Utils.debug("random Team");
			}
		}while(players.size() > playerHaveTeam.size());
		sendTeamToClient();
	}
	
	public void sendTeamToClient() {
		for (Team team : teams) {
			for (Player player : team.getPlayers()) {
				gc.sendGameEvent(player, "your_team", new String[]{team.getName()} );
			}
		}
	}
	
	public void onPlayerReady(int playerAmount){
		if(players.size() == playerAmount){
			randomTeam(players);
			endEngine();
		}
	}
	
	@Override
	public void startEngine() {
		
	}

	@Override
	public void endEngine() {
		gc.sendGameEvent("qa_start");
		gc.nextEngine();
	}
	
}
