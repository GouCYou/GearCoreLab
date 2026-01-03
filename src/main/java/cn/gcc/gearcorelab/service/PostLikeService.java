package cn.gcc.gearcorelab.service;

/**
 * 帖子点赞业务接口
 */
public interface PostLikeService {
    /**
     * 判断用户是否已点赞
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    boolean liked(Long postId, Long userId);

    /**
     * 点赞
     */
    void like(Long postId, Long userId);

    /**
     * 取消点赞
     */
    void unlike(Long postId, Long userId);

    /**
     * 查询点赞总数
     */
    int countLikes(Long postId);
}
