package com.partylinkserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import pl.engine.GameContext;
import pl.engine.GameShakeEngine;
import pl.engine.Main;

public class RegistrarActivity extends GameActivity{
    private GameContext gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialServiceBinding();
        gc = GameContext.getInstance();
        setContentView(R.layout.activity_registrar);

        Log.d("REGIS", "This is RegistraActivity");
        if(gc != null){
            Log.d("REGIS", "gc != null");
        }else{
            Log.d("REGIS", "gc == null");
        }
//        if(gc.getCurrentGameEngine() instanceof GameShakeEngine){
//            Intent intent = new Intent(this, ShakeActivity.class);
//            startActivity(intent);
//        };
    }


    @Override
    public void onGameEvent(String event, String[] params) {
        Log.d("REGIS", "event: " + event);
        if(event.equals("change_engine")){
//            Intent intent = new Intent(this, ShakeActivity.class);
            Intent intent = new Intent(this, NumericActivity.class);
            startActivity(intent);
        }
    }
}
