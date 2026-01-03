package cn.gcc.gearcorelab.service;

public interface CommentLikeService {
    boolean liked(Long commentId, Long userId);
    int countLikes(Long commentId);
    void like(Long commentId, Long userId);
    void unlike(Long commentId, Long userId);
}
