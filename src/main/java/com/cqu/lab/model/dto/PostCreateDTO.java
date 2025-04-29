package com.cqu.lab.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreateDTO {
    /**
     * type -0 图片 -1 视频
     */
    private Integer type;
    /**
     * 帖子标题
     */
    private String title;
    /**
     * 分类
     */
    private Integer category;
    /**
     * 可见性
     */
    private Integer visibility;
    /**
     * 帖子内容
     */
    private String content;
}
