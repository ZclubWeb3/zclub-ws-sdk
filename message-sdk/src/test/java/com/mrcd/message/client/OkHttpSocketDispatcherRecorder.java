package com.mrcd.message.client;

import com.mrcd.message.MessageDispatcher;
import com.mrcd.message.msg.JsonResponseMessage;

import org.junit.Assert;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * 记录每一个回调事件, 用于进行测试
 */
public class OkHttpSocketDispatcherRecorder extends MessageDispatcher {

    private final String name ;
    private JsonResponseMessage mLastMessage ;
    private final BlockingQueue<WebSocketEvent> events = new LinkedBlockingQueue<>();

    public OkHttpSocketDispatcherRecorder(String name) {
        this.name = name;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        System.out.println(this.toString() + "onOpen");
        enqueueEvent(webSocket, WebSocketEvent.OPEN, response.toString());
    }


    /**
     * binary 的消息也会转到 String 类型的消息中, 因此 binary 消息不需要覆写
     * @param webSocket
     * @param text
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        System.out.println(this.toString() +  " onMessage : " + text);
        enqueueEvent(webSocket, WebSocketEvent.MESSAGE, text);
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        enqueueEvent(webSocket, WebSocketEvent.CLOSING, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        enqueueEvent(webSocket, WebSocketEvent.CLOSED, reason);
    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        enqueueEvent(webSocket, WebSocketEvent.FAILURD, t.getMessage());
    }


    public WebSocket assertOpen() {
        WebSocketEvent event = nextEvent() ;
        Assert.assertNotNull(event.webSocket);
        return event.webSocket;
    }

    @Override
    public void onHandleMessage(String type, JsonResponseMessage jsonMessage) {
        mLastMessage = jsonMessage ;
    }

    /**
     * 验证消息交互
     * @param payload
     */
    public void assertTextMessage(String payload) {
        WebSocketEvent actual = nextEvent();
        Assert.assertEquals(actual.type, WebSocketEvent.MESSAGE);
        Assert.assertEquals(actual.msg, payload);
    }


    /**
     * 验证消息交互
     * @param payload
     */
    public void assertBinaryMessage(String payload) {
        WebSocketEvent actual = nextEvent();
        Assert.assertEquals(actual.type, WebSocketEvent.MESSAGE);
        Assert.assertEquals(actual.msg, payload);
    }


    private void enqueueEvent(WebSocket webSocket, int type, String msg) {
        events.add(new WebSocketEvent(webSocket, type, msg)) ;
    }


    /**
     * 获取最后一个事件
     * @return
     */
    private WebSocketEvent nextEvent() {
        try {
            WebSocketEvent event = events.poll(10, TimeUnit.SECONDS);
            if (event == null) {
                throw new AssertionError("Timed out waiting for event.");
            }
            return event;
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "OkHttpSocketDispatcherRecorder, name = " + name + " ";
    }

    public void assertClosing(int code, String msg) {
        WebSocketEvent actual = nextEvent();
        Assert.assertEquals(actual.type, WebSocketEvent.CLOSING);
    }

    public void assertClosed(int code, String msg) {
        WebSocketEvent actual = nextEvent();
        Assert.assertEquals(actual.type, WebSocketEvent.CLOSED);
    }

    public void assertExhausted() {
        Assert.assertTrue( events.isEmpty());
    }

    JsonResponseMessage getLastJsonMessage() {
        return mLastMessage;
    }

    public void makeClean() {
        events.clear();
        mLastMessage = null ;
    }

    /**
     * 事件类型
     */
    private static class WebSocketEvent {
        public static final int OPEN = 1 ;
        public static final int MESSAGE = 2 ;
        public static final int CLOSING = 3 ;
        public static final int CLOSED = 4 ;
        public static final int FAILURD = 5 ;

        final WebSocket webSocket ;
        final int type ;
        final String msg;

        public WebSocketEvent(WebSocket webSocket, int type, String msg) {
            this.webSocket = webSocket;
            this.type = type;
            this.msg = msg;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            WebSocketEvent that = (WebSocketEvent) o;

            if (type != that.type)
                return false;
            return msg != null ? msg.equals(that.msg) : that.msg == null;
        }

        @Override
        public int hashCode() {
            int result = type;
            result = 31 * result + (msg != null ? msg.hashCode() : 0);
            return result;
        }
    }
}
