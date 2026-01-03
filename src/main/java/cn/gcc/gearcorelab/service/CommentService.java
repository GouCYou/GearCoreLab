package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> listForPost(Long postId);
    void add(Comment comment, String imageUrl);
    void delete(Long commentId, Long currentUserId, boolean isAdmin);
    List<Comment> getCommentsByUsername(String username);
    long getTotalCommentCount();
}
