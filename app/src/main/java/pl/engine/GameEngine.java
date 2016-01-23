package pl.engine;

public abstract class GameEngine {
	protected GameContext gc;
	private String name;
	private String activityName;
	public GameEngine(GameContext gc){
		this.gc = gc;		
	}
	
	public GameEngine(GameContext gc, String name, String activityName){
		this.gc = gc;		
		this.name = name;
		this.activityName = activityName;
	}

	public String getName(){
		return name;
	}
	public String getActivityName(){
		return activityName;
	}
	public abstract void startEngine();
	public abstract void endEngine();
	public abstract void onPlayerReady(int playerAmount);
	public abstract void onIncomingEvent(int clientId, String event, String[] params);
	
}
