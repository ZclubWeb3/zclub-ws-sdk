package com.mrcd.message.msg;

import com.mrcd.message.BaseMsgTest;
import com.mrcd.message.protocol.MessageProtocol;

import org.json.JSONObject;
import org.junit.Test;

/**
 * JsonRequestMessage Test
 */
public class JsonRequestMessageTest extends BaseMsgTest {

    @Test
    public void testJsonReqMessage() {
        final JsonRequestMessage inviteMicMsg = new JsonRequestMessage("room01" ,"user_1234", "receiver_1234", "mic_invite") ;
        // 1. 添加参数
        inviteMicMsg.putInData("msg", "Say hello");
        // 2. 构建请求对象, 包含 msg_id 、type 等类型
        JSONObject request = inviteMicMsg.buildRequest() ;

        // 请求方法
        assertEquals("mic_invite", inviteMicMsg.getMethod());
        // 请求类型
        assertEquals("chatroom", inviteMicMsg.getType());
        // msg_id 不为空
        assertNotNull(inviteMicMsg.getId());
        // sender
        assertEquals("user_1234", inviteMicMsg.getSenderId());
        // roomid
        assertEquals("room01", inviteMicMsg.getRoomId());
        // receiver
        assertEquals("receiver_1234", inviteMicMsg.getReceiverId());

        // 验证 body 中的内容
        JSONObject body = request.optJSONObject(MessageProtocol.DATA) ;
        assertNotNull(body);
        assertEquals("Say hello", inviteMicMsg.getTarget().optString("msg"));
    }


    @Test
    public void testJsonReqMessageWithBody() {
        // 1. 添加参数
        final JsonRequestMessage inviteMicMsg = new JsonRequestMessage("room01", "suser_1234",  "ruser_1234", "mic_invite") ;
        // put key-value in data
        inviteMicMsg.putInData("msg", "hello") ;
        // 2. 构建请求对象, 包含 msg_id 、type 等类型
        JSONObject request = inviteMicMsg.buildRequest() ;

        // 请求方法
        assertEquals("mic_invite", inviteMicMsg.getMethod());
        // 请求类型
        assertEquals("chatroom", inviteMicMsg.getType());
        // msg_id 不为空
        assertNotNull(inviteMicMsg.getId());
        // sender
        assertEquals("suser_1234", inviteMicMsg.getSenderId());
        // roomid
        assertEquals("room01", inviteMicMsg.getRoomId());
        // receiver
        assertEquals("ruser_1234", inviteMicMsg.getReceiverId());

        // 验证 body 中的内容, 从 request 中去 body
        JSONObject bodyFromReq = request.optJSONObject(MessageProtocol.DATA) ;
        assertNotNull(bodyFromReq);

        // 从 message 中直接去 body target
        JSONObject bodyFromMsg = inviteMicMsg.getTarget();
        assertNotNull(bodyFromMsg);
        assertEquals("hello", inviteMicMsg.getTarget().optString("msg"));
    }
}
