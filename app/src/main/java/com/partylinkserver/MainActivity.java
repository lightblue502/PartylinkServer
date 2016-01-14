package com.partylinkserver;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.engine.GameContext;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN", "hello party link server");
        setContentView(R.layout.activity_main);


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        tv = (TextView)findViewById(R.id.showIpText);
        tv.setText(ip);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });


    }
    public void process(){
        GameContext gc = new GameContext(ip, 5566, 2);
        gc.begin();
    }

}
