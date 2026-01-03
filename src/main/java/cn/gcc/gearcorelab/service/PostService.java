package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.Post;

import java.util.List;

/**
 * 帖子业务接口
 */
public interface PostService {
    List<Post> listAll();
    List<Post> listRecent();
    // 新增分页方法
    List<Post> listAllWithPagination(int page, int size);
    long getTotalPostCount();
    void create(Post post, List<String> imageUrls);
    Post getById(Long id);
    boolean liked(Long postId, Long userId);
    int countLikes(Long postId);
    void like(Long postId, Long userId);
    void unlike(Long postId, Long userId);
    void delete(Long postId, Long currentUserId, boolean isAdmin);
    // 新增：
    java.time.LocalDateTime getLastActivity(Long postId);
    List<String> getImageUrls(Long postId);
    List<Post> getPostsByUsername(String username);
    List<Post> searchPosts(String query);
    // 管理员相关方法
    List<Post> getAllPostsForAdmin(int page, int size, String search, String status);
    void deletePost(Long postId);
}
