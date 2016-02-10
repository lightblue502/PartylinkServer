package pl.engine;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import pl.engine.CommunicationManager.ClientHandler;

public class SocketPlayer {
	private ExecutorService es;
	private Integer clientId;
	private ClientHandler handler = null;
	private String androidId = null;
	public SocketPlayer(Integer clientId, ClientHandler handler, String androidId, ExecutorService es) {
		// TODO Auto-generated constructor stub
		this.clientId = clientId;
		this.handler = handler;
		this.androidId = androidId;
		this.es = es;
	}
	
	public String getAndroidId(){
		return androidId;
	}
	
	public boolean isChangeSocket(Socket socket){
		handler.setSocket(socket);
		es.execute(handler);
		Utils.debug("Change Socket : " + handler.getSocketString());
		return true;
	}

	public ClientHandler getHandler(){
		return handler;
	}
	public Integer getClientId(){
		return clientId;
	}
}
