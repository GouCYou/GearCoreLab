package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.News;

import java.util.List;

/**
 * 科技新闻业务接口
 */
public interface NewsService {
    List<News> listAll();
    List<News> getAllNews();
    List<News> listAllWithPagination(int page, int size);
    long getTotalNewsCount();
    void create(News news);
    News findById(Long id);
    void update(News news);
    void delete(Long id);
    void incrementViewCount(Long id);
    void deleteById(Long id);
    
    /**
     * 搜索新闻
     */
    List<News> searchNews(String query);
}