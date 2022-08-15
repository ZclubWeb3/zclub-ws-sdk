package com.mrcd.message.msg;

import com.mrcd.message.protocol.MessageProtocol;

import org.json.JSONObject;

import java.util.UUID;

/**
 * 客户端发送给后端的消息类型, 数据都存储在 data 中 ( mTarget )
 */
public class JsonRequestMessage extends MrcdMessage<JSONObject> {

    public JsonRequestMessage(String method) {
        this("", "", method);
    }

    public JsonRequestMessage(String roomId, String senderId, String method) {
        this(roomId,  senderId, "", method);
    }

    public JsonRequestMessage(String roomId, String senderId, String receiverId, String method) {
        super(UUID.randomUUID().toString(), MessageProtocol.CHATROOM_TYPE, roomId, senderId, receiverId,  method);
        this.mTarget = new JSONObject();
    }

    public JsonRequestMessage(String roomId, String senderId, String[] receiverIds, String method) {
        super(roomId, senderId, receiverIds, method, new JSONObject());
    }

    /**
     * 将数据存放到 data 中
     * @param key
     * @param value
     * @return
     */
    public JsonRequestMessage putInData(String key, Object value) {
        if (mTarget != null) {
            putInJson(mTarget, key, value);
        }
        return this ;
    }

    @Override
    public String toString() {
        return "JsonRequestMessage{method='" + mMethod + '\'' + ", id='" + id + '\'' + '}';
    }
}
