package com.partylinkserver;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class NumericActivity extends AppCompatActivity {
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric);

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
}
