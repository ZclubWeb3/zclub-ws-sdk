package com.mrcd.message;

import com.mrcd.message.logger.MessageLogger;
import com.mrcd.message.msg.JsonResponseMessage;
import com.mrcd.message.parser.JsonRespMessageParser;
import com.mrcd.message.protocol.MessageProtocol;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * fixme: 注意WebSocketListener的所有回调都是执行在子线程中
 * <p>
 *
 * 状态管理可以参考: https://github.com/Rabtman/WsManager/blob/master/wsmanager/src/main/java/com/rabtman/wsmanager/WsManager.java
 */
public abstract class MessageDispatcher extends WebSocketListener {
    /**
     * WebSocket 是否是连接状态
     */
    protected volatile boolean isConnected = false;
    /**
     * message logger
     */
    protected MessageLogger mMessageLogger;
    /**
     * 将报文解析为包含 json body 的消息实体
     */
    protected JsonRespMessageParser mJsonMsgParser = new JsonRespMessageParser();

    public MessageDispatcher() {
        super();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        isConnected = true;
        logcat("OnOpen : " + response);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        onMessage(webSocket, bytes != null ? bytes.utf8() : "");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // 将报文解析为包含 消息类型、json body 的消息实体
        final JsonResponseMessage jsonMessage = mJsonMsgParser.parse(text);
        logcat("onMessage : " + text);
        onHandleMessage(jsonMessage.getMethod(), jsonMessage);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(MessageProtocol.CLOSE_CODE, MessageProtocol.BYE_BYE);
        logcat("onClosing: " + code + "/" + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        isConnected = false;
        logcat("onClosed: " + code + "/" + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        isConnected = false ;
        onLoseConnection();
        logcat("onFailure: cause : " + t.getCause() + ", msg : " + t.toString());
    }

    /**
     * 断开连接之后的回调函数, 如果断开连接之后需要自动重连, 那么用户应该在子类实现这个方法
     */
    protected void onLoseConnection() {

    }

    /**
     * 子类必须实现该函数, 完成对各种message的逻辑处理
     *
     * @param method 消息类型
     * @param jsonMessage 消息的封装
     */
    public abstract void onHandleMessage(String method, JsonResponseMessage jsonMessage);

    /**
     * websocket 是否已经建立连接
     *
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    void setMessageLogger(MessageLogger logger) {
        mMessageLogger = logger;
    }

    MessageLogger getMessageLogger() {
        return mMessageLogger;
    }

    private void logcat(String s) {
        if (mMessageLogger != null) {
            mMessageLogger.onLog(isConnected, s);
        }
    }
}
