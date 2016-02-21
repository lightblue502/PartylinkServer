package com.partylinkserver;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import java.nio.LongBuffer;

import pl.engine.GameContext;
import pl.engine.GameShakeEngine;
import pl.engine.RegistrarEngine;
import pl.engine.Utils;

public class MainActivity extends GameActivity {
//    private TextView tv;
    private String ip;
    private WebView wv;
    private GameContext gc;
    private int playerAmount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialServiceBinding();
        Log.d("MAIN", "hello party link server");
        setContentView(R.layout.activity_main);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("DEBUG_MAIN","ipAddress --- >"+ip);
//        tv = (TextView)findViewById(R.id.showIpText);
//        tv.setText(ip);

        //webView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.mainWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/home.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");
        Log.d("DEBUG_result", "This is result activity");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("homeUI_Start");
            }
            @Override
            public void backDoor(String num) {
                onGameEvent("back_Door",new String[]{num});
            }
        });


        //endwebview
        new CountDownTimer(1000 * 5, 1000) {

            public void onTick(long millisUntilFinished) {
                wv.loadUrl("javascript:loading("+(millisUntilFinished / 1000)+")");
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
//                mTextField.setText("done!");
                wv.loadUrl("javascript:getIpAddress('"+ip+"')");
            }
        }.start();




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
        super.onGameEvent(event,params);
        Utils.debug("on Game event , MainActivity : " + event);
        if(event.equals("socketplayers_ready")){
            Utils.debug("socketpalyer ready");
            final Context that = this;
            new CountDownTimer(1000 * 2, 1000) {

                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    gc.begin();
                    if (gc.getCurrentGameEngine() instanceof RegistrarEngine) {
                        wv.loadUrl("javascript:stopAudio()");
                        Intent intent = new Intent(that, RegistrarActivity.class);
                        startActivity(intent);
                    }else {
                        Log.d("MAIN", "cannot get instance");
                    }
                }
            }.start();
        }else if(event.equals("getIpAddress")){
            Log.d("DEBUG_getIpAddress", params[0] + "");
            wv.loadUrl("javascript:getIpAddress('"+params[0]+"')");

        }else if(event.equals("player_size")){
            wv.loadUrl("javascript:getPlayerSize("+params[0]+","+playerAmount+ ")");
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
        gc = gcs.startGameContext(ip, playerAmount,this);

    }
}
