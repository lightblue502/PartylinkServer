package com.partylinkserver;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import pl.engine.Utils;

public class QAActivity extends GameActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        initialServiceBinding();
        Utils.debug("This is Question Answer Acrivity");
        tv = (TextView)findViewById(R.id.textView);
    }

    @Override
    public void onGameEvent(String event, String[] params) {
        Utils.debug("onGameEvent" + event);
        if(event.equals("qa_asking")){
            tv.setText(params[0]);
        }
    }
}
