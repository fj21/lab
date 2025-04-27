package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 统计数据VO
 */
@Data
public class StatisticsVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户总数
     */
    private Integer userCount;
    
    /**
     * 帖子总数
     */
    private Integer postCount;
    
    /**
     * 今日新增用户数
     */
    private Integer todayNewUsers;
    
    /**
     * 今日新增帖子数
     */
    private Integer todayNewPosts;
    
    /**
     * 用户活跃度统计（最近7天）
     * key: 日期，value: 活跃用户数
     */
    private Map<String, Integer> userActivityStats;
    
    /**
     * 帖子分类统计
     * key: 分类名称，value: 帖子数量
     */
    private Map<String, Integer> postCategoryStats;
}
