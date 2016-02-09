package com.partylinkserver;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import pl.engine.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class PauseFragment extends Fragment {
    private WebView wv;

    public PauseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pause, container, false);
        // Inflate the layout for this fragment
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }

        wv = (WebView)view.findViewById(R.id.pause_view);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/pause.html");
        if(wv != null){
            Utils.debug(wv.toString());
            Utils.debug("load web view");
        }else {
            Utils.debug("can not load web view");
        }
        Utils.debug("pause Fragment");
        return view;
    }

}
