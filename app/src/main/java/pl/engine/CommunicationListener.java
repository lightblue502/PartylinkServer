package pl.engine;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

public interface CommunicationListener {
	public void onIncomingData(int clientId, String line);
	public void addSocketPlayer(SocketPlayer socketplayer);
	public boolean existPlayerSocket(String android_id);
	public void editPlayerSocket(String androidId, Socket socket);
}
