package cn.gcc.gearcorelab.model;

import java.time.LocalDateTime;

public class MessageConversation {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private Long lastMessageId;
    private LocalDateTime lastMessageTime;
    private Integer user1UnreadCount;
    private Integer user2UnreadCount;
    
    // 关联字段
    private String user1Username;
    private String user1AvatarUrl;
    private String user2Username;
    private String user2AvatarUrl;
    private String lastMessageContent;
    
    public MessageConversation() {}
    
    public MessageConversation(Long user1Id, Long user2Id) {
        this.user1Id = Math.min(user1Id, user2Id);
        this.user2Id = Math.max(user1Id, user2Id);
        this.lastMessageTime = LocalDateTime.now();
        this.user1UnreadCount = 0;
        this.user2UnreadCount = 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUser1Id() {
        return user1Id;
    }
    
    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }
    
    public Long getUser2Id() {
        return user2Id;
    }
    
    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }
    
    public Long getLastMessageId() {
        return lastMessageId;
    }
    
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
    
    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }
    
    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
    
    public Integer getUser1UnreadCount() {
        return user1UnreadCount;
    }
    
    public void setUser1UnreadCount(Integer user1UnreadCount) {
        this.user1UnreadCount = user1UnreadCount;
    }
    
    public Integer getUser2UnreadCount() {
        return user2UnreadCount;
    }
    
    public void setUser2UnreadCount(Integer user2UnreadCount) {
        this.user2UnreadCount = user2UnreadCount;
    }
    
    public String getUser1Username() {
        return user1Username;
    }
    
    public void setUser1Username(String user1Username) {
        this.user1Username = user1Username;
    }
    
    public String getUser1AvatarUrl() {
        return user1AvatarUrl;
    }
    
    public void setUser1AvatarUrl(String user1AvatarUrl) {
        this.user1AvatarUrl = user1AvatarUrl;
    }
    
    public String getUser2Username() {
        return user2Username;
    }
    
    public void setUser2Username(String user2Username) {
        this.user2Username = user2Username;
    }
    
    public String getUser2AvatarUrl() {
        return user2AvatarUrl;
    }
    
    public void setUser2AvatarUrl(String user2AvatarUrl) {
        this.user2AvatarUrl = user2AvatarUrl;
    }
    
    public String getLastMessageContent() {
        return lastMessageContent;
    }
    
    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }
    
    /**
     * 获取对方用户ID
     */
    public Long getOtherUserId(Long currentUserId) {
        return currentUserId.equals(user1Id) ? user2Id : user1Id;
    }
    
    /**
     * 获取对方用户名
     */
    public String getOtherUsername(Long currentUserId) {
        return currentUserId.equals(user1Id) ? user2Username : user1Username;
    }
    
    /**
     * 获取对方头像
     */
    public String getOtherAvatarUrl(Long currentUserId) {
        return currentUserId.equals(user1Id) ? user2AvatarUrl : user1AvatarUrl;
    }
    
    /**
     * 获取当前用户的未读数量
     */
    public Integer getUnreadCount(Long currentUserId) {
        return currentUserId.equals(user1Id) ? user1UnreadCount : user2UnreadCount;
    }
}