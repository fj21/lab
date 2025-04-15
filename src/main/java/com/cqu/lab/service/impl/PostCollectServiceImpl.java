package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.model.entity.PostCollect;
import com.cqu.lab.service.PostCollectService;
import com.cqu.lab.mapper.PostCollectMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【post_collect(用户收藏表)】的数据库操作Service实现
* @createDate 2025-04-13 14:43:06
*/
@Service
public class PostCollectServiceImpl extends ServiceImpl<PostCollectMapper, PostCollect>
    implements PostCollectService{

}




