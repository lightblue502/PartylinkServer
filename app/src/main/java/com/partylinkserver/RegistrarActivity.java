package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import pl.engine.GameContext;
import pl.engine.GameShakeEngine;
import pl.engine.Main;
import pl.engine.RegistrarEngine;

public class RegistrarActivity extends GameActivity{
    private GameContext gc;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialServiceBinding();
        gc = GameContext.getInstance();
        setContentView(R.layout.activity_registrar);

        Log.d("REGIS", "This is RegistraActivity");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        wv = (WebView) findViewById(R.id.registerWebview);
        wv.loadUrl("file:///android_asset/gameLobby.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);
        wv.addJavascriptInterface(javaScriptInterface, "Android");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                //call engine that game numeric_game ready to play;
                sendGameEvent("RegisterServerUI_Start");
            }

            @Override
            public void backDoor(String num) {

            }

            @Override
            public void sendScore(int score) {

            }
        });
    }

    @Override
    public void onGameEvent(String event, String[] params){
        super.onGameEvent(event,params);
        Log.d("REGIS", "event: " + event);
        if(event.equals("change_engine")){
            wv.loadUrl("javascript:stopAudio()");
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (event.equals("game_pause")) {
            super.changeToPauseFragment(R.id.fragment_container);
        }else if(event.equals("game_resume")){
            super.onSuicidePauseFragment();
        }else if(event.equals("setPlayer")){
            //params[0] -> 'ClientId' : params[1] -> 'name' : params[2] -> icon path
            wv.loadUrl("javascript:setPlayer("+ params[0]+",'"+params[1]+"','"+params[2]+"')");
        }else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
