package com.cqu.lab.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Elasticsearch索引映射配置类
 * 用于初始化Elasticsearch索引和配置IK分词器
 */
@Slf4j
@Component
@Conditional(ElasticsearchDisableCondition.class)
public class ElasticsearchMappingConfig implements ApplicationRunner {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.user-index}")
    private String userIndex;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // 检查用户索引是否存在
            boolean userIndexExists = restHighLevelClient.indices().exists(
                    new GetIndexRequest(userIndex), RequestOptions.DEFAULT);

            // 如果索引不存在，则创建索引并配置映射
            if (!userIndexExists) {
                log.info("Creating Elasticsearch index: {}", userIndex);
                createUserIndex();
            }
        } catch (Exception e) {
            log.error("Failed to initialize Elasticsearch indices: {}", e.getMessage(), e);
        }
    }

    /**
     * 创建用户索引并配置映射
     */
    private void createUserIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(userIndex);

        // 配置索引设置，包括分片数、副本数和分析器
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                // 配置IK分词器
                .put("analysis.analyzer.ik_smart.type", "custom")
                .put("analysis.analyzer.ik_smart.tokenizer", "ik_smart")
                .put("analysis.analyzer.ik_max_word.type", "custom")
                .put("analysis.analyzer.ik_max_word.tokenizer", "ik_max_word")
                .build());

        // 创建映射
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                // ID字段
                builder.startObject("id");
                {
                    builder.field("type", "integer");
                }
                builder.endObject();

                // 手机号字段 - keyword类型，不分词
                builder.startObject("phone");
                {
                    builder.field("type", "keyword");
                }
                builder.endObject();

                // 用户名字段 - 使用IK分词器
                builder.startObject("username");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "ik_smart");
                    builder.field("search_analyzer", "ik_smart");
                }
                builder.endObject();

                // 个性签名字段 - 使用IK分词器
                builder.startObject("signature");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "ik_smart");
                    builder.field("search_analyzer", "ik_smart");
                }
                builder.endObject();

                // 其他字段可以根据需要添加
            }
            builder.endObject();
        }
        builder.endObject();

        request.mapping(builder);

        // 创建索引
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        log.info("Successfully created index: {}", userIndex);
    }
}
