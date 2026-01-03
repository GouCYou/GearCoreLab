package cn.gcc.gearcorelab.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostImageMapper {
    List<String> findUrlsByPostId(@Param("postId") Long postId);
    void insert(@Param("postId") Long postId,
                @Param("imageUrl") String imageUrl,
                @Param("sortOrder") Integer sortOrder);
    void deleteByPostId(@Param("postId") Long postId);
}
