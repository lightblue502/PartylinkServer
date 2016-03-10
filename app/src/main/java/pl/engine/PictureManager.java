package pl.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
//import java.util.zip.*;

/**
 * Created by lucksikalosuvalna on 3/7/16 AD.
 */
public class PictureManager extends Thread {
    private static int currentClientId = 0;
    private String mCurrentPhotoPath;
    private String address;
    private int port;
    private boolean closed = false;
    private BufferedReader reader;
    private HashMap<Integer, ClientPictureHandler> clients = new HashMap<Integer, ClientPictureHandler>();
    private CommunicationListener listener;


    private ExecutorService es = Executors.newFixedThreadPool(10);
    public PictureManager (String address, int port, CommunicationListener listener) {
        this.address = address;
        this.port = port;
        this.listener = listener;
    }
    public void run(){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
            Utils.debug("================= PhotoUploader Waiting for new connection =================");
            while(!closed){
                Socket socket = serverSocket.accept();
                Utils.debug("PictureUploader Socket accpet" + socket);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String initial_message = reader.readLine();	//expect ID=clientId
                Utils.debug("PictureManager initial_message: " + initial_message);
                int given_client_id = Integer.parseInt(initial_message.substring(3));
                Utils.debug(initial_message + " ====> " + given_client_id);
                ClientPictureHandler handler = given_client_id >= 0 ? clients.get(given_client_id) : null;
                if(handler == null){
                    handler = new ClientPictureHandler(socket, given_client_id);
                    es.execute(handler);
                    clients.put(given_client_id, handler);
                }
                Utils.debug("PictureUploader clientId receive: " + given_client_id);
                Utils.debug("PictureUploader handler clientId receive: " + handler.clientId);


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

    public class ClientPictureHandler implements Runnable{
        private Socket socket;
        private BufferedReader reader;
        private boolean closed = false;
        private int clientId;
        private DataInputStream input;

        public ClientPictureHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        public void close(){
            closed = true;
        }

        @Override
        public void run() {
            try{
                input = new DataInputStream(socket.getInputStream());
                Utils.debug("[PictureManager]input receive type: " + input.getClass());
                int len = 0;

                while(!closed && (len = input.readInt()) > 0){
                    Utils.debug("[PictureManager]" + len);
                    byte[] compressed_data = new byte[len];
                    input.readFully(compressed_data, 0, compressed_data.length);
                    Utils.debug("[PictureManager] Incoming event : " + len + " clientId: " + clientId);
                    saveToInternalStorage(decompressByteArray(compressed_data), clientId);
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

        public File saveToInternalStorage(byte[] uncompressed_data, int clientId) throws IOException, DataFormatException{
            Utils.debug("saveToInternalStorage PictureManager");
            Utils.debug("saveToInternalStorage Listener clientID: " + clientId);
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(uncompressed_data, 0, uncompressed_data.length);
            String imageFileName = Integer.toString(clientId);
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File path = new File(storageDir, "/PartyLinks/");
            if(!path.exists()){
                path.mkdirs();
            }
            File image = new File(path, "clientId" + imageFileName+".jpeg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(image);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.close();
            }

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file://" + image.getAbsolutePath();
            listener.setPicturePath(mCurrentPhotoPath, clientId);
            Utils.debug("Save to Internal Storage: " + mCurrentPhotoPath + " --- clientId: " + clientId);
            return image;
        }
    }
    public byte[] decompressByteArray(byte[] compressed_data) {
        Utils.debug("decompress: " + compressed_data.getClass().toString());
        Inflater inflater = new Inflater();
        inflater.setInput(compressed_data);

        try (final ByteArrayOutputStream out = new ByteArrayOutputStream(compressed_data.length)) {
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                out.write(buffer, 0, count);
            }
            return out.toByteArray();

        } catch (final IOException | DataFormatException e) {
            System.err.println("Decompression failed! Returning the compressed data...");
            e.printStackTrace();
            return compressed_data;
        }
    }
}
