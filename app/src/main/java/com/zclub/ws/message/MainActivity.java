package com.zclub.ws.message;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.mrcd.message.MessageDispatcher;
import com.mrcd.message.MessageSDK;
import com.mrcd.message.logger.DefaultMessageLogger;
import com.mrcd.message.msg.JsonRequestMessage;
import com.mrcd.message.msg.JsonResponseMessage;
import com.zclub.ws.message.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String ECHO_SERVER_WS_UR = "ws://echo.websocket.org/" ;
    private ActivityMainBinding binding;
    private MessageSDK mMessageSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        configMessageSDK();
        binding.sendBtn.setOnClickListener((v) -> mMessageSDK.sendMessage(new JsonRequestMessage("zclub_room_01", "user_007",
                "user_friend_01", "apply_mic")));
    }

    private void configMessageSDK() {
        mMessageSDK = new MessageSDK.Builder(ECHO_SERVER_WS_UR)
                .messageDispatcher(new ZClubMessageDispatcher())
                .messageLogger(new DefaultMessageLogger())
                .build();
        mMessageSDK.connect();
    }

    /**
     * zclub MessageDispatcher
     */
    private class ZClubMessageDispatcher extends MessageDispatcher {

        @Override
        public void onHandleMessage(String method, JsonResponseMessage jsonMessage) {
            final String message = jsonMessage.getTarget().toString();
            Log.e("", "### onHandleMessage: " + message);
            // dispatch message with method
            runOnUiThread(() -> binding.wsMsgLoggerTv.append(method + " : " + message));
        }
    }
}