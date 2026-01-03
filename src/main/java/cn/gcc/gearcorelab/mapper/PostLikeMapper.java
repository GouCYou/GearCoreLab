package cn.gcc.gearcorelab.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {
    /** 点赞数 */
    int countByPostId(@Param("postId") Long postId);

    /** 是否已点赞 */
    boolean existsByPostIdAndUserId(@Param("postId") Long postId,
                                    @Param("userId") Long userId);

    /** 点赞 */
    void insert(@Param("postId") Long postId,
                @Param("userId") Long userId);

    /** 取消点赞 */
    void delete(@Param("postId") Long postId,
                @Param("userId") Long userId);

    /** 删除某贴所有点赞（用于删贴时） */
    void deleteByPostId(@Param("postId") Long postId);
}
