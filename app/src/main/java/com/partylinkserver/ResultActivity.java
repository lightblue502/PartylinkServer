package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class ResultActivity extends GameActivity {
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.resultWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/result.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java
        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");
        Log.d("DEBUG_result", "This is result activity");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("resultUI_Start");
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
        Log.d("DEBUG_inResultActivity","onGameEvent"+ event);
        if(params ==  null){
            return;
        }else if (event.equals("game_pause")) {
            super.changeToPauseFragment(R.id.fragment_container);
        }
        else if(event.equals("game_resume")){
            super.onSuicidePauseFragment();
        }else if(event.equals("change_engine")){
            wv.loadUrl("javascript:stopAudio()");
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if(event.equals("getResultScores")){
            Log.d("DEBUG_getResultScores",params[0]+"");
            wv.loadUrl("javascript:getResultScores("+params[0]+")");
        }else if(event.equals("playerConfirm")){
            Log.d("DEBUG","PlayerConfirm "+params[0]);
            wv.loadUrl("javascript:playerConfirm("+ params[0]+")");
        }else if(event.equals("initPlayer")){
            Log.d("DEBUG_initPLAYER",params[0]+"");
            wv.loadUrl("javascript:initPlayer("+params[0]+")");
        }else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
