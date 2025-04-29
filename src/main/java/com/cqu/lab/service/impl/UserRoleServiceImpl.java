package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cqu.lab.model.entity.UserRole;
import com.cqu.lab.service.UserRoleService;
import com.cqu.lab.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【user_role(用户角色表)】的数据库操作Service实现
* @createDate 2025-04-28 23:44:02
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




