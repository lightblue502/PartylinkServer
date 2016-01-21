package com.partylinkserver;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import java.nio.LongBuffer;

import pl.engine.GameContext;
import pl.engine.GameShakeEngine;
import pl.engine.RegistrarEngine;
import pl.engine.Utils;

public class MainActivity extends GameActivity {
    private TextView tv;
    private String ip;
    private GameContext gc;
    private int playerAmount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialServiceBinding();
        Log.d("MAIN", "hello party link server");
        setContentView(R.layout.activity_main);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("DEBUG_MAIN","ipAddress --- >"+ip);
        tv = (TextView)findViewById(R.id.showIpText);
        tv.setText(ip);

        if(gc != null) {
            if (gc.getCurrentGameEngine() instanceof RegistrarEngine) {
                Intent intent = new Intent(this, RegistrarActivity.class);
                startActivity(intent);
            } else {
                Log.d("MAIN", "cannot get instance");
            }
        }else{
            Log.d("onResume", "Game Context is null");
        }
    }

    @Override
    public void onGameEvent(String event, String[] params) {
        Utils.debug("on Game event , MainActivity : " + event);
        if(event.equals("socketplayers_ready")){
            Utils.debug("socketpalyer ready");
            gc.begin();
            if (gc.getCurrentGameEngine() instanceof RegistrarEngine) {
                Intent intent = new Intent(this, RegistrarActivity.class);
                startActivity(intent);
            }else {
                Log.d("MAIN", "cannot get instance");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        gc = gcs.startGameContext(ip, playerAmount, this);

    }
}
