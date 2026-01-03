package cn.gcc.gearcorelab.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikeMapper {
    /** 点赞数 */
    int countByCommentId(@Param("commentId") Long commentId);

    /** 是否已点赞 */
    boolean existsByCommentIdAndUserId(@Param("commentId") Long commentId,
                                       @Param("userId") Long userId);

    /** 点赞 */
    void insert(@Param("commentId") Long commentId,
                @Param("userId") Long userId);

    /** 取消点赞 */
    void delete(@Param("commentId") Long commentId,
                @Param("userId") Long userId);

    /** 删除某评论所有点赞（删评时） */
    void deleteByCommentId(@Param("commentId") Long commentId);
}
