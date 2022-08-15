package com.mrcd.message.msg;

import android.text.TextUtils;
import com.mrcd.message.protocol.MessageProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * 消息基类, 将目标类型转成对应协议的字符串
 *
 * @param <T> 目标类型
 */
public abstract class MrcdMessage<T> {
    /**
     * 消息id
     */
    protected final String id;
    /**
     * 协议类型, 目前有 含有chatroom 群聊类型
     */
    protected final String mType;

    /**
     * msg发送者
     */
    protected final String mSenderId;

    /**
     * msg接收者
     */
    protected final String mReceiverId;

    /**
     * msg接收者-多个接收者
     */
    protected final String[] mReceiverIds;

    /**
     * room id
     */
    protected final String mRoomId;

    /**
     * 请求的方法, 相当于 http 中的path
     */
    protected String mMethod;

    /**
     * 解析data之后的具体实体类型
     */
    protected T mTarget = null;

    /**
     * 具体的消息类型，如 text, image...etc
     */
    protected String mMsgType = "";

    public MrcdMessage(String id, String type, String roomId, String senderId, String receiverId,  String method) {
        this.id = id;
        this.mType = type;
        this.mSenderId = senderId;
        this.mReceiverId = receiverId;
        this.mReceiverIds = new String[0];
        this.mRoomId = roomId;
        this.mMethod = method;
    }


    public MrcdMessage(String roomId, String senderId, String receiverId,  String method, T mTarget) {
        this.id = UUID.randomUUID().toString();
        this.mType = MessageProtocol.CHATROOM_TYPE;
        this.mSenderId = senderId;
        this.mReceiverId = receiverId;
        this.mReceiverIds = new String[0];
        this.mRoomId = roomId;
        this.mMethod = method;
        this.mTarget = mTarget;
    }

    public MrcdMessage(String roomId, String senderId, String[] receiverIds,  String method, T mTarget) {
        this.id = UUID.randomUUID().toString();
        this.mType = MessageProtocol.CHATROOM_TYPE;
        this.mSenderId = senderId;
        this.mReceiverId = "";
        this.mReceiverIds = receiverIds;
        this.mRoomId = roomId;
        this.mMethod = method;
        this.mTarget = mTarget;
    }

    /**
     * 将实体对象转换为 web socket 协议的报文
     *
     * @return
     */
    public JSONObject buildRequest() {
        final JSONObject jsonObject = new JSONObject();
        putInJson(jsonObject, MessageProtocol.TYPE, MessageProtocol.CHATROOM_TYPE);
        putInJson(jsonObject, MessageProtocol.MSG_ID, this.id);
        putInJson(jsonObject, MessageProtocol.SENDER, this.mSenderId);
        putInJson(jsonObject, MessageProtocol.ROOM_ID, this.mRoomId);
        putInJson(jsonObject, MessageProtocol.MSG_METHOD, this.getMethod());
        putInJson(jsonObject, MessageProtocol.TIMESTAMP, System.currentTimeMillis());
        // receivers or receiver
        if (this.mReceiverIds != null && this.mReceiverIds.length > 0) {
            JSONArray jsonArray = new JSONArray();
            for (String receiverId : mReceiverIds) {
                jsonArray.put(receiverId);
            }
            putInJson(jsonObject, MessageProtocol.RECEIVERS, jsonArray);
        } else if (!TextUtils.isEmpty(this.mReceiverId)) {
            putInJson(jsonObject, MessageProtocol.RECEIVER, this.mReceiverId);
        }
        // target
        if (this.mTarget != null) {
            putInJson(jsonObject, MessageProtocol.DATA, mTarget);
        }
        return jsonObject;
    }

    protected static void putInJson(JSONObject jsonObject, String key, Object value) {
        try {
            if (jsonObject != null) {
                jsonObject.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public String getReceiverId() {
        return mReceiverId;
    }

    public String getRoomId() {
        return mRoomId;
    }

    public T getTarget() {
        return mTarget;
    }

    public String getMethod() {
        return mMethod;
    }

    public String getType() {
        return mType;
    }

    public String getMsgType() {
        return mMsgType;
    }
}
