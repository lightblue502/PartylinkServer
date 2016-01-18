package com.partylinkserver;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.util.Log;

import pl.engine.GameContext;
import pl.engine.Utils;

public class GameCommunicationService extends Service implements GameCommunicationListener {
    private final IBinder binder = new GameCommunicationServiceBinder();
    private GameContext gc;
    public GameCommunicationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public GameContext startGameContext(String ip, int playerAmount){
        gc = GameContext.getInstance();
        gc.init(ip, 5566, playerAmount, this);
        return gc;
    }
    @Override
    public void onIncommingEvent(String event, String[] params) {
        Log.d("DEBUG_gcs.onIncome", "event--->" + event + ":" + "params" + params);
        sendEvent(event, params);
    }

    private void sendEvent(String name, String[] params){
        StringBuilder sb = new StringBuilder(name);
        sb.append('|');
        for(int i = 0; i < params.length; i++){
            sb.append(params[i]);
            if(i != params.length - 1){
                sb.append(',');
            }
        }
        Intent intent = new Intent("game-event");
        intent.putExtra("name", sb.toString());
        Log.d("DEBUG_gcs.sendEvent", "broadcasting game event: " + name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public class GameCommunicationServiceBinder extends Binder {
        GameCommunicationService getService() { return GameCommunicationService.this; }
    }

}
