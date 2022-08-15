package com.mrcd.message;

import android.os.Handler;
import android.text.TextUtils;
import com.mrcd.message.logger.MessageLogger;
import com.mrcd.message.protocol.MessageProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * OkHttp WebSocket client implementation
 */
public final class OkHttpWebSocketClient implements MrcdWebSocketClient {

    private Map<String, String> mHeadersMap = new HashMap<>();
    private String mWsUrl;
    private OkHttpClient mClient;
    private WebSocket mWebSocket;
    private MessageDispatcher mWebSocketDispatcher = null;

    private int mPingInternal = 60;
    private MessageLogger mMessageLogger;

    public OkHttpWebSocketClient(String wsUrl) {
        setWsUrl(wsUrl);
    }

    @Override
    public void setMessageDispatcher(MessageDispatcher listener) {
        mWebSocketDispatcher = listener;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        if (mHeadersMap != null && headers != null) {
            mHeadersMap.clear();
            mHeadersMap.putAll(headers);
        }
    }

    /**
     * set server url
     *
     * @param wsUrl
     */
    @Override
    public void setWsUrl(String wsUrl) {
        this.mWsUrl = wsUrl;
    }

    @Override
    public void connect() {
        log(" connect to " + mWsUrl);
        if (mWsUrl == null || TextUtils.isEmpty(mWsUrl)) {
            log("connect -> mWsUrl is null!  return ");
            return;
        }
        final Request.Builder builder = new Request.Builder();
        // set url
        builder.url(mWsUrl);
        // set headers
        if (mHeadersMap != null && mHeadersMap.size() > 0) {
            Set<String> keySet = mHeadersMap.keySet();
            for (String key : keySet) {
                builder.addHeader(key, mHeadersMap.get(key));
            }
        }
        final Request request = builder.build();
        // build OkHttpClient
        if (mClient == null) {
            mClient = new OkHttpClient.Builder().build();
        }

        if (mWebSocketDispatcher != null) {
            mWebSocketDispatcher.setMessageLogger(mMessageLogger);
        }

        if (mWebSocket != null) {
            mWebSocket.cancel();
        }

        // start websocket server
        mWebSocket = mClient.newWebSocket(request, new UiThreadMessageDispatcherWrapper(mWebSocketDispatcher));
    }

    @Override
    public boolean isConnected() {
        return mWebSocketDispatcher != null && mWebSocketDispatcher.isConnected();
    }

    @Override
    public void setMessageLogger(MessageLogger logger) {
        mMessageLogger = logger;
        if (mWebSocketDispatcher != null) {
            mWebSocketDispatcher.setMessageLogger(mMessageLogger);
        }
    }

    @Override
    public void setConnectionLostTimeout(int connectionLostTimeout) {
        mPingInternal = connectionLostTimeout;
    }

    @Override
    public boolean send(String message) {
        log("send : " + message);
        if (mWebSocket != null) {
            return mWebSocket.send(message);
        }
        return false;
    }

    String getWsUrl() {
        return mWsUrl;
    }

    MessageDispatcher getWebSocketDispatcher() {
        return mWebSocketDispatcher;
    }

    MessageLogger getMessageLogger() {
        return mMessageLogger;
    }

    @Override
    public void close() {
        if (mClient != null) {
            mClient.dispatcher().executorService().shutdown();
        }
        if (mWebSocket != null) {
            mWebSocket.close(MessageProtocol.CLOSE_CODE, MessageProtocol.BYE_BYE);
        }
        mWebSocketDispatcher = null;
        log("=====> OkHttpWebSocket close ");
    }

    private void log(String msg) {
        if (mMessageLogger != null) {
            mMessageLogger.onLog(isConnected(), msg);
        } else {
            System.out.println(msg);
        }
    }

    /**
     * 将 WebSocketListener 的所有回调函数都执行在ui线程, 该对象需要在UI线程上创建
     */
    private static class UiThreadMessageDispatcherWrapper extends WebSocketListener {

        private Handler mUiHandler = new Handler() ;
        private final WebSocketListener mDelegate;

        public UiThreadMessageDispatcherWrapper(WebSocketListener mDelegate) {
            super();
            this.mDelegate = mDelegate;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onOpen(webSocket, response);
                    }
                });
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onMessage(webSocket, text);
                    }
                });
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onMessage(webSocket, bytes);
                    }
                });
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onClosing(webSocket, code, reason);
                    }
                });
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onClosed(webSocket, code, reason);
                    }
                });
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            if (mDelegate != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDelegate.onFailure(webSocket, t, response);
                    }
                });
            }
        }
    } // end of UiThreadMessageDispatcherWrapper
}
