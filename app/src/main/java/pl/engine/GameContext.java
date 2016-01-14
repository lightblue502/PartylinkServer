package pl.engine;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GameContext implements CommunicationListener{
	private int engineIndex;
	private int playerAmount;
	private static final String[] BLANK_PARAMS = new String[0];
	private GameEngine currentGameEngine;
	private List<SocketPlayer> socketplayers = new ArrayList<SocketPlayer>();
	private List<ResultScore> resultScoreLists = new ArrayList<ResultScore>();
	private List<GameEngine> engines = new ArrayList<GameEngine>();
	private List<Team> teams = new ArrayList<Team>();
	private CommunicationManager cm;
	
	public GameContext(String address, int port, int playerAmount){
		this.playerAmount = playerAmount;
		engineIndex = 0;
		engines.add(new RegistrarEngine(this, playerAmount));
		engines.add(new GameShakeEngine(this, playerAmount,"GAME SHAKE"));
		engines.add(new NumericEngine(this, playerAmount, "GAME NUMBER"));
		engines.add(new EndEngine(this));
		cm = new CommunicationManager(address , port, this);
		cm.start();
	}
	
	public void setPlayerAmount(int playerAmount){
		this.playerAmount = playerAmount;
	}
	
	public void begin(){
		currentGameEngine = engines.get(engineIndex);
	}

	public void nextEngine(){
		currentGameEngine = engines.get(++engineIndex); 
		currentGameEngine.startEngine();
	}
	
	public void onIncomingData(int clientId, String line){
		if(currentGameEngine != null){
			int idx = line.indexOf('|');			
			String event = null;
			String[] params = null;
			if(idx > 0){
				event = line.substring(0, idx);
				params = line.substring(idx + 1).split(",");				
			}else if(idx < 0 && line.length() > 0){
				event = line;
				params = BLANK_PARAMS;
			}
			if(event != null){
				//LOG
				if(event.equals("register")){
					Utils.print("accepting line => " + line + " clientId : "+clientId);
					if(getPlayerByClientID(clientId) != null)
						Utils.debug("-- name: " + getPlayerByClientID(clientId).getName() + "\n");
				}
				
				currentGameEngine.onIncomingEvent(clientId, event, params);
			}
				
		}
	}
	
	public void sendGameEvent(String event){
		cm.broadcastData(event);
	}
	
	public void sendGameEvent(Player player, String event){
		sendGameEvent(player, event, BLANK_PARAMS);
	}
	
	public void sendGameEvent(Player player, String event, String[] params){
		StringBuilder sb = new StringBuilder(event);
        sb.append('|');
        for(int i = 0; i < params.length; i++){
            sb.append(params[i]);
            if(i != params.length - 1){
                sb.append(',');
            }
        }

        cm.sendData(player.getCliendId(), sb.toString());
	}

	public List<Team> getTeams() {
		return teams;
	}
	
	public void addTeams(Team team){
		teams.add(team);
	}
	
	public List<Player> getAllPlayer(){
		List<Player> allPlayers = new ArrayList<Player>();
		for (Team team : teams) {
			for (Player player : team.getPlayers()) {
				allPlayers.add(player);
			}
		}
		return allPlayers;
	}
	
	public Player getPlayerByClientID(int clientId) {
		for (Player player : getAllPlayer()) {
			if(player.getCliendId() == clientId){
				return player;
			}
		}
		return null;
	}
	
	public Team getTeamByClientId(int clientId){
		Player player = getPlayerByClientID(clientId);
		for(Team team: teams){
			if(team.isPlayer(player)){
				return team;
			}
		}
		return null;
	}
	
	public void addResultScore(ResultScore resultScore){
		resultScoreLists.add(resultScore);
	}
	
	public List<ResultScore> getResultScores(){
		return resultScoreLists;
	}

	@Override
	public boolean existPlayerSocket(String androidId) {
		for (SocketPlayer socketplayer : socketplayers) {
			Utils.debug("existSP:" + socketplayer.getAndroidId());
			if(socketplayer.getAndroidId().equals(androidId)){
				return true;
			}
		}
		return false;
	}
	
	public void editPlayerSocket(String androidId, Socket socket){
		for (SocketPlayer socketplayer : socketplayers) {
			Utils.debug("editSP:" + socketplayer.getAndroidId());
			if(socketplayer.getAndroidId().equals(androidId)){
				socketplayer.changeSocket(socket);
			}
		}
		
	}

	@Override
	public void addSocketPlayer(SocketPlayer socketplayer) {
		socketplayers.add(socketplayer);	
	}
	
}
