package pl.engine;

public class Player {
	private int clientId;
	private String name;
	private String imagePath;
	
	public Player(int cliendId, String name, String imagePath) {
		super();
		this.clientId = cliendId;
		this.name = name;
		this.imagePath = imagePath;
	}
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	public int getCliendId() {
		return clientId;
	}

	public String getName() {
		return name;
	}

	public String getImagePath(){return  imagePath;}
	
}
