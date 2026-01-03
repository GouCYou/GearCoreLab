// src/main/java/cn/gcc/gearcorelab/mapper/CommentMapper.java
package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.Comment;
import java.util.List;

public interface CommentMapper {

    List<Comment> findByPostId(Long postId);

    Comment findById(Long id);

    void insert(Comment comment);

    void delete(Long id);

    /**
     * 新增：批量删除某帖下的所有评论
     */
    void deleteByPostId(Long postId);
    
    /**
     * 根据用户名查询评论
     */
    List<Comment> findByUsername(String username);
    
    /**
     * 获取评论总数
     */
    long countAllComments();
}
