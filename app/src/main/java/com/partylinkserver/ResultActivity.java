package com.partylinkserver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ResultActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialServiceBinding();
    }

    @Override
    public void onGameEvent(String event, String[] params) throws ClassNotFoundException {
        Log.d("DEBUG_inResultActivity","onGameEvent"+ event);
        if(params ==  null){
            return;
        }
        else if(event.equals("change_engine")){
            Intent intent = new Intent(this, gc.getCurrentGameEngine().getActivityClass());
            startActivity(intent);
        }
    }
}
