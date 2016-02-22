package pl.engine;

import android.content.Context;
import android.util.Log;

import com.partylinkserver.EndActivity;
import com.partylinkserver.QAActivity;
import com.partylinkserver.ResultActivity;
import com.partylinkserver.GameCommunicationListener;
import com.partylinkserver.NumericActivity;
import com.partylinkserver.RegistrarActivity;
import com.partylinkserver.ShakeActivity;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameContext implements CommunicationListener{
	private Context context;
	private int engineIndex;
	private int playerAmount;
    private GameCommunicationListener gameLister;
	private static final String[] BLANK_PARAMS = new String[0];
	private GameEngine currentGameEngine;
	private List<ResultScore> resultScoreLists = new ArrayList<ResultScore>();
	private List<GameEngine> engines = new ArrayList<GameEngine>();
	private List<Team> teams = new ArrayList<Team>();
	private CommunicationManager cm;

	public static GameContext instance;
	public static GameContext getInstance() {
		if (instance == null)
			instance = new GameContext();
		return instance;
	}

	public GameCommunicationListener getGameLister(){
		return gameLister;
	}
	private boolean isInitialized = false;
	public void init(String address, int port, int playerAmount, GameCommunicationListener gameListener, Context context) {
        this.gameLister = gameListener;
		this.context = context;
		if (isInitialized)
			return;

		isInitialized = true;

		this.playerAmount = playerAmount;
		engineIndex = 0;

		engines.add(new RegistrarEngine(this, playerAmount, "REGISTER" , RegistrarActivity.class,""));
		engines.add(new GameShakeEngine(this, playerAmount,"GAME SHAKE", ShakeActivity.class, "shake_start"));
		engines.add(new ResultEngine(this, playerAmount, "RESULT SCORE", ResultActivity.class, "result_start"));
		engines.add(new NumericEngine(this, playerAmount, "GAME NUMBER", NumericActivity.class, "numeric_start"));
		engines.add(new ResultEngine(this, playerAmount, "RESULT SCORE", ResultActivity.class, "result_start"));
        engines.add(new QAEngine(this, playerAmount, "GAME QA", context, QAActivity.class, "qa_start"));
        engines.add(new ResultEngine(this, playerAmount, "RESULT SCORE", ResultActivity.class, "result_start"));
        engines.add(new EndEngine(this, playerAmount, "END ENGINE", EndActivity.class, "end_start"));



		cm = new CommunicationManager(address , port, this);
		cm.start();
	}

	private GameContext(){

	}

	public void setPlayerAmount(int playerAmount){
		this.playerAmount = playerAmount;
	}
	public int getPlayerAmount(){
        return  playerAmount;
    }
    public void setEngineIndex(int engineIndex){
        this.engineIndex = engineIndex;
    }
    public Context getContext(){
        return context;
    }
	public void begin(){
		currentGameEngine = engines.get(engineIndex);
		Utils.debug("(GameContext) is begining --> CurrentGameengine is " + currentGameEngine.getName());
	}

	public void nextEngine(){

		currentGameEngine = engines.get(++engineIndex);
        Log.d("DEBUG", " " + currentGameEngine.getName());
//		sendGameEvent(currentGameEngine.getClientStart());
        changeToNextEngine();
        if(engineIndex > engines.size()-1 )
            engineIndex = 0;
	}

    public void changeToNextEngine(){
        currentGameEngine.startEngine();
        gameLister.onIncommingEvent("change_engine", new String[0]);
    }

    public void setCurrentGameEngine(GameEngine currentGameEngine){
        this.currentGameEngine = currentGameEngine;
    }

    public GameEngine getGameEngineByClass(Class selectClass){
        for ( GameEngine engine : engines){
            if(engine.getClass().getName().equals(selectClass.getName())){
                return engine;
            }
        }
        return  null;
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

	public void sendGameEvent(String event, String[] params){
		StringBuilder sb = new StringBuilder(event);
		sb.append('|');
		for(int i = 0; i < params.length; i++){
			sb.append(params[i]);
			if(i != params.length - 1){
				sb.append(',');
			}
		}
//        Log.d("DEBUG_gc.sendGameEvent","params --->"+sb.toString());
		cm.broadcastData(sb.toString());
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
	public GameEngine getCurrentGameEngine(){
		return currentGameEngine;
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



	public void onConnectionStateChanged(int clientId, int state){
		//make game decision
		if(state == STATE_CONNECTED){
			checkConnectionReady();

		}else if(state == STATE_DISCONNECTED){
			Utils.debug("Player DISCONNECTED");
			sendGameEvent("game_pause");
			gameLister.onIncommingEvent("game_pause", new String[]{});
		}
	}

	private boolean connectionReadyIssued = false;
	private void checkConnectionReady(){
		gameLister.onIncommingEvent("player_size", new String[]{String.valueOf(cm.getClients().size())} );
		if(!connectionReadyIssued && cm.getClients().size() == playerAmount){
			gameLister.onIncommingEvent("socketplayers_ready", new String[0]);
			connectionReadyIssued = true;
		}
	}
}
