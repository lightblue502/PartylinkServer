package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

import pl.engine.Utils;

public class BallActivity extends GameActivity {
    private TextView tv;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.ballWebView);

        wv.loadUrl("file:///android_asset/ball.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");

        Utils.debug("This is Ball Activity");
        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("ballUI_Start");
            }
            @Override
            public void backDoor(String num) {
                onGameEvent("back_Door", new String[]{num});
            }

            public void getDistance(int distance){
                sendGameEvent("getDistance", new String[]{String.valueOf(distance)});
            }

        });
    }

    @Override
    public void onGameEvent(String event, String[] params) {
        super.onGameEvent(event,params);
        Utils.debug("onGameEvent" + event);
        if(params ==  null){
            return;
        }else if (event.equals("initial_bomb")) {
            wv.loadUrl("javascript:getInitialBomb("+ Arrays.toString(params) + ")");
        }else if (event.equals("game_pause")) {
            wv.loadUrl("javascript:pause()");
//            super.changeToPauseFragment(R.id.fragment_container);
        }
        else if(event.equals("game_resume")){
            wv.loadUrl("javascript:resume()");
//            super.onSuicidePauseFragment();
        }
        else if(event.equals("change_engine")){
            wv.loadUrl("javascript:stopAudio()");
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else if(event.equals("getCurrentScore")){
            //params[0] -> 'A' : params[1] -> 'B'
            wv.loadUrl("javascript:getCurrentScore("+ params[0]+","+params[1]+")");
        }
        else if(event.equals("getWinRound")){
            //params[0] -> 'A' : params[1] -> 'B'
            wv.loadUrl("javascript:getWinRound(" + params[0]+","+ params[1]+")");
        }
        else if(event.equals("getRound")){
            wv.loadUrl("javascript:getRound("+params[0]+")");
        }
        else if(event.equals("initPlayer")){
            Log.d("DEBUG_initPLAYER", params[0] + "");
            wv.loadUrl("javascript:initPlayer("+params[0]+")");
        }
        else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown("+params[0]+")");
        }
        else if(event.equals("jump")){
            wv.loadUrl("javascript:jump()");
        }
        else if(event.equals("bomb")){
            wv.loadUrl("javascript:bomb()");
        }
        else if(event.equals("stop")){
            wv.loadUrl("javascript:stop()");
        }
        else if(event.equals("start")){
            wv.loadUrl("javascript:start()");
        }
        else if(event.equals("move")){
            wv.loadUrl("javascript:move("+params[0]+")");
        }
    }
}
