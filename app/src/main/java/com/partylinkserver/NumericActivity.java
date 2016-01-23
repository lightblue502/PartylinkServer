package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class NumericActivity extends GameActivity {
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }


        wv = (WebView)findViewById(R.id.numericWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/numeric.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);
        wv.addJavascriptInterface(javaScriptInterface, "Android");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                //call engine that game numeric_game ready to play;
                sendGameEvent("NumericServerUI_Start");
            }
        });

        Log.d("DEBUG", "(NUMERIC) This is numeric activity");



    }
    @Override
    public void onGameEvent(String event, String[] params) throws ClassNotFoundException {
        if(params ==  null){
            return;
        }
        else if(event.equals("change_engine")){
            Intent intent = new Intent(this, Class.forName("com.partylinkserver." + gc.getCurrentGameEngine().getActivityName()));
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
        else if(event.equals("initPlayer")){
            //create new teams
            Log.d("DEBUG_initPlayer","at server UI --- "+params[0]);
            wv.loadUrl("javascript:initPlayer("+params[0]+")");
        }
    }
}
