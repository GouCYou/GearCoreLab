package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.UserFollowMapper;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.UserFollow;
import cn.gcc.gearcorelab.service.UserFollowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserFollowServiceImpl implements UserFollowService {
    
    private final UserFollowMapper userFollowMapper;
    
    public UserFollowServiceImpl(UserFollowMapper userFollowMapper) {
        this.userFollowMapper = userFollowMapper;
    }
    
    @Override
    public boolean followUser(Long followerId, Long followingId) {
        // 不能关注自己
        if (followerId.equals(followingId)) {
            return false;
        }
        
        // 检查是否已经关注
        if (isFollowing(followerId, followingId)) {
            return false;
        }
        
        try {
            UserFollow userFollow = new UserFollow(followerId, followingId);
            userFollow.setCreatedAt(LocalDateTime.now());
            userFollowMapper.insert(userFollow);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean unfollowUser(Long followerId, Long followingId) {
        try {
            userFollowMapper.delete(followerId, followingId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        UserFollow follow = userFollowMapper.findByFollowerAndFollowing(followerId, followingId);
        return follow != null;
    }
    
    @Override
    public List<User> getFollowings(Long userId) {
        return userFollowMapper.findFollowingsByUserId(userId);
    }
    
    @Override
    public List<User> getFollowers(Long userId) {
        return userFollowMapper.findFollowersByUserId(userId);
    }
    
    @Override
    public int getFollowingCount(Long userId) {
        return userFollowMapper.countFollowingsByUserId(userId);
    }
    
    @Override
    public int getFollowerCount(Long userId) {
        return userFollowMapper.countFollowersByUserId(userId);
    }
    
    @Override
    public boolean isMutualFollow(Long userId1, Long userId2) {
        return userFollowMapper.isMutualFollow(userId1, userId2);
    }
}