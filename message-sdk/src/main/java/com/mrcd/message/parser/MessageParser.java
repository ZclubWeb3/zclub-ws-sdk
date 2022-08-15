package com.mrcd.message.parser;

/**
 * json message parser
 * @param <T> 目标消息类型
 */
public interface MessageParser<T> {

    /**
     * 将字符串的消息解析为对应的消息类型
     * @param messageBody
     * @return
     */
    T parse(String messageBody) ;
}
