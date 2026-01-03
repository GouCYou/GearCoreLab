package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.PostImageMapper;
import cn.gcc.gearcorelab.mapper.PostLikeMapper;
import cn.gcc.gearcorelab.mapper.PostMapper;
import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.service.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子业务实现
 */
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostImageMapper imageMapper;
    private final PostLikeMapper likeMapper;

    public PostServiceImpl(PostMapper postMapper,
                           PostImageMapper imageMapper,
                           PostLikeMapper likeMapper) {
        this.postMapper = postMapper;
        this.imageMapper = imageMapper;
        this.likeMapper = likeMapper;
    }

    /**
     * 根据 ID 查询单个帖子
     */
    @Override
    public Post getById(Long postId) {
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 加载帖子图片URL
            post.setImageUrls(imageMapper.findUrlsByPostId(postId));
        }
        return post;
    }

    /**
     * 列出所有帖子
     */
    @Override
    public List<Post> listAll() {
        List<Post> posts = postMapper.findAllOrderByRecentActivity();
        // 为每个帖子加载图片URL
        for (Post post : posts) {
            post.setImageUrls(imageMapper.findUrlsByPostId(post.getId()));
        }
        return posts;
    }

    /**
     * 分页列出帖子
     */
    @Override
    public List<Post> listAllWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.findAllOrderByRecentActivityWithPagination(offset, size);
        // 为每个帖子加载图片URL
        for (Post post : posts) {
            post.setImageUrls(imageMapper.findUrlsByPostId(post.getId()));
        }
        return posts;
    }

    /**
     * 获取帖子总数
     */
    @Override
    public long getTotalPostCount() {
        return postMapper.countAllPosts();
    }
    
    /**
     * 列出最近的帖子
     */
    @Override
    public List<Post> listRecent() {
        List<Post> allPosts = postMapper.findAllOrderByRecentActivity();
        return allPosts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(10) // 假设最近的帖子是最新创建的10个
                .collect(Collectors.toList());
    }

    /**
     * 创建新帖子，同时保存图片 URL 列表
     */
    @Override
    public void create(Post post, List<String> imageUrls) {
        post.setCreatedAt(LocalDateTime.now());
        postMapper.insert(post);
        Long id = post.getId();
        for (int i = 0; i < imageUrls.size(); i++) {
            imageMapper.insert(id, imageUrls.get(i), i);
        }
    }

    /**
     * 删除帖子。只有作者本人或管理员可以删除。
     */
    @Override
    public void delete(Long postId, Long currentUserId, boolean isAdmin) {
        Post post = postMapper.findById(postId);
        if (!isAdmin && !post.getUserId().equals(currentUserId)) {
            throw new SecurityException("无权限删除此帖子");
        }
        // 先删点赞
        likeMapper.deleteByPostId(postId);
        // 再删图片
        imageMapper.deleteByPostId(postId);
        // 最后删帖子
        postMapper.delete(postId);
    }

    /**
     * 点赞
     */
    @Override
    public void like(Long postId, Long userId) {
        if (!likeMapper.existsByPostIdAndUserId(postId, userId)) {
            likeMapper.insert(postId, userId);
        }
    }

    /**
     * 取消点赞
     */
    @Override
    public void unlike(Long postId, Long userId) {
        likeMapper.delete(postId, userId);
    }

    /**
     * 判断是否已点赞
     */
    @Override
    public boolean liked(Long postId, Long userId) {
        return likeMapper.existsByPostIdAndUserId(postId, userId);
    }

    /**
     * 查询点赞数量
     */
    @Override
    public int countLikes(Long postId) {
        return likeMapper.countByPostId(postId);
    }

    /**
     * 获取帖子最后活跃时间（创建或最新评论）
     */
    @Override
    public LocalDateTime getLastActivity(Long postId) {
        // 由于PostMapper中没有findLastActivity方法，这里返回帖子的创建时间
        // 后续需要在PostMapper中添加相应方法或修改此实现
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 如果lastActivity为null，则返回createdAt
            return post.getLastActivity() != null ? post.getLastActivity() : post.getCreatedAt();
        }
        return LocalDateTime.now();
    }

    /**
     * 获取帖子所有图片 URL
     */
    @Override
    public List<String> getImageUrls(Long postId) {
        return imageMapper.findUrlsByPostId(postId);
    }
    
    /**
     * 根据用户名查询帖子
     */
    @Override
    public List<Post> getPostsByUsername(String username) {
        List<Post> posts = postMapper.findByUsername(username);
        // 为每个帖子加载图片URL
        for (Post post : posts) {
            post.setImageUrls(imageMapper.findUrlsByPostId(post.getId()));
        }
        return posts;
    }
    
    /**
     * 搜索帖子
     */
    @Override
    public List<Post> searchPosts(String query) {
        List<Post> posts = postMapper.searchPosts(query);
        // 为每个帖子加载图片URL
        for (Post post : posts) {
            post.setImageUrls(imageMapper.findUrlsByPostId(post.getId()));
        }
        return posts;
    }

    /**
     * 管理员获取帖子列表
     */
    @Override
    public List<Post> getAllPostsForAdmin(int page, int size, String search, String status) {
        int offset = page * size;
        List<Post> posts;
        
        if (search != null && !search.trim().isEmpty()) {
            // 如果有搜索条件，使用搜索
            posts = new ArrayList<>(); // 占位，需实现分页搜索
        } else {
            // 否则获取所有帖子
            posts = postMapper.findAllOrderByRecentActivityWithPagination(offset, size);
        }
        
        // 为每个帖子加载图片URL
        for (Post post : posts) {
            post.setImageUrls(imageMapper.findUrlsByPostId(post.getId()));
        }
        
        return posts;
    }

    /**
     * 删除帖子
     */
    @Override
    public void deletePost(Long postId) {
        // 先删除帖子相关的图片
        imageMapper.deleteByPostId(postId);
        // 删除帖子的点赞记录
        likeMapper.deleteByPostId(postId);
        // 最后删除帖子本身
        postMapper.delete(postId);
    }
}
