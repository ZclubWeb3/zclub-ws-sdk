package com.mrcd.message;

import com.mrcd.message.logger.DefaultMessageLogger;
import com.mrcd.message.logger.MessageLogger;
import com.mrcd.message.msg.JsonResponseMessage;
import com.mrcd.message.msg.MrcdMessage;

import java.util.Map;

/**
 * A facade class of MessageSDK
 */
public class MessageSDK {

    final MrcdWebSocketClient mClient ;

    public MessageSDK(MrcdWebSocketClient client) {
        this.mClient = client;
    }

    /**
     * 使用新的socket连接
     */
    public void connect() {
        if ( mClient != null ) {
            mClient.connect() ;
        }
    }

    public void setWsUrl(String wsUrl) {
        if ( mClient != null ) {
            mClient.setWsUrl(wsUrl);
        }
    }

    public void setHeaders(Map<String, String> headers) {
        if (mClient != null) {
            mClient.setHeaders(headers);
        }
    }

    public boolean isConnected() {
        if (mClient != null) {
            return mClient.isConnected();
        }
        return false;
    }

    /**
     * send message
     * @param message
     */
    public boolean sendMessage(String message) {
        if ( mClient != null ) {
            return mClient.send(message);
        }
        return false;
    }

    /**
     * 根据 message 类型构造特定的消息格式之后写入到 WebSocket Client
     * @param message
     * @param <T>
     */
    public <T> boolean sendMessage(MrcdMessage<T> message) {
        if ( mClient != null ) {
            return mClient.send(message.buildRequest().toString()) ;
        }
        return false;
    }

    public void close() {
        if ( mClient != null ) {
            mClient.close();
        }
    }

    /**
     * MessageSDK
     */
    public static class Builder {

        private final String mWsUrl ;
        private MrcdWebSocketClient mClient ;
        private MessageDispatcher mMessageDispatcher;
        private MessageLogger mLogger ;
        private Map<String, String> mHeaders;

        public Builder(String wsUrl) {
            this.mWsUrl = wsUrl;
        }

        public Builder headers(Map<String, String> headers) {
            this.mHeaders = headers;
            return this;
        }

        public Builder websocketClient(MrcdWebSocketClient client) {
            this.mClient = client;
            return this;
        }

        public Builder messageLogger(MessageLogger mLogger) {
            this.mLogger = mLogger;
            return this;
        }

        public Builder messageDispatcher(MessageDispatcher mMessageListener) {
            this.mMessageDispatcher = mMessageListener;
            return this;
        }

        public MessageSDK build() {
            if ( mLogger == null ) {
                mLogger = new DefaultMessageLogger() ;
            }
            if ( mClient == null ) {
                mClient = new OkHttpWebSocketClient(mWsUrl);
            }

            if ( mMessageDispatcher == null ) {
                mMessageDispatcher = new DefaultMessageDispatcher() ;
            }
            mClient.setHeaders(mHeaders);
            mClient.setMessageDispatcher(mMessageDispatcher);
            mClient.setMessageLogger(mLogger);
            return new MessageSDK(mClient) ;
        }
    }


    /**
     * default dispatcher
     */
    private static class DefaultMessageDispatcher extends MessageDispatcher {

        @Override
        public void onHandleMessage(String type, JsonResponseMessage jsonMessage) {
            System.out.println("Attention !!! DefaultMessageDispatcher will do nothing for Message SDK ");
        }
    }
}
