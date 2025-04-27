package com.cqu.lab.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 内容更新DTO
 */
@Data
public class ContentUpdateDTO {
    
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;
    
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> imageUrls;
}
