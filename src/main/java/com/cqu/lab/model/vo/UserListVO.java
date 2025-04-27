package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户列表VO
 */
@Data
public class UserListVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 总用户数
     */
    private Integer total;
    
    /**
     * 当前页码
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 用户列表
     */
    private List<UserVO> userList;
}
