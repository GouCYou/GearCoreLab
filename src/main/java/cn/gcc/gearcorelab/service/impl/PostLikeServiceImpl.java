package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.PostLikeMapper;
import cn.gcc.gearcorelab.service.PostLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeMapper likeMapper;

    public PostLikeServiceImpl(PostLikeMapper likeMapper) {
        this.likeMapper = likeMapper;
    }

    @Override
    public boolean liked(Long postId, Long userId) {
        // 调用 existsByPostIdAndUserId
        return likeMapper.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    @Transactional
    public void like(Long postId, Long userId) {
        if (!liked(postId, userId)) {
            likeMapper.insert(postId, userId);
        }
    }

    @Override
    @Transactional
    public void unlike(Long postId, Long userId) {
        if (liked(postId, userId)) {
            likeMapper.delete(postId, userId);
        }
    }

    @Override
    public int countLikes(Long postId) {
        Integer cnt = likeMapper.countByPostId(postId);
        return cnt != null ? cnt : 0;
    }
}
