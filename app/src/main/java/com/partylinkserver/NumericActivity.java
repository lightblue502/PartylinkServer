package com.partylinkserver;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        wv.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        Log.d("DEBUG", "(NUMERIC) This is numeric activity");
    }
    @Override
    public void onGameEvent(String event, String[] params) {
//        Log.d("DEBUG","(onGameEvent--Numeric)"+event + " "+params);
        if(params ==  null){
            return;
        }
        else if(event.equals("getCurrentScore")){
            wv.loadUrl("javascript:getCurrentScore(" + params[0]+",'A')");
            wv.loadUrl("javascript:getCurrentScore("+params[1]+",'B')");
        }
        else if(event.equals("getQuestion")){
//            Log.d("DEBUG_inGetQuestion", "hello params" + params[0] + params[1] + params[2]);
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
        else if(event.equals("getRound")){
            wv.loadUrl("javascript:getRound("+params[0]+")");
        }
    }
}
