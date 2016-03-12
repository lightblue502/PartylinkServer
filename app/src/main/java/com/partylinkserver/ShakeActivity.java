package com.partylinkserver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import pl.engine.GameShakeEngine;

public class ShakeActivity extends GameActivity {
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        initialServiceBinding();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.shakeWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/shake.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");
        Log.d("SHAKE", "This is shake activity");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("shakeUI_Start");
            }

            @Override
            public void backDoor(String num) {
                onGameEvent("back_Door", new String[]{num});
            }

            @Override
            public void sendScore(int score) {

            }
        });
    }

    @Override
    public void onGameEvent(String event, String[] params){
        super.onGameEvent(event,params);

        if(params ==  null){
            return;
        }
        else if (event.equals("game_pause")) {
            super.changeToPauseFragment(R.id.fragment_container);
        }
        else if(event.equals("game_resume")){
            super.onSuicidePauseFragment();
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
            Log.d("DEBUG_initPLAYER",params[0]+"");
            wv.loadUrl("javascript:initPlayer("+params[0]+")");
        }
        else if(event.equals("shake")){
            Log.d("DEBUG_shake_inShakeAct ", params[0]+" , "+params[1]);
            wv.loadUrl("javascript:shake(" + params[0]+",'"+ params[1]+"')");
        }
        else if(event.equals("resetStage")){
            wv.loadUrl("javascript:resetStage()");
        }else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
