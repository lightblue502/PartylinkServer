package pl.engine;

public abstract class GameEngine {
	protected GameContext gc;
	private String name;
	private Class activityClass;
	private String clientStart;
	protected boolean gamePaused = false;
	public GameEngine(GameContext gc){
		this.gc = gc;		
	}
	
	public GameEngine(GameContext gc, String name, Class activityClass, String clientStart){
		this.gc = gc;		
		this.name = name;
		this.activityClass = activityClass;
		this.clientStart = clientStart;
	}

	public String getName(){
		return name;
	}
	public Class getActivityClass(){
		return activityClass;
	}
	public String getClientStart(){
		return clientStart; 
	}
	public abstract void startEngine();
	public abstract void endEngine();
	public abstract void onPlayerReady(int playerAmount);
	public abstract void onIncomingEvent(int clientId, String event, String[] params);
	protected boolean onPlayerResumeReady(int playerAmount, int count){
		if(playerAmount == count){
			return true;
		}
		return false;
	}
	public void pauseGame(){
		Utils.debug("==================== GAME PAUSE ===============");
		gamePaused = true;
	}
	public void resumeGame(){
		Utils.debug("==================== GAME RESUME ===============");
		gamePaused = false;
	}
}
