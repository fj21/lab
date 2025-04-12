package com.cqu.lab.mq;

import com.cqu.lab.constant.Constants;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 用户信息更新消息监听器
 * 用于收到用户更新消息后同步更新ES索引
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = Constants.USER_UPDATE_TOPIC,
        consumerGroup = "lab-user-update-consumer-group"
)
public class UserUpdateListener implements RocketMQListener<User> {
    
    @Resource
    private UserRepository userRepository;
    
    @Override
    public void onMessage(User user) {
        try {
            // 更新ES索引
            userRepository.save(user);
            log.info("用户信息ES索引更新成功，用户ID：{}", user.getId());
        } catch (Exception e) {
            log.error("用户信息ES索引更新失败，用户ID：{}，错误：{}", user.getId(), e.getMessage());
        }
    }
} 