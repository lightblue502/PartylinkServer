package com.partylinkserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import pl.engine.Utils;

public class QAActivity extends GameActivity {
    private TextView tv;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.qaWebView);
        tv = (TextView)findViewById(R.id.textView);

        wv.loadUrl("file:///android_asset/qa.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");

        Utils.debug("This is Question Answer Acrivity");
        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("qaUI_Start");
            }
        });
    }

    @Override
    public void onGameEvent(String event, String[] params) {
        Utils.debug("onGameEvent" + event);
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
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            startActivity(intent);
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
        else if(event.equals("qa_asking")){
            Log.d("DEBUG_ask_inQAAct ", params[0]+" , "+params[1]);
            wv.loadUrl("javascript:ask(" + params[0]+",'"+ params[1]+"')");
        }
        else if(event.equals("qa_correct")){
            Log.d("DEBUG_correct_inQAAct ", params[0]);
            wv.loadUrl("javascript:correct(" + params[0]+")");
        }
        else if(event.equals("qa_wrong")){
            Log.d("DEBUG_wrong_inQAAct ", params[0]);
            wv.loadUrl("javascript:wrong(" + params[0]+")");
        }
        else if(event.equals("getCountdown")){
            wv.loadUrl("javascript:getCountdown('"+params[0]+"')");
        }
    }
}
