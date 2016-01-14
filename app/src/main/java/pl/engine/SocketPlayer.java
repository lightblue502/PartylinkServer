package pl.engine;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import pl.engine.CommunicationManager.ClientHandler;

public class SocketPlayer {
	private ExecutorService es;
	private ClientHandler handler = null;
	private String androidId = null;
	public SocketPlayer(ClientHandler handler, String androidId, ExecutorService es) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.androidId = androidId;
		this.es = es;
	}
	
	public String getAndroidId(){
		return androidId;
	}
	
	public void changeSocket(Socket socket){
		handler.setSocket(socket);
		es.execute(handler);
	}
}
