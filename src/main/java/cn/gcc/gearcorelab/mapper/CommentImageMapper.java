package cn.gcc.gearcorelab.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentImageMapper {
    String findUrlByCommentId(@Param("commentId") Long commentId);
    void insert(@Param("commentId") Long commentId,
                @Param("imageUrl") String imageUrl);
    void deleteByCommentId(@Param("commentId") Long commentId);
}
