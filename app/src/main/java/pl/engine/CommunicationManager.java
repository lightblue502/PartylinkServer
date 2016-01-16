package pl.engine;

import android.util.Log;

import com.partylinkserver.GameCommunicationListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommunicationManager extends Thread {
	private static int currentClientId = 1;
	private GameCommunicationListener gameListener;
	private String address;
	private int port;
	private boolean closed = false;
	private BufferedReader reader;
	private HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();
	private CommunicationListener listener;
	
	private ExecutorService es = Executors.newFixedThreadPool(10);
	public CommunicationManager(String address, int port, CommunicationListener listener, GameCommunicationListener gameListener) {
		this.address = address;
		this.port = port;
		this.listener = listener;
		this.gameListener = gameListener;
	}

	public void run(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
			Utils.debug("Waiting for new connection");
			while(!closed){
				Socket socket = serverSocket.accept();

				String androidId = null;
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				Utils.debug("=============================================================");
//				Utils.debug("read line socket: " + reader);
//				Utils.debug("=============================================================");
				androidId = reader.readLine();
				Utils.debug("Android ID: " + androidId);
				if(!listener.existPlayerSocket(androidId)){
					int clientId = currentClientId++;
					ClientHandler handler = new ClientHandler(socket, clientId);
					es.execute(handler);
					Utils.debug("Client hanlder has been created for client id = " + clientId);
					clients.put(clientId, handler);
					listener.addSocketPlayer((new SocketPlayer(handler, androidId, es)));
					if(listener.socketPlayerReady()){
						gameListener.onIncommingEvent("socketplayers_ready", new String[0]);
					}
				}else{
					listener.editPlayerSocket(androidId, socket);
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(serverSocket != null){
			try{
				serverSocket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void close(){
		closed = true;
		for(ClientHandler handler : clients.values()){
			handler.close();
		}
	}
	
	public void onIncomingData(int clientId, String line){
		listener.onIncomingData(clientId, line);
	}
	
	public void broadcastData(String line){
		for(ClientHandler handler : clients.values()){
			handler.sendData(line);
		}
	}
	
	public void sendData(int clientId, String line){
		ClientHandler handler = clients.get(clientId);
		if(handler != null){
			handler.sendData(line);
		}
	}
	
	public class ClientHandler implements Runnable{
		private Socket socket;
		private PrintWriter writer;
		private BufferedReader reader;
		private boolean closed = false;
		private int clientId;
		
		public ClientHandler(Socket socket, int clientId) {			
			this.socket = socket;
			this.clientId = clientId;
		}

		public void sendData(String line){
			writer.println(line);
			writer.flush();
		}
		
		public void close(){
			closed = true;
		}
		
		public void setSocket(Socket socket){
			this.socket = socket;
		}

		@Override
		public void run() {
			try{
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = null;
				Utils.debug("read line commmu: " + reader);
				while(!closed && (line = reader.readLine()) != null){
					onIncomingData(clientId, line);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			clients.remove(clientId); //better handling
			
			try{
				if(!socket.isClosed())
					socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
		
	}
}
