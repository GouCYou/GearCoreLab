package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private String imageUrl;      // 单张图片URL
    private List<String> imageUrls; // 多张图片URL列表
    private LocalDateTime createdAt;

    /** 评论人用户名，用于详情页显示 */
    private String authorUsername;
    /** 评论人头像链接，用于详情页显示 */
    private String authorAvatarUrl;
    /** 是否已点赞（当前登录用户） */
    private boolean liked;
    /** 点赞总数 */
    private int likeCount;
    /** 评论所属帖子标题，用于个人中心显示 */
    private String postTitle;
}
