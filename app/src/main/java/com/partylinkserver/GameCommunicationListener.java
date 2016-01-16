package com.partylinkserver;

/**
 * Created by lucksikalosuvalna on 1/15/2016 AD.
 */
public interface GameCommunicationListener {
    public void onIncommingEvent(String event, String[] params);
}
