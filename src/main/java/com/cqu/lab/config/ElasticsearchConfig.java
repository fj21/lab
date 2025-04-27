package com.cqu.lab.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch配置类
 */
@Configuration
@Conditional(ElasticsearchDisableCondition.class)
@EnableElasticsearchRepositories(basePackages = "com.cqu.lab.repository")
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.username:}")
    private String username;

    @Value("${elasticsearch.password:}")
    private String password;

    /**
     * 配置ES客户端
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(host + ":" + port);

        // 如果配置了用户名和密码，则添加基本认证
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            builder.withBasicAuth(username, password);
        }

        // 设置连接超时和socket超时
        builder.withConnectTimeout(5000)
               .withSocketTimeout(30000);

        ClientConfiguration clientConfiguration = builder.build();
        return RestClients.create(clientConfiguration).rest();
    }

    /**
     * 配置ES模板 - 主要的ElasticsearchRestTemplate Bean
     */
    @Primary
    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }

    /**
     * 为了向后兼容，提供一个名为elasticsearchTemplate的别名
     * 解决"A component required a bean named 'elasticsearchTemplate' that could not be found."错误
     */
    @Bean(name = "elasticsearchTemplate")
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return elasticsearchRestTemplate();
    }
}