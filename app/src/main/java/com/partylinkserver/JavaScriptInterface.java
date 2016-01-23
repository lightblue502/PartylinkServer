package com.partylinkserver;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


public class JavaScriptInterface {
    private Context context;
    public static JavaScriptInterface instance;

    public static JavaScriptInterface getInstance() {
        if (instance == null)
            instance = new JavaScriptInterface();
        return instance;
    }
    public void init(Context context){
        this.context = context;
    }
    @JavascriptInterface
    public void makeToast(String message){
        //html ติดต่อไปหา android
        Log.d("JSI", "making toast from native code");
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    public static interface onUiReadyListener {
        public void ready();
    }
    private onUiReadyListener listener;

    public void setOnGameReadyListener(onUiReadyListener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void onUiReady(){
        Log.d("DEBUG_webCallAndroid", "onUiReady");
        if (listener != null) {
            listener.ready();
        }
    }

}
