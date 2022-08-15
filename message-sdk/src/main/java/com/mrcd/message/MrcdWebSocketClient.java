package com.mrcd.message;

import com.mrcd.message.logger.MessageLogger;

import java.util.Map;

/**
 * web socket client interface
 */
public interface MrcdWebSocketClient {

    /**
     * set websocket message listener
     *
     * @param listener
     */
    void setMessageDispatcher(MessageDispatcher listener);

    /**
     * set headers
     * @param headers
     */
    void setHeaders(Map<String, String> headers);

    /**
     * set server url
     * @param wsUrl
     */
    void setWsUrl(String wsUrl);

    /**
     * connect to the websocket server
     */
    void connect();

    /**
     * return true if connect success
     * @return return true if connect success
     */
    boolean isConnected();

    /**
     * set message logger
     *
     * @param logger
     */
    void setMessageLogger(MessageLogger logger);

    /**
     * Setter for the interval checking for lost connections
     * A value lower or equal 0 results in the check to be deactivated
     *
     * @param connectionLostTimeout connectionLostTimeout the interval in seconds
     */
    void setConnectionLostTimeout(int connectionLostTimeout);

    /**
     * send message to websocket
     *
     * @param message
     */
    boolean send(String message);

    /**
     * close websocket connection
     */
    void close();
}
