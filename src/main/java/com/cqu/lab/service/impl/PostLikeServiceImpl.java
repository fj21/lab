package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.model.entity.PostLike;
import com.cqu.lab.service.PostLikeService;
import com.cqu.lab.mapper.PostLikeMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【post_like(用户点赞表)】的数据库操作Service实现
* @createDate 2025-04-13 14:43:02
*/
@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike>
    implements PostLikeService{

}




