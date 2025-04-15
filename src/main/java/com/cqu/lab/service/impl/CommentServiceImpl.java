package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.model.entity.Comment;
import com.cqu.lab.service.CommentService;
import com.cqu.lab.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2025-04-13 13:11:31
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




