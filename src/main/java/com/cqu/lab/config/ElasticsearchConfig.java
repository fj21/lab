package com.cqu.lab.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch配置类
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.cqu.lab.repository")
public class ElasticsearchConfig {
    
    @Value("${elasticsearch.host}")
    private String host;
    
    @Value("${elasticsearch.port}")
    private int port;
    
    /**
     * 配置ES客户端
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
    
    /**
     * 配置ES模板
     */
    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }
} 