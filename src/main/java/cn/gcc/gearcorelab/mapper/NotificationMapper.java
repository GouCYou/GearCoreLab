package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    
    /**
     * 根据用户名查询通知列表
     */
    List<Notification> findByUsername(@Param("username") String username);
    
    /**
     * 根据ID查询通知
     */
    Notification findById(@Param("id") Long id);
    
    /**
     * 统计用户未读通知数量
     */
    int countUnreadByUsername(@Param("username") String username);
    
    /**
     * 插入新通知
     */
    void insert(Notification notification);
    
    /**
     * 标记通知为已读
     */
    void markAsRead(@Param("id") Long id);
    
    /**
     * 标记用户所有通知为已读
     */
    void markAllAsReadByUserId(@Param("userId") Long userId);
    
    /**
     * 删除通知
     */
    void delete(@Param("id") Long id);
    
    /**
     * 获取所有通知（管理员用）
     */
    List<Notification> findAll();
    
    /**
     * 根据ID删除通知（管理员用）
     */
    void deleteById(@Param("id") Long id);
}