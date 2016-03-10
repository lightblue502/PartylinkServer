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
	private String address;
	private int port;
	private boolean closed = false;
	private BufferedReader reader;
	private HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();
	private CommunicationListener listener;
	
	private ExecutorService es = Executors.newFixedThreadPool(10);
	public CommunicationManager(String address, int port, CommunicationListener listener) {
		this.address = address;
		this.port = port;
		this.listener = listener;
	}

	public void run(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
			Utils.debug("================= Server Waiting for new connection =================");
			while(!closed){
				Socket socket = serverSocket.accept();
				Utils.debug("Socket accpet" + socket);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String initial_message = reader.readLine();	//expect ID=x
				int given_client_id = Integer.parseInt(initial_message.substring(3));
				Utils.debug(initial_message + " ====> " + given_client_id);
				ClientHandler handler = given_client_id >= 0 ? clients.get(given_client_id) : null;
				if(handler == null){
					given_client_id = currentClientId++;
				}else{
//					listener.onConnectionStateChanged(given_client_id, CommunicationListener.STATE_DISCONNECTED);
					handler.close();
				}
				handler = new ClientHandler(socket, given_client_id);
				es.execute(handler);
				Utils.debug("Client hanlder has been created for client id = " + given_client_id);
				clients.put(given_client_id, handler);
				listener.onConnectionStateChanged(given_client_id, CommunicationListener.STATE_CONNECTED);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(serverSocket != null){
			try{
				Utils.debug("================== CLOSE SOCKET ==================");
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
			Utils.debug("handler has sendData line: " + line + " to ClientId " + clientId);
			handler.sendData(line);
		}
	}

	public HashMap<Integer, ClientHandler> getClients(){
		return clients;
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

		@Override
		public void run() {
			try{
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = null;
				writer.println("ID=" + clientId);
				writer.flush();

				while(!closed && (line = reader.readLine()) != null){
					Utils.debug("Incoming event : " + line);
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
