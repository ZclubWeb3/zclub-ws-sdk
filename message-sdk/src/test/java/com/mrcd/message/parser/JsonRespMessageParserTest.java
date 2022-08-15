package com.mrcd.message.parser;

import com.mrcd.message.BaseMsgTest;
import com.mrcd.message.msg.JsonResponseMessage;
import com.mrcd.utils.AssetsUtil;

import org.json.JSONObject;
import org.junit.Test;

/**
 * JsonRespMessageParser TestCase
 */
public class JsonRespMessageParserTest extends BaseMsgTest {

    /**
     * 解析请求成功的协议数据
     */
    @Test
    public void testParseValidJson() {
        String messageBody = AssetsUtil.readJsonString("apply_for_mic.json") ;
        notNull( messageBody);

        JsonResponseMessage jsonIncomeMessage = new JsonRespMessageParser().parse(messageBody) ;
        // type 验证
        eq( "chatroom", jsonIncomeMessage.getType());
        eq( "uuid", jsonIncomeMessage.getId());
        eq( "apply_for_mic", jsonIncomeMessage.getMethod());
        eq(200, jsonIncomeMessage.getStatusCode());
        eq("s13131414", jsonIncomeMessage.getSenderId());
        eq("42fs52fsf525224iul9", jsonIncomeMessage.getRoomId());

        // 验证body 参数
        JSONObject bodyJson = jsonIncomeMessage.getTarget() ;
        notNull( bodyJson);
        eq( "hello", bodyJson.optString("msg"));
    }

    /**
     *
     * 解析含有错误信息的测试用例
     */
    @Test
    public void testParseProtocolError() {
        String messageBody = AssetsUtil.readJsonString("websocket_error.json") ;
        notNull( messageBody);

        JsonResponseMessage jsonIncomeMessage = new JsonRespMessageParser().parse(messageBody) ;

        eq( 404, jsonIncomeMessage.getStatusCode());
        eq( "mid_123", jsonIncomeMessage.getId());

        notNull( jsonIncomeMessage.getTarget() );
        // 错误消息验证
        eq( "method not found!", jsonIncomeMessage.getTarget().optString("err_msg"));
    }


    /**
     * 无效的数据
     */
    @Test
    public void testParseInValidJson() {
        // empty string
        JsonResponseMessage jsonIncomeMessage = new JsonRespMessageParser().parse("") ;
        notNull(jsonIncomeMessage);
        eq("", jsonIncomeMessage.getType());
        isNull(jsonIncomeMessage.getTarget());


        // null string
        jsonIncomeMessage = new JsonRespMessageParser().parse(null) ;
        notNull(jsonIncomeMessage);
        eq("", jsonIncomeMessage.getType());
        isNull(jsonIncomeMessage.getTarget());

    }

    @Test
    public void testFileNotFound() {
        String fileData = AssetsUtil.readJsonString("not_found.json") ;
        isNullStr(fileData);
    }
}
