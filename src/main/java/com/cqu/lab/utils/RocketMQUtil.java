package com.cqu.lab.utils;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMQ工具类
 */
@Component
public class RocketMQUtil {

    private RocketMQTemplate rocketMQTemplate;
    
    public RocketMQUtil(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }
    
    /**
     * 同步发送消息
     * @param topic 主题
     * @param message 消息
     */
    public <T> void syncSend(String topic, T message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }
    
    /**
     * 异步发送消息
     * @param topic 主题
     * @param message 消息
     */
    public <T> void asyncSend(String topic, T message) {
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), null);
    }
    
    /**
     * 延迟发送消息
     * @param topic 主题
     * @param message 消息
     * @param delayLevel 延迟等级
     */
    public <T> void syncSendDelay(String topic, T message, int delayLevel) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(message).build(), 3000, delayLevel);
    }
} 