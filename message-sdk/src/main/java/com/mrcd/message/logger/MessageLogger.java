package com.mrcd.message.logger;

/**
 * WebSocket logger, 可以用于记录用户的日志信息, 便于后期根据日志查询用户的信息
 */
public interface MessageLogger {

    /**
     * all websocket message will be logged !!
     *
     * @param isConnected
     * @param message
     */
    void onLog(boolean isConnected, String message);
}
