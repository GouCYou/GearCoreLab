package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String type; // SYSTEM, ADMIN, USER
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    
    // 发送者信息（用于管理员通知）
    private String senderUsername;
    private String senderAvatarUrl;
    
    // 关联信息（如帖子ID、评论ID等）
    private Long relatedPostId;
    private Long relatedCommentId;
    
    // 通知类型枚举
    public enum NotificationType {
        SYSTEM("系统通知"),
        ADMIN("管理员通知"),
        LIKE("点赞通知"),
        COMMENT("评论通知"),
        REPLY("回复通知");
        
        private final String description;
        
        NotificationType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}