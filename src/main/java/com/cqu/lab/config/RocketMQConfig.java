package com.cqu.lab.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * RocketMQ配置类
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQConfig {

    @Value("${spring.rocketmq.name-server}")
    private String nameServer;

    @Value("${spring.rocketmq.producer.group}")
    private String producerGroup;

    @Value("${spring.rocketmq.consumer.group}")
    private String consumerGroup;

    @Bean
    @ConditionalOnMissingBean(RocketMQTemplate.class)
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();

        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServer);
        producer.setProducerGroup(producerGroup);
        producer.setSendMsgTimeout(3000);

        rocketMQTemplate.setProducer(producer);
        rocketMQTemplate.setMessageConverter(new StringMessageConverter());

        return rocketMQTemplate;
    }

    /**
     * 配置自定义的 RocketMQ 消息转换器
     * 解决实体类序列化问题
     */
    @Bean
    public MessageConverter rocketMQMessageConverter() {
        List<MessageConverter> converters = new ArrayList<>();
        
        // 添加Jackson转换器，用于处理对象到JSON的转换
        MappingJackson2MessageConverter jackson2MessageConverter = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setSerializedPayloadClass(String.class);
        
        // 设置消息内容类型为JSON
        jackson2MessageConverter.setContentTypeResolver(mimetype -> {
            return org.springframework.util.MimeType.valueOf("application/json");
        });
        
        // 添加转换器到列表
        converters.add(jackson2MessageConverter);
        converters.add(new StringMessageConverter());
        
        // 创建组合转换器
        return new CompositeMessageConverter(converters);
    }
}
