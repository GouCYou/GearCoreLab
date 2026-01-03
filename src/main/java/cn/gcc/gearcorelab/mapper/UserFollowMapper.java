package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.UserFollow;
import cn.gcc.gearcorelab.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowMapper {
    
    /**
     * 添加关注关系
     */
    void insert(UserFollow userFollow);
    
    /**
     * 删除关注关系
     */
    void delete(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 检查是否已关注
     */
    UserFollow findByFollowerAndFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 获取用户的关注列表
     */
    List<User> findFollowingsByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户的粉丝列表
     */
    List<User> findFollowersByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户关注数量
     */
    int countFollowingsByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户粉丝数量
     */
    int countFollowersByUserId(@Param("userId") Long userId);
    
    /**
     * 检查两个用户是否互相关注
     */
    boolean isMutualFollow(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}