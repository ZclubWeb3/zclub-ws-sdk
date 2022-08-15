package com.mrcd.message.client;

import com.mrcd.message.BaseMsgTest;
import com.mrcd.message.msg.JsonResponseMessage;
import com.mrcd.utils.AssetsUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.ByteString;

/**
 * 通过记录的形式判断
 */
public class OkHttpClientTest extends BaseMsgTest {

    public static final String ECHO_SERVER_WS_UR = "ws://echo.websocket.org/" ;

    /**
     * A network that resolves only one IP address per host. Use this when testing route selection
     * fallbacks to prevent the host machine's various IP addresses from interfering.
     */
    private static final Dns SINGLE_INET_ADDRESS_DNS = hostname -> {
        List<InetAddress> addresses = Dns.SYSTEM.lookup(hostname);
        return Collections.singletonList(addresses.get(0));
    };
    private static final ConnectionPool connectionPool = new ConnectionPool();
    private static final Dispatcher dispatcher = new Dispatcher();

    @Rule
    public final MockWebServer webServer = new MockWebServer();
    private final Random random = new Random(0);

    private OkHttpClient client = defaultClient().newBuilder()
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(chain -> {
                Response response = chain.proceed(chain.request());
                // Ensure application interceptors never see a null body.
                notNull(response.body());
                return response;
            })
            .build();

    OkHttpSocketDispatcherRecorder serverListener = new OkHttpSocketDispatcherRecorder("client") ;
    OkHttpSocketDispatcherRecorder clientListener = new OkHttpSocketDispatcherRecorder("server") ;


    @Before
    public void setUp() throws Exception {
        serverListener.makeClean();
        clientListener.makeClean();
        isFalse( clientListener.isConnected() );
    }

    /**
     * 测试发送文字消息
     */
    @Test
    public void textMessage() {
        // mock server
        webServer.enqueue(new MockResponse().withWebSocketUpgrade(serverListener));
        WebSocket webSocket = newWebSocket(clientListener);

        // 两端都已经建立了连接
        clientListener.assertOpen();
        WebSocket server = serverListener.assertOpen();

        isTrue( clientListener.isConnected() );
        // client 发送消息
        webSocket.send("Hello, WebSockets!");
        // 验证server 是否收到了消息
        serverListener.assertTextMessage("Hello, WebSockets!");

        closeWebSockets(webSocket, server);

        sleep(500);
        // 断开了连接
        isFalse( clientListener.isConnected() );
    }


    /**
     * 测试发送json文字消息, server send to client
     */
    @Test
    public void textJsonMessage() {
        // mock server
        webServer.enqueue(new MockResponse().withWebSocketUpgrade(serverListener));
        WebSocket webSocket = newWebSocket(clientListener);

        // 两端都已经建立了连接
        clientListener.assertOpen();
        WebSocket server = serverListener.assertOpen();

        isTrue( clientListener.isConnected() );
        String loginResp = AssetsUtil.readJsonString("apply_for_mic.json") ;
        // server 发送消息
        server.send(loginResp);
        // 验证 client 是否收到了消息
        clientListener.assertTextMessage(loginResp);

        JsonResponseMessage jsonIncomeMessage = clientListener.getLastJsonMessage() ;
        // 验证数据解析部分
        notNull(jsonIncomeMessage);
        eq("apply_for_mic", jsonIncomeMessage.getMethod());
        eq("uuid", jsonIncomeMessage.getId());

        closeWebSockets(webSocket, server);
        sleep(500);
        // 断开了连接
        isFalse( clientListener.isConnected() );
    }



    /**
     * 测试发送文字消息
     */
    @Test
    public void binaryMessage() {
        // mock server
        webServer.enqueue(new MockResponse().withWebSocketUpgrade(serverListener));
        WebSocket webSocket = newWebSocket(clientListener);

        // 两端都已经建立了连接
        clientListener.assertOpen();
        WebSocket server = serverListener.assertOpen();

        isTrue( clientListener.isConnected() );
        // client 发送消息
        webSocket.send(ByteString.encodeUtf8("Hi-Binary"));
        // 验证server 是否收到了消息
        serverListener.assertBinaryMessage("Hi-Binary");

        closeWebSockets(webSocket, server);
        sleep(500);
        // 断开了连接
        isFalse( clientListener.isConnected() );
    }


    private void closeWebSockets(WebSocket clientSocket, WebSocket server) {
        server.close(1001, "");
        clientListener.assertClosing(1001, "");
        clientSocket.close(1000, "");
        serverListener.assertClosing(1000, "");
        clientListener.assertClosed(1001, "");
        serverListener.assertClosed(1000, "");
        clientListener.assertExhausted();
        serverListener.assertExhausted();
    }

    private RealWebSocket newWebSocket(WebSocketListener clientListener) {
        HttpUrl url = webServer.url("/");

        Request request = new Request.Builder().get().url(url).build() ;
        RealWebSocket webSocket = new RealWebSocket(request, clientListener, random, client.pingIntervalMillis());
        webSocket.connect(client);
        return webSocket;
    }

    public static OkHttpClient defaultClient() {
        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .dispatcher(dispatcher)
                .dns(SINGLE_INET_ADDRESS_DNS) // Prevent unexpected fallback addresses.
                .build();
    }
}
