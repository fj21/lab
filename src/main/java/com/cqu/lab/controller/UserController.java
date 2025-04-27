package com.cqu.lab.controller;

import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.dto.UserLoginDTO;
import com.cqu.lab.model.dto.UserRegisterDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
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
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("用户登录请求: {}", loginDTO.getPhone());
        String token = userService.login(loginDTO);
        return Result.success(token, "登录成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getCurrentUserInfo() {
        Integer userId = ThreadLocalUtil.getUserId();
        log.info("获取当前用户信息, 用户ID: {}", userId);
        return Result.success(userService.getUserInfo(userId));
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