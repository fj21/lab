package com.cqu.lab.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.StringMessageConverter;

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
}
