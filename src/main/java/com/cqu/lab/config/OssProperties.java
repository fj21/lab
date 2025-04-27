package com.cqu.lab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Aliyun OSS configuration properties
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssProperties {
    /**
     * OSS endpoint
     */
    private String endpoint;
    
    /**
     * Access key ID
     */
    private String accessKeyId;
    
    /**
     * Access key secret
     */
    private String accessKeySecret;
    
    /**
     * Bucket name
     */
    private String bucketName;
    
    /**
     * URL prefix for accessing files
     */
    private String urlPrefix;


}
