package com.mrcd.message.msg;

import com.mrcd.message.protocol.MessageProtocol;

import org.json.JSONObject;

/**
 * 后端返回的Json 数据类型, 数据都存储在 data 中 ( mTarget )
 * <p>
 */
public class JsonResponseMessage extends MrcdMessage<JSONObject> {

    protected final int mStatusCode;
    protected JSONObject mRawMsgData;

    @Deprecated
    public JsonResponseMessage(int stCode, String msgId, String msgType,
                               String senderId, String roomId, String method, JSONObject body) {
        super(msgId, msgType, roomId, senderId, "",  method);
        mStatusCode = stCode;
        mTarget = body;
    }

    public JsonResponseMessage(JSONObject json) {
        super(optString(json, MessageProtocol.MSG_ID),
                optString(json, MessageProtocol.TYPE),
                optString(json, MessageProtocol.ROOM_ID),
                optString(json, MessageProtocol.SENDER), "",
                optString(json, MessageProtocol.MSG_METHOD));
        mRawMsgData = json;
        mMsgType = json != null ? json.optString(MessageProtocol.MSG_TYPE) : "";
        mStatusCode = json != null ? json.optInt(MessageProtocol.STATUS_CODE) : -1;
        mTarget = json != null ? json.optJSONObject(MessageProtocol.DATA) : null;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return mStatusCode == MessageProtocol.SUCCESS_CODE;
    }

    public String getErrorMessage() {
        return optString(mTarget, "err_msg");
    }

    public JSONObject getRawMsgData() {
        return mRawMsgData;
    }

    @Override
    public String toString() {
        return "JsonIncomeMessage{method='" + mMethod + '\'' + ", id='" + id + '\'' + '}';
    }


    private static String optString(JSONObject jsonObject, String key) {
        return jsonObject != null ? jsonObject.optString(key) : "";
    }
}
