package com.cqu.lab.mq;

import com.cqu.lab.constant.Constants;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.repository.UserRepository;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.dao.DataAccessException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * 用户信息更新消息监听器
 * 用于收到用户更新消息后同步更新ES索引
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = Constants.USER_UPDATE_TOPIC,
        consumerGroup = "lab-user-update-consumer-group",
        nameServer = "${spring.rocketmq.name-server}"
)
public class UserUpdateListener implements RocketMQListener<User> {

    @Resource
    private UserRepository userRepository;

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public void onMessage(User user) {
        if (user == null || user.getId() == null) {
            log.error("接收到无效的用户更新消息，用户对象为空或ID为空");
            return;
        }

        log.info("接收到用户更新消息，用户ID：{}", user.getId());

        try {
            // 调用保存方法更新ES索引
            saveUserToElasticsearch(user);
        } catch (Exception e) {
            log.error("用户信息ES索引更新失败，用户ID：{}，错误：{}", user.getId(), e.getMessage(), e);
        }
    }

    /**
     * 将用户信息保存到Elasticsearch索引
     * 使用Spring Retry进行重试
     *
     * @param user 用户实体
     */
    @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void saveUserToElasticsearch(User user) {
        try {
            // 检查索引是否存在，不存在则创建
            if (!elasticsearchOperations.indexOps(User.class).exists()) {
                log.info("用户索引不存在，正在创建索引");
                elasticsearchOperations.indexOps(User.class).create();
                elasticsearchOperations.indexOps(User.class).putMapping();
            }

            // 保存用户到ES索引
            // 使用ElasticsearchOperations保存文档
            elasticsearchOperations.save(user);
            log.info("用户信息ES索引更新成功，用户ID：{}", user.getId());
        } catch (org.springframework.data.elasticsearch.UncategorizedElasticsearchException e) {
            log.error("Elasticsearch连接异常（将重试），用户ID：{}，错误：{}", user.getId(), e.getMessage());
            throw e; // 重新抛出异常以触发重试
        } catch (DataAccessException e) {
            log.error("用户信息ES索引更新失败（将重试），用户ID：{}，错误：{}", user.getId(), e.getMessage());
            throw e; // 重新抛出异常以触发重试
        } catch (Exception e) {
            log.error("用户信息ES索引更新失败（不可重试的错误），用户ID：{}，错误：{}", user.getId(), e.getMessage(), e);
        }
    }
}