package pl.engine;

public class Player {
	private int clientId;
	private String name;
	
	public Player(int cliendId, String name) {
		super();
		this.clientId = cliendId;
		this.name = name;
	}

	public int getCliendId() {
		return clientId;
	}

	public String getName() {
		return name;
	}
	
}
