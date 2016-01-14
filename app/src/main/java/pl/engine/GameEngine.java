package pl.engine;

public abstract class GameEngine {
	protected GameContext gc;
	private String name;
	public GameEngine(GameContext gc){
		this.gc = gc;		
	}
	
	public GameEngine(GameContext gc, String name){
		this.gc = gc;		
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	public abstract void startEngine();
	public abstract void endEngine();
	public abstract void onPlayerReady(int playerAmount);
	public abstract void onIncomingEvent(int clientId, String event, String[] params);
	
}
