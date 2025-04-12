package com.cqu.lab.repository;

import com.cqu.lab.model.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户ES仓库接口
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {
    
    /**
     * 根据用户名模糊查询
     * @param username 用户名关键词
     * @return 匹配的用户列表
     */
    List<User> findByUsernameContaining(String username);
    
    /**
     * 根据个性签名模糊查询
     * @param signature 签名关键词
     * @return 匹配的用户列表
     */
    List<User> findBySignatureContaining(String signature);
} 