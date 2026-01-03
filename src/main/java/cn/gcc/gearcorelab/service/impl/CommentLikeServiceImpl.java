package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.CommentLikeMapper;
import cn.gcc.gearcorelab.service.CommentLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeMapper mapper;

    public CommentLikeServiceImpl(CommentLikeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean liked(Long commentId, Long userId) {
        return mapper.existsByCommentIdAndUserId(commentId, userId);
    }

    @Override
    @Transactional
    public void like(Long commentId, Long userId) {
        synchronized ((commentId + "-" + userId).intern()) {
            if (!liked(commentId, userId)) {
                mapper.insert(commentId, userId);
            }
        }
    }

    @Override
    @Transactional
    public void unlike(Long commentId, Long userId) {
        if (liked(commentId, userId)) {
            mapper.delete(commentId, userId);
        }
    }

    @Override
    public int countLikes(Long commentId) {
        Integer c = mapper.countByCommentId(commentId);
        return c != null ? c : 0;
    }
}
