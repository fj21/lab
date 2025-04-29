package com.cqu.lab.service;

import com.cqu.lab.model.dto.UserCreateDTO;
import com.cqu.lab.model.dto.UserLoginDTO;
import com.cqu.lab.model.dto.UserRegisterDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.model.vo.UserListVO;
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

    /**
     * 获取用户列表（分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户列表
     */
    UserListVO getUserList(Integer pageNum, Integer pageSize);

    /**
     * 创建用户（管理员操作）
     * @param userCreateDTO 用户创建DTO
     * @return 用户ID
     */
    Integer createUser(UserCreateDTO userCreateDTO);

    /**
     * 更新用户信息（管理员操作）
     * @param userId 用户ID
     * @param userUpdateDTO 用户更新DTO
     * @return 是否成功
     */
    Boolean updateUserAdmin(Integer userId, UserUpdateDTO userUpdateDTO);

    /**
     * 更新用户状态（启用/禁用）
     * @param userId 用户ID
     * @param status 状态（0-正常，1-禁用）
     * @return 是否成功
     */
    Boolean updateUserStatus(Integer userId, Integer status);

    /**
     * 获取用户总数
     * @return 用户总数
     */
    long count();

    /**
     * 根据token获取用户ID
     * @param token 用户token
     * @return 用户ID字符串
     */
    String getUserIdByToken(String token);
}
