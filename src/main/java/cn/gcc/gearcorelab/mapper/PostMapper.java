package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> findAllOrderByRecentActivity();
    // 新增分页查询方法
    List<Post> findAllOrderByRecentActivityWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    int countAllPosts();
    void insert(Post post);
    // 此方法在PostServiceImpl中未使用，可能是遗留代码
    void insertImage(@Param("postId") Long postId, @Param("url") String url);
    Post findById(Long id);
    void delete(Long id);
    /** 新增：删除某帖所有图片记录 */
    void deleteImagesByPostId(Long postId);
    
    /** 根据用户名查询帖子 */
    List<Post> findByUsername(String username);
    
    /** 搜索帖子 */
    List<Post> searchPosts(String query);
}
