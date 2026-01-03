package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.MessageConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageConversationMapper {
    
    /**
     * 插入会话记录
     */
    void insert(MessageConversation conversation);
    
    /**
     * 更新会话记录
     */
    void update(MessageConversation conversation);
    
    /**
     * 根据两个用户ID查询会话
     */
    MessageConversation findByUsers(
        @Param("user1Id") Long user1Id,
        @Param("user2Id") Long user2Id
    );
    
    /**
     * 查询用户的所有会话列表
     */
    List<MessageConversation> findConversationsByUser(
        @Param("userId") Long userId,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    /**
     * 统计用户的会话总数
     */
    int countConversationsByUser(@Param("userId") Long userId);
    
    /**
     * 更新未读数量
     */
    void updateUnreadCount(
        @Param("user1Id") Long user1Id,
        @Param("user2Id") Long user2Id,
        @Param("user1UnreadCount") Integer user1UnreadCount,
        @Param("user2UnreadCount") Integer user2UnreadCount
    );
    
    /**
     * 删除会话
     */
    void deleteByUsers(
        @Param("user1Id") Long user1Id,
        @Param("user2Id") Long user2Id
    );
}