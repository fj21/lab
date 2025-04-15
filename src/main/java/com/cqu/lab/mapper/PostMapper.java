package com.cqu.lab.mapper;

import com.cqu.lab.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Mapper
* @createDate 2025-04-13 09:56:12
* @Entity com.cqu.lab.model.entity.Post
*/
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select("select * from post where category = #{category} and id < #{lastPostId} " +
            "order by created_at desc limit 50;")
    List<Post> selectSectionPost(Integer category, Integer lastPostId);

    @Select("select id from post order by created_at desc limit 1;")
    Integer selectNewesPost();
}




