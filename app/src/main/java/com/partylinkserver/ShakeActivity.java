package com.partylinkserver;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pl.engine.GameShakeEngine;

public class ShakeActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        Log.d("SHAKE", "This is shake activity");
//        if(gc != null) {
//            gc.getCurrentGameEngine().toString();
//        }else{
//            Log.d("SHAKE", "gc is null");
//        }
    }
}
