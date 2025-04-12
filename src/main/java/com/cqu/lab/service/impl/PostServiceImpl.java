package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.dao.domain.Post;
import com.cqu.lab.service.PostService;
import com.cqu.lab.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-04-12 15:54:03
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




