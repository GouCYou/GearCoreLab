package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    /** 新增：作者用户名，用于详情页显示 */
    private String authorUsername;
    /** 新增：作者头像链接，用于详情页显示 */
    private String authorAvatarUrl;
    /** 新增：最后活跃时间（发帖或最新评论时间），用于列表排序 */
    private LocalDateTime lastActivity;
    /** 新增：是否已点赞（当前登录用户） */
    private boolean liked;
    /** 新增：点赞总数 */
    private int likeCount;
    /** 新增：帖子关联的图片列表 */
    private List<String> imageUrls;
    /** 新增：评论总数 */
    private Integer commentCount;
    
    /** 新增：关联的配置单ID，可为空 */
    private Long configId;
    
    // 手动添加getter和setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getAuthorUsername() {
        return authorUsername;
    }
    
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
    
    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }
    
    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }
    
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    
    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    public boolean isLiked() {
        return liked;
    }
    
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public Integer getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
    
    public Long getConfigId() {
        return configId;
    }
    
    public void setConfigId(Long configId) {
        this.configId = configId;
    }
}
