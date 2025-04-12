package com.cqu.lab.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqu.lab.constant.Constants;
import com.cqu.lab.mapper.UserMapper;
import com.cqu.lab.model.dto.UserLoginDTO;
import com.cqu.lab.model.dto.UserRegisterDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.model.vo.UserVO;
import com.cqu.lab.repository.UserRepository;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.JwtUtil;
import com.cqu.lab.utils.RedisUtil;
import com.cqu.lab.utils.RocketMQUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserRepository userRepository;
    
    @Resource
    private PasswordEncoder passwordEncoder;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Resource
    private RedisUtil redisUtil;
    
    @Resource
    private RedissonClient redissonClient;
    
    @Resource
    private RocketMQUtil rocketMQUtil;
    
    /**
     * 用户注册
     * 使用Redisson分布式锁保证同一手机号并发注册的安全性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer register(UserRegisterDTO registerDTO) {
        // 获取用户手机号的分布式锁
        String lockKey = Constants.USER_PHONE_LOCK_PREFIX + registerDTO.getPhone();
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            // 尝试获取锁，最多等待3秒，锁过期时间10秒
            boolean locked = lock.tryLock(Constants.USER_PHONE_LOCK_WAIT_TIME, Constants.USER_PHONE_LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                throw new RuntimeException("系统繁忙，请稍后再试");
            }
            
            // 检查手机号是否已被注册
            User existUser = getByPhone(registerDTO.getPhone());
            if (existUser != null) {
                throw new RuntimeException("该手机号已被注册");
            }
            
            // 创建新用户
            User user = new User();
            user.setPhone(registerDTO.getPhone());
            user.setUsername(registerDTO.getUsername());
            
            // 生成随机盐并加密密码
            String salt = RandomUtil.randomString(6);
            user.setSalt(salt);
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword() + salt));
            
            // 设置初始值
            user.setImage("https://placeholder.com/user_default.png"); // 默认头像
            user.setSignature("这个人很懒，什么都没留下"); // 默认签名
            user.setStatus(Constants.USER_STATUS_NORMAL); // 正常状态
            user.setAttention(0); // 关注数
            user.setFollowers(0); // 粉丝数
            user.setWorks(0); // 作品数
            user.setLikes(0); // 获赞数
            
            // 保存用户到数据库
            userMapper.insert(user);
            
            // 保存用户到ES索引
            userRepository.save(user);
            
            return user.getId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("注册过程被中断");
        } finally {
            // 确保锁被释放
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
    
    /**
     * 用户登录
     */
    @Override
    public String login(UserLoginDTO loginDTO) {
        // 根据手机号查询用户
        User user = getByPhone(loginDTO.getPhone());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查账号状态
        if (Constants.USER_STATUS_LOCKED == user.getStatus()) {
            throw new RuntimeException("账号已被锁定");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword() + user.getSalt(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 生成JWT令牌
        return jwtUtil.generateToken(user.getId());
    }
    
    /**
     * 获取用户详细信息
     */
    @Override
    public UserVO getUserInfo(Integer userId) {
        // 从Redis缓存获取
        String cacheKey = Constants.REDIS_USER_KEY_PREFIX + userId;
        Object cacheObj = redisUtil.getCacheObject(cacheKey);
        
        if (cacheObj != null) {
            return (UserVO) cacheObj;
        }
        
        // 缓存未命中，从数据库查询
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        
        // 存入Redis缓存
        redisUtil.setCacheObject(cacheKey, userVO, Constants.USER_CACHE_EXPIRE_TIME);
        
        return userVO;
    }
    
    /**
     * 获取用户基本信息（用于简略显示）
     */
    @Override
    public UserBasicVO getUserBasicInfo(Integer userId) {
        // 从Redis缓存获取
        String cacheKey = Constants.REDIS_USER_BASIC_INFO_KEY_PREFIX + userId;
        Object cacheObj = redisUtil.getCacheObject(cacheKey);
        
        if (cacheObj != null) {
            return (UserBasicVO) cacheObj;
        }
        
        // 缓存未命中，从数据库查询
        User user = userMapper.findBasicInfoById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 转换为VO对象
        UserBasicVO userBasicVO = new UserBasicVO();
        BeanUtil.copyProperties(user, userBasicVO);
        
        // 存入Redis缓存，不设置过期时间（基本信息变化不频繁）
        redisUtil.setCacheObject(cacheKey, userBasicVO);
        
        return userBasicVO;
    }
    
    /**
     * 更新用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(UserUpdateDTO updateDTO) {
        Integer userId = updateDTO.getId();
        
        // 查询用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户信息
        User updateUser = new User();
        updateUser.setId(userId);
        
        // 只更新有值的字段
        if (updateDTO.getUsername() != null) {
            updateUser.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getImage() != null) {
            updateUser.setImage(updateDTO.getImage());
        }
        if (updateDTO.getSignature() != null) {
            updateUser.setSignature(updateDTO.getSignature());
        }
        
        // 更新数据库
        int rows = userMapper.updateById(updateUser);
        
        if (rows > 0) {
            // 清除Redis缓存
            redisUtil.deleteObject(Constants.REDIS_USER_KEY_PREFIX + userId);
            redisUtil.deleteObject(Constants.REDIS_USER_BASIC_INFO_KEY_PREFIX + userId);
            
            // 查询更新后的完整用户数据
            User updatedUser = userMapper.selectById(userId);
            
            // 异步更新ES索引
            rocketMQUtil.asyncSend(Constants.USER_UPDATE_TOPIC, updatedUser);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * 根据用户ID获取用户实体
     */
    @Override
    public User getById(Integer userId) {
        return userMapper.selectById(userId);
    }
    
    /**
     * 根据手机号查询用户
     */
    @Override
    public User getByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }
}




