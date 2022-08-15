package com.mrcd.message.logger;

/**
 * Default Message Logger
 */
public class DefaultMessageLogger implements MessageLogger {

    @Override
    public void onLog(boolean isConnected, String message) {
        System.out.println("isConnected : " + isConnected + ", message : " + message);
    }
}
