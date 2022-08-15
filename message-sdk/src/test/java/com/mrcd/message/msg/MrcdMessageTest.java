package com.mrcd.message.msg;

import com.mrcd.message.BaseMsgTest;
import org.json.JSONObject;
import org.junit.Test;

/**
 * MrcdMessage Test
 */
public class MrcdMessageTest extends BaseMsgTest {

    @Test
    public void testJsonRequestMessage() {
        final SimpleMessage simpleMessage = new SimpleMessage("mrsimple") ;
        // 构建请求对象, 包含 msg_id 、type 等类型
        JSONObject request = simpleMessage.buildRequest() ;
        // 请求方法
        assertEquals("update_name", request.optString("method"));
        // 请求类型
        assertEquals("chatroom", request.optString("type"));
        // msg_id 不为空
        assertNotNull(request.optString("msg_id"));
        // sender
        assertEquals("sender", request.optString("sender"));
        // receiver
        assertEquals("receiver", request.optString("receiver"));
        // room id
        assertEquals("room", request.optString("room_id"));

        // 验证 body 中的内容
        String target = simpleMessage.getTarget();
        assertNotNull(target);
        assertEquals("mrsimple", target);
    }

    /**
     * 测试通过 uuid 生成的msg id 不重复
     */
    @Test
    public void testMessageIdUnique() {
        SimpleMessage lastMessage = new SimpleMessage("mrcd") ;
        for (int i = 0; i < 10000; i++) {
            SimpleMessage curMessage = new SimpleMessage("mrcd") ;
            eq("mrcd", curMessage.getTarget());
            notNull(curMessage.getId());

            // 通过 uuid 生成的msg id 不重复
            assertNotEquals(lastMessage.getId(), curMessage.getId());
            lastMessage = curMessage ;
        }
    }

    /**
     * simple message
     */
    private static class SimpleMessage extends MrcdMessage<String> {

        public SimpleMessage(String mTarget) {
            super("room", "sender", "receiver",   "update_name", mTarget);
        }
    }
}
