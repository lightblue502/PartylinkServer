package com.partylinkserver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class BlankActivity extends GameActivity {
private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        initialServiceBinding();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        wv = (WebView)findViewById(R.id.blankWebView);
//        wv.loadData("<h3> Hello world</h3>", "text/html","UTF-8");
        wv.loadUrl("file:///android_asset/home.html");
        wv.getSettings().setJavaScriptEnabled(true); // ทำให้ java script รันได้ใน java

        JavaScriptInterface javaScriptInterface = JavaScriptInterface.getInstance();
        javaScriptInterface.init(this);

        wv.addJavascriptInterface(javaScriptInterface, "Android");
        Log.d("end", "This is blank activity");

        javaScriptInterface.setOnGameReadyListener(new JavaScriptInterface.onUiReadyListener() {
            @Override
            public void ready() {
                sendGameEvent("blankUI_Start");
            }
            @Override
            public void backDoor(String num) {
                onGameEvent("back_Door",new String[]{num});
            }
        });
    }

    @Override
    public void onGameEvent(String event, String[] params){
        super.onGameEvent(event,params);
        Log.d("DEBUG_inBlankActivity","onGameEvent"+ event);
        if(params ==  null){
            return;
        }else if(event.equals("change_engine")) {
            wv.loadUrl("javascript:stopAudio()");
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if(event.equals("setDisplay")){
            wv.loadUrl("javascript:setDisplay('" + params[0]+"')");
        }
    }
}
