package pl.engine;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RegistrarEngine extends GameEngine{
	private int playerAmount;
	private int numOfTeam;
	private GameManager gameManager;
	private List<Team> teams = gc.getTeams();
	private List<Player> players = new ArrayList<Player>();
	public RegistrarEngine(GameContext gc, int playerAmount, String name, Class activityClass,String clientStart){
		super(gc,name,activityClass,clientStart);
		this.playerAmount = playerAmount;
		this.numOfTeam = 2;
		int maxPlayerAmount =  (playerAmount+1)/numOfTeam;
		Team teamA = new Team("teamA",maxPlayerAmount);
		Team teamB = new Team("teamB",maxPlayerAmount);
		gc.addTeams(teamA);
		gc.addTeams(teamB);
		this.gameManager = new GameManager(gc);
	}
	
	
	
	public void onIncomingEvent(int clientId, String event, String[] params){		
		if("register".equals(event)){
			Player player = new Player(clientId, params[0]);
			sendPlayerToUI(player);
			players.add(player);
			gc.sendGameEvent(player, "register_ok");
		}
		onPlayerReady(playerAmount);
		
	}

	public void sendPlayerToUI(Player player){
		String cliendId = String.valueOf(player.getCliendId());
		String name = String.valueOf(player.getName());
		gc.getGameLister().onIncommingEvent("setPlayer",new String[]{cliendId, name});
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
			gameManager.countDownGameReady(5);
			gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
				@Override
				public void ready() {
					Log.d("DEBUG", "call endEngine");
					endEngine();
				}
			});
		}
	}
	
	@Override
	public void startEngine() {
		
	}

	@Override
	public void endEngine() {
		// gc.sendGameEvent("shake_start");
		gc.nextEngine();
	}
	
}
