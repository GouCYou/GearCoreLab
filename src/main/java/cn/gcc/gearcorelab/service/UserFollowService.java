package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.User;

import java.util.List;

public interface UserFollowService {
    
    /**
     * 关注用户
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否成功
     */
    boolean followUser(Long followerId, Long followingId);
    
    /**
     * 取消关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否成功
     */
    boolean unfollowUser(Long followerId, Long followingId);
    
    /**
     * 检查是否已关注
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注
     */
    boolean isFollowing(Long followerId, Long followingId);
    
    /**
     * 获取用户关注的人列表
     * @param userId 用户ID
     * @return 关注列表
     */
    List<User> getFollowings(Long userId);
    
    /**
     * 获取用户的粉丝列表
     * @param userId 用户ID
     * @return 粉丝列表
     */
    List<User> getFollowers(Long userId);
    
    /**
     * 获取用户关注数量
     * @param userId 用户ID
     * @return 关注数量
     */
    int getFollowingCount(Long userId);
    
    /**
     * 获取用户粉丝数量
     * @param userId 用户ID
     * @return 粉丝数量
     */
    int getFollowerCount(Long userId);
    
    /**
     * 检查两个用户是否互相关注
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 是否互相关注
     */
    boolean isMutualFollow(Long userId1, Long userId2);
}