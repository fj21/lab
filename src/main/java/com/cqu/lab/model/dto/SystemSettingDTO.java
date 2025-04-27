package com.cqu.lab.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 系统设置DTO
 */
@Data
public class SystemSettingDTO {

    /**
     * 网站名称
     */
    @NotBlank(message = "网站名称不能为空")
    private String siteName;

    /**
     * 联系邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String contactEmail;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 是否允许注册
     */
    private Boolean allowRegistration;
}
