package com.cqu.lab.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqu.lab.model.entity.UserRole;
import org.apache.ibatis.annotations.Select;

/**
* @author Jinhong Jiang
* @description 针对表【user_role(用户角色表)】的数据库操作Mapper
* @createDate 2025-04-28 23:44:02
* @Entity generator.domain.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select * from user_role where user_id = #{user_id}")
    UserRole selectByUserId(Integer user_id);
}




