package com.cqu.lab.service;

import com.cqu.lab.model.dto.UserLoginDTO;
import com.cqu.lab.model.dto.UserRegisterDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.model.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户ID
     */
    Integer register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return JWT令牌
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 获取用户详细信息
     * @param userId 用户ID
     * @return 用户详细信息
     */
    UserVO getUserInfo(Integer userId);
    
    /**
     * 获取用户基本信息（用于简略显示）
     * @param userId 用户ID
     * @return 用户基本信息
     */
    UserBasicVO getUserBasicInfo(Integer userId);
    
    /**
     * 更新用户信息
     * @param updateDTO 更新信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(UserUpdateDTO updateDTO);
    
    /**
     * 根据用户ID获取用户实体
     * @param userId 用户ID
     * @return 用户实体
     */
    User getById(Integer userId);
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户实体
     */
    User getByPhone(String phone);
}
