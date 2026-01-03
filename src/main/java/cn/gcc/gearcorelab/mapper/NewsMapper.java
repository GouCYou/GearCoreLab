package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper {
    List<News> findAllOrderByPublishTime();
    List<News> findAllOrderByPublishTimeWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    int countAllNews();
    void insert(News news);
    News findById(Long id);
    void update(News news);
    void delete(Long id);
    
    void incrementViewCount(Long id);
    
    /**
     * 搜索新闻
     */
    List<News> searchNews(@Param("query") String query);
}