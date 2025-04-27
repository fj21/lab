package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统设置VO
 */
@Data
public class SystemSettingVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 网站名称
     */
    private String siteName;
    
    /**
     * 联系邮箱
     */
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
