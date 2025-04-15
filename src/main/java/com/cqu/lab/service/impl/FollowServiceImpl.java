package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.model.entity.Follow;
import com.cqu.lab.service.FollowService;
import com.cqu.lab.mapper.FollowMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【follow(用户关注表)】的数据库操作Service实现
* @createDate 2025-04-13 14:43:09
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

}




