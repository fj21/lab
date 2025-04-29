package com.cqu.lab.controller;

import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.dto.UserLoginDTO;
import com.cqu.lab.model.dto.UserRegisterDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.model.vo.UserVO;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.ThreadLocalUtil;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Integer> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("用户注册请求: {}", registerDTO.getPhone());
        Integer userId = userService.register(registerDTO);
        return Result.success(userId, "注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Object> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("用户登录请求: {}", loginDTO.getPhone());
        try {
            // 获取登录token
            String token = userService.login(loginDTO);
            log.info("用户登录成功，生成token: {}", token);

            // 获取用户ID
            User user = userService.getByPhone(loginDTO.getPhone());
            if (user == null) {
                log.error("用户登录后无法获取用户信息: {}", loginDTO.getPhone());
                return Result.failed("登录失败: 无法获取用户信息");
            }

            Integer userId = user.getId();
            log.info("用户登录成功，用户ID: {}", userId);

            // 创建包含token和userId的响应对象
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("userId", userId);

            return Result.success(responseData, "登录成功");
        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage(), e);
            return Result.failed("登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getCurrentUserInfo() {
        Integer userId = ThreadLocalUtil.getUserId();
        log.info("获取当前用户信息, 用户ID: {}", userId);

        if (userId == null) {
            log.warn("获取用户信息失败: 未找到用户ID");
            return Result.failed("未找到用户ID，请重新登录");
        }

        try {
            UserVO userInfo = userService.getUserInfo(userId);
            log.info("成功获取用户信息: {}", userInfo.getUsername());
            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息异常: {}", e.getMessage(), e);
            return Result.failed("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取用户基本信息
     */
    @GetMapping("/basic/{userId}")
    public Result<UserBasicVO> getUserBasicInfo(@PathVariable @NotNull Integer userId) {
        log.info("获取用户基本信息, 用户ID: {}", userId);
        return Result.success(userService.getUserBasicInfo(userId));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<Boolean> updateUserInfo(@Valid @RequestBody UserUpdateDTO updateDTO) {
        Integer userId = ThreadLocalUtil.getUserId();
        log.info("更新用户信息, 用户ID: {}", userId);

        // 确保更新的是当前用户的信息
        updateDTO.setId(userId);

        boolean success = userService.updateUserInfo(updateDTO);
        return success ? Result.success(true, "更新成功") : Result.failed("更新失败");
    }
}