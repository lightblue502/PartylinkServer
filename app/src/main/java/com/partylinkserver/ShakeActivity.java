package com.partylinkserver;

import android.content.Context;
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
        wv.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        Log.d("SHAKE", "This is shake activity");
//        if(gc != null) {
//            gc.getCurrentGameEngine().toString();
//        }else{
//            Log.d("SHAKE", "gc is null");
//        }
    }

    @Override
    public void onGameEvent(String event, String[] params) {
        if(params ==  null){
            return;
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
        else if(event.equals("getQuestion")){
            String strs = "['";
            for(int i = 0; i < params.length; i++){
                strs += params[i];
                if(i != params.length - 1){
                    strs += "','";
                }else{
                    strs += "']";
                }
            }
            wv.loadUrl("javascript:getQuestion("+strs+")");
        }
    }
}
