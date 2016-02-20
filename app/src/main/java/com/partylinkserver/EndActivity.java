package com.partylinkserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class EndActivity extends GameActivity {
private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.endWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/endGame.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");
        Log.d("end", "This is end activity");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("endUI_Start");
            }

            @Override
            public void backDoor(String num) {
                sendGameEvent("back_Door",new String[]{num});
            }
        });
    }

    @Override
    public void onGameEvent(String event, String[] params){
        super.onGameEvent(event,params);
        Log.d("DEBUG_inEndActivity","onGameEvent"+ event);
        if(params ==  null){
            return;
        }else if(event.equals("change_engine")){
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            startActivity(intent);
        }else if(event.equals("getResultScores")){
            Log.d("DEBUG_getResultScores",params[0]+"");
            wv.loadUrl("javascript:getResultScores("+params[0]+")");
        }else if(event.equals("playerConfirm")){
            Log.d("DEBUG","PlayerConfirm "+params[0]);
            wv.loadUrl("javascript:playerConfirm("+ params[0]+")");
        }else if(event.equals("initPlayer")){
            Log.d("DEBUG_initPLAYER",params[0]+"");
            wv.loadUrl("javascript:initPlayer("+params[0]+")");
        }else if(event.equals("getTeamWin")){
            Log.d("DEBUG_getTeamWin",params[0]+"");
            wv.loadUrl("javascript:getTeamWin('"+params[0]+"')");
        }else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
