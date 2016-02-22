package com.partylinkserver;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import pl.engine.BlankEngine;
import pl.engine.EndEngine;
import pl.engine.GameContext;
import pl.engine.GameEngine;
import pl.engine.GameShakeEngine;
import pl.engine.NumericEngine;
import pl.engine.QAEngine;
import pl.engine.ResultEngine;
import pl.engine.Utils;

public abstract class GameActivity extends AppCompatActivity {
    protected GameCommunicationService gcs;
    protected GameContext gc = GameContext.getInstance();
    protected boolean bound = false;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver broadcastReceiver;

    public void initialServiceBinding(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                GameCommunicationService.GameCommunicationServiceBinder binder = (GameCommunicationService.GameCommunicationServiceBinder) service;
                gcs = binder.getService();
                bound = true;
                GameActivity.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        Intent intent = new Intent(this, GameCommunicationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String event = intent.getStringExtra("event");
                String[] params = intent.getStringArrayExtra("params");
                onGameEvent(event,  params);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("game-event"));
    }

    public void sendGameEvent(String event){
        gc.onIncomingData(0, event);
    }

    public void sendGameEvent(String event, String[] params){
        StringBuilder line = new StringBuilder(event);
        line.append('|');
        for(int i = 0; i < params.length; i++){
            line.append(params[i]);
            if(i != params.length - 1){
                line.append(',');
            }
        }
        gc.onIncomingData(0, line.toString());
    }

    public void onGameEvent(String event, String[] params){
        if(event.equals("back_Door")){
            int select = Integer.parseInt(params[0]);
            GameEngine currentGameEngine= null;
            gc.setEngineIndex(0);
            int playerAmount = gc.getPlayerAmount();
            gc.getCurrentGameEngine().endEngine();
            Log.d("backdoor press : ", params[0] + "");
            if(select == 1){
                currentGameEngine = new GameShakeEngine(gc, playerAmount,"GAME SHAKE", ShakeActivity.class, "shake_start");
            }else if(select == 2){
                currentGameEngine = new NumericEngine(gc, playerAmount, "GAME NUMBER", NumericActivity.class, "numeric_start");
            }else if(select == 3){
                currentGameEngine = new QAEngine(gc, playerAmount, "GAME QA", gc.getContext(), QAActivity.class, "qa_start");
            }else if(select == 8){
                currentGameEngine = new ResultEngine(gc, playerAmount, "RESULT SCORE", ResultActivity.class, "result_start");
            }else if(select == 9){
                currentGameEngine = new EndEngine(gc, playerAmount, "END ENGINE", EndActivity.class, "end_start");
            }else{
                currentGameEngine = new BlankEngine(gc, playerAmount, "BLANK ENGINE", BlankActivity.class, "blank_start");
            }

            if (currentGameEngine != null) {
                gc.setCurrentGameEngine(currentGameEngine);
                gc.changeToNextEngine();
            }
            // param[0] goes from 0-9
        }
    }


    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    protected  void onServiceConnected(){

    }
    protected void changeToPauseFragment(int container){
        PauseFragment fragment = new PauseFragment();
        addFragment(fragment, container);

    }
    public void addFragment(Fragment fragment, int container){
        FragmentTransaction ft = getFragmentManager().beginTransaction().add(container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
    protected void onSuicidePauseFragment() {
        getFragmentManager().popBackStack();
    }
}
