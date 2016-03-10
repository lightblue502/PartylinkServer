package pl.engine;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.zip.DataFormatException;

public interface CommunicationListener {
	public static final int STATE_CONNECTED = 1;
	public static final int STATE_DISCONNECTED = 2;

	public void onIncomingData(int clientId, String line);
	public void onConnectionStateChanged(int clientId, int state);
	public void setPicturePath(String path,int clientId);
}
