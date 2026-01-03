package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.Notification;
import java.util.List;

public interface NotificationService {
    
    /**
     * 根据用户名获取通知列表
     */
    List<Notification> getNotificationsByUsername(String username);
    
    /**
     * 获取用户未读通知数量
     */
    int getUnreadCount(String username);
    
    /**
     * 标记通知为已读
     */
    void markAsRead(Long notificationId, String username);
    
    /**
     * 标记所有通知为已读
     */
    void markAllAsRead(String username);
    
    /**
     * 删除通知
     */
    void deleteNotification(Long notificationId, String username);
    
    /**
     * 创建系统通知
     */
    void createSystemNotification(String title, String content, String targetUsername);
    
    /**
     * 创建管理员通知（发送给所有用户）
     */
    void createAdminNotification(String title, String content, String senderUsername);
    
    /**
     * 创建点赞通知
     */
    void createLikeNotification(String targetUsername, String likerUsername, Long postId);
    
    /**
     * 创建评论通知
     */
    void createCommentNotification(String targetUsername, String commenterUsername, Long postId, Long commentId);
    
    /**
     * 获取所有通知（管理员用）
     */
    List<Notification> getAllNotifications();
    
    /**
     * 根据ID删除通知（管理员用）
     */
    void deleteNotificationById(Long notificationId);
}