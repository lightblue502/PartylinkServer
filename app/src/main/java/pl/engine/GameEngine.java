package pl.engine;

public abstract class GameEngine {
	protected GameContext gc;
	private String name;
	private Class activityClass;
	public GameEngine(GameContext gc){
		this.gc = gc;		
	}
	
	public GameEngine(GameContext gc, String name, Class activityClass){
		this.gc = gc;		
		this.name = name;
		this.activityClass = activityClass;
	}

	public String getName(){
		return name;
	}
	public Class getActivityClass(){
		return activityClass;
	}
	public abstract void startEngine();
	public abstract void endEngine();
	public abstract void onPlayerReady(int playerAmount);
	public abstract void onIncomingEvent(int clientId, String event, String[] params);
	
}
