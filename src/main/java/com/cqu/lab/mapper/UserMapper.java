package com.cqu.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqu.lab.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);
    
    /**
     * 获取用户基本信息（用于展示）
     * @param userId 用户ID
     * @return 用户基本信息
     */
    @Select("SELECT id, username, image, signature FROM user WHERE id = #{userId}")
    User findBasicInfoById(@Param("userId") Integer userId);
}




