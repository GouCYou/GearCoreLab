package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.NotificationMapper;
import cn.gcc.gearcorelab.mapper.UserMapper;
import cn.gcc.gearcorelab.model.Notification;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Notification> getNotificationsByUsername(String username) {
        System.out.println("查询用户 " + username + " 的通知");
        List<Notification> notifications = notificationMapper.findByUsername(username);
        System.out.println("查询结果：找到 " + notifications.size() + " 条通知");
        for (Notification notification : notifications) {
            System.out.println("通知详情 - ID: " + notification.getId() + ", 标题: " + notification.getTitle() + ", 用户ID: " + notification.getUserId());
        }
        return notifications;
    }

    @Override
    public int getUnreadCount(String username) {
        return notificationMapper.countUnreadByUsername(username);
    }

    @Override
    public void markAsRead(Long notificationId, String username) {
        // 验证通知是否属于当前用户
        Notification notification = notificationMapper.findById(notificationId);
        if (notification != null) {
            User user = userMapper.findByUsername(username);
            if (user != null && notification.getUserId().equals(user.getId())) {
                notificationMapper.markAsRead(notificationId);
            } else {
                throw new RuntimeException("无权限操作此通知");
            }
        } else {
            throw new RuntimeException("通知不存在");
        }
    }

    @Override
    public void markAllAsRead(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            notificationMapper.markAllAsReadByUserId(user.getId());
        }
    }

    @Override
    public void deleteNotification(Long notificationId, String username) {
        // 验证通知是否属于当前用户
        Notification notification = notificationMapper.findById(notificationId);
        if (notification != null) {
            User user = userMapper.findByUsername(username);
            if (user != null && notification.getUserId().equals(user.getId())) {
                notificationMapper.delete(notificationId);
            } else {
                throw new RuntimeException("无权限删除此通知");
            }
        } else {
            throw new RuntimeException("通知不存在");
        }
    }

    @Override
    public void createSystemNotification(String title, String content, String targetUsername) {
        System.out.println("创建系统通知 - 目标用户: " + targetUsername);
        if (targetUsername == null || targetUsername.trim().isEmpty()) {
            // 如果targetUsername为空，发送给所有用户
            List<User> allUsers = userMapper.findAll();
            System.out.println("发送给所有用户，用户数量: " + allUsers.size());
            for (User u : allUsers) {
                System.out.println("用户信息 - ID: " + u.getId() + ", 用户名: " + u.getUsername());
            }
            for (User user : allUsers) {
                Notification notification = new Notification();
                notification.setUserId(user.getId());
                notification.setTitle(title);
                notification.setContent(content);
                notification.setType(Notification.NotificationType.SYSTEM.name());
                notification.setRead(false);
                notification.setCreatedAt(LocalDateTime.now());
                
                System.out.println("准备为用户 " + user.getUsername() + " (ID: " + user.getId() + ") 创建通知");
                notificationMapper.insert(notification);
                System.out.println("为用户 " + user.getUsername() + " 创建系统通知成功，通知ID: " + notification.getId());
            }
        } else {
            // 发送给指定用户
            User user = userMapper.findByUsername(targetUsername);
            if (user != null) {
                Notification notification = new Notification();
                notification.setUserId(user.getId());
                notification.setTitle(title);
                notification.setContent(content);
                notification.setType(Notification.NotificationType.SYSTEM.name());
                notification.setRead(false);
                notification.setCreatedAt(LocalDateTime.now());
                
                notificationMapper.insert(notification);
                System.out.println("为指定用户 " + user.getUsername() + " 创建系统通知成功");
            } else {
                System.out.println("未找到指定用户: " + targetUsername);
            }
        }
    }

    @Override
    public void createAdminNotification(String title, String content, String senderUsername) {
        System.out.println("创建管理员通知 - 发送者: " + senderUsername);
        // 获取所有用户
        List<User> allUsers = userMapper.findAll();
        System.out.println("发送给所有用户，用户数量: " + allUsers.size());
        User sender = userMapper.findByUsername(senderUsername);
        
        for (User user : allUsers) {
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(Notification.NotificationType.ADMIN.name());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            
            if (sender != null) {
                notification.setSenderUsername(sender.getUsername());
                notification.setSenderAvatarUrl(sender.getAvatarUrl());
            }
            
            notificationMapper.insert(notification);
            System.out.println("为用户 " + user.getUsername() + " 创建管理员通知成功");
        }
    }

    @Override
    public void createLikeNotification(String targetUsername, String likerUsername, Long postId) {
        User targetUser = userMapper.findByUsername(targetUsername);
        User liker = userMapper.findByUsername(likerUsername);
        
        if (targetUser != null && liker != null && !targetUsername.equals(likerUsername)) {
            Notification notification = new Notification();
            notification.setUserId(targetUser.getId());
            notification.setTitle("点赞通知");
            notification.setContent(liker.getUsername() + " 点赞了您的帖子");
            notification.setType(Notification.NotificationType.LIKE.name());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSenderUsername(liker.getUsername());
            notification.setSenderAvatarUrl(liker.getAvatarUrl());
            notification.setRelatedPostId(postId);
            
            notificationMapper.insert(notification);
        }
    }

    @Override
    public void createCommentNotification(String targetUsername, String commenterUsername, Long postId, Long commentId) {
        User targetUser = userMapper.findByUsername(targetUsername);
        User commenter = userMapper.findByUsername(commenterUsername);
        
        if (targetUser != null && commenter != null && !targetUsername.equals(commenterUsername)) {
            Notification notification = new Notification();
            notification.setUserId(targetUser.getId());
            notification.setTitle("评论通知");
            notification.setContent(commenter.getUsername() + " 评论了您的帖子");
            notification.setType(Notification.NotificationType.COMMENT.name());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSenderUsername(commenter.getUsername());
            notification.setSenderAvatarUrl(commenter.getAvatarUrl());
            notification.setRelatedPostId(postId);
            notification.setRelatedCommentId(commentId);
            
            notificationMapper.insert(notification);
        }
    }
    
    @Override
    public List<Notification> getAllNotifications() {
        return notificationMapper.findAll();
    }
    
    @Override
    public void deleteNotificationById(Long notificationId) {
        notificationMapper.deleteById(notificationId);
    }
}