package com.mrcd.message.protocol;

/**
 * Message Protocol Constrait Strings
 */
public final class MessageProtocol {

    // ====================> integer type
    /**
     * 退出码
     */
    public static final int CLOSE_CODE = 1000;

    public static final int SUCCESS_CODE = 200;


    // ===========================> string type
    /**
     * 退出消息
     */
    public static final String BYE_BYE = "bye";
    /**
     * 状态码
     */
    public static final String STATUS_CODE = "status_code";

    /**
     * 消息类型, 目前有 chatroom, 代表群聊
     */
    public static final String TYPE = "type";
    /**
     * 群聊类型
     */
    public static final String CHATROOM_TYPE = "chatroom";
    /**
     * 消息请求方法, 相当于http的path
     */
    public static final String MSG_METHOD = "method";
    /**
     * 消息id
     */
    public static final String MSG_ID = "msg_id";
    /**
     * 消息data
     */
    public static final String DATA = "data";
    /**
     * msg发送者
     */
    public static final String SENDER = "sender";
    /**
     * msg接收者
     */
    public static final String RECEIVER = "receiver";
    /**
     * msg接收者(多个接收者)
     */
    public static final String RECEIVERS = "receivers";
    /**
     * 房间id
     */
    public static final String ROOM_ID = "room_id";
    /**
     * 消息
     */
    public static final String MSG = "msg";
    /**
     * ~@用户类型消息
     */
    public static final String AT = "at";
    /**
     * 时间戳
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * 消息类型
     */
    public static final String MSG_TYPE = "msg_type";

    private MessageProtocol() {
    }
}
