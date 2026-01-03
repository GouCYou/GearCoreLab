package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.CommentMapper;
import cn.gcc.gearcorelab.mapper.CommentLikeMapper;
import cn.gcc.gearcorelab.model.Comment;
import cn.gcc.gearcorelab.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;

    public CommentServiceImpl(CommentMapper commentMapper,
                              CommentLikeMapper commentLikeMapper) {
        this.commentMapper = commentMapper;
        this.commentLikeMapper = commentLikeMapper;
    }

    @Override
    public List<Comment> listForPost(Long postId) {
        List<Comment> comments = commentMapper.findByPostId(postId);
        // 排序：有点赞的按点赞数降序，无点赞的按时间降序（新评论在上面）
        comments.sort((a, b) -> {
            if (a.getLikeCount() != b.getLikeCount()) {
                return Integer.compare(b.getLikeCount(), a.getLikeCount()); // 点赞多的在前
            } else {
                return b.getCreatedAt().compareTo(a.getCreatedAt()); // 时间新的在前
            }
        });
        return comments;
    }

    @Override
    @Transactional
    public void add(Comment comment, String imageUrl) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setImageUrl(imageUrl);
        commentMapper.insert(comment);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long currentUserId, boolean isAdmin) {
        Comment c = commentMapper.findById(commentId);
        if (c == null) {
            throw new IllegalArgumentException("评论不存在");
        }
        if (!isAdmin && !c.getUserId().equals(currentUserId)) {
            throw new SecurityException("无权限删除此评论");
        }
        // 删除点赞记录
        commentLikeMapper.deleteByCommentId(commentId);
        // 删除评论
        commentMapper.delete(commentId);
    }
    
    /**
     * 根据用户名查询评论
     */
    @Override
    public List<Comment> getCommentsByUsername(String username) {
        return commentMapper.findByUsername(username);
    }
    
    @Override
    public long getTotalCommentCount() {
        return commentMapper.countAllComments();
    }
}
