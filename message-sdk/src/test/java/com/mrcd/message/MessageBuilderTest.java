package com.mrcd.message;

import com.mrcd.message.client.OkHttpClientTest;
import com.mrcd.message.logger.MessageLogger;
import com.mrcd.message.msg.JsonResponseMessage;

import org.junit.After;
import org.junit.Test;

public class MessageBuilderTest extends BaseMsgTest {

    private MessageSDK mMsgSdk ;

    @After
    public void tearDown() throws Exception {
        mMsgSdk.close();
        mMsgSdk = null ;
    }

    @Test
    public void testBuildMessageSDK() {
        MessageDispatcher dispatcher = new MessageDispatcher() {
            @Override
            public void onHandleMessage(String type, JsonResponseMessage jsonMessage) {

            }
        } ;

        MessageLogger logger = new MessageLogger() {
            @Override
            public void onLog(boolean isConnected, String message) {

            }
        } ;

        mMsgSdk = new MessageSDK.Builder(OkHttpClientTest.ECHO_SERVER_WS_UR).messageDispatcher(dispatcher).messageLogger(logger).build();
        notNull(mMsgSdk);
        isTrue( mMsgSdk.mClient instanceof OkHttpWebSocketClient);
        OkHttpWebSocketClient client = (OkHttpWebSocketClient) mMsgSdk.mClient ;
        // 验证
        eq(OkHttpClientTest.ECHO_SERVER_WS_UR, client.getWsUrl());
        // dispatcher
        eq(dispatcher, client.getWebSocketDispatcher());
        // dispatcher 中的logger
        eq(logger, client.getWebSocketDispatcher().getMessageLogger());
        eq(logger, client.getMessageLogger());
    }


    @Test
    public void testBuildMessageSDKWithNull() {
        mMsgSdk = new MessageSDK.Builder(OkHttpClientTest.ECHO_SERVER_WS_UR).messageDispatcher(null).messageLogger(null).build();
        notNull(mMsgSdk);
        isTrue( mMsgSdk.mClient instanceof OkHttpWebSocketClient);
        OkHttpWebSocketClient client = (OkHttpWebSocketClient) mMsgSdk.mClient ;
        // 验证
        eq(OkHttpClientTest.ECHO_SERVER_WS_UR, client.getWsUrl());
        notNull( client.getWebSocketDispatcher());
        // dispatcher 中的logger
        notNull( client.getWebSocketDispatcher().getMessageLogger());
        notNull( client.getMessageLogger());
    }


    @Test
    public void testBuildMessageSDKWithEmptyWebSocketUrl() {
        mMsgSdk = new MessageSDK.Builder(null).messageDispatcher(null).messageLogger(null).build();
        notNull(mMsgSdk);
        isTrue( mMsgSdk.mClient instanceof OkHttpWebSocketClient);
        OkHttpWebSocketClient client = (OkHttpWebSocketClient) mMsgSdk.mClient ;
        // 验证
        isNullStr(client.getWsUrl());
        notNull( client.getWebSocketDispatcher());
        // dispatcher 中的logger
        notNull( client.getWebSocketDispatcher().getMessageLogger());
        notNull( client.getMessageLogger());

        mMsgSdk.connect();
    }
}
