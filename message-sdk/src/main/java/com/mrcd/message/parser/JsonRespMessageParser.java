package com.mrcd.message.parser;

import com.mrcd.message.msg.JsonResponseMessage;

import org.json.JSONObject;

/**
 * WebSocket json message parser
 */
public class JsonRespMessageParser implements MessageParser<JsonResponseMessage>  {

    @Override
    public JsonResponseMessage parse(String messageBody) {
        if ( messageBody == null || messageBody.length() == 0 ) {
            return new JsonResponseMessage(new JSONObject());
        }

        JSONObject jsonObject = null ;
        try {
            jsonObject = new JSONObject(messageBody) ;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new JsonResponseMessage(jsonObject != null ? jsonObject : new JSONObject());
    }
}
