package com.cqu.lab.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 条件类，用于根据配置决定是否启用Elasticsearch
 */
public class ElasticsearchDisableCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 如果elasticsearch.enabled为false，则不启用Elasticsearch配置
        String enabled = context.getEnvironment().getProperty("elasticsearch.enabled");
        return !"false".equalsIgnoreCase(enabled);
    }
}
