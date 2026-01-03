package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.NewsMapper;
import cn.gcc.gearcorelab.model.News;
import cn.gcc.gearcorelab.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    
    @Autowired
    private NewsMapper newsMapper;
    
    @Override
    public List<News> listAll() {
        return newsMapper.findAllOrderByPublishTime();
    }
    
    @Override
    public List<News> listAllWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return newsMapper.findAllOrderByPublishTimeWithPagination(offset, size);
    }
    
    @Override
    public long getTotalNewsCount() {
        return newsMapper.countAllNews();
    }
    
    @Override
    public void create(News news) {
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());
        if (news.getPublishTime() == null) {
            news.setPublishTime(LocalDateTime.now());
        }
        if (news.getViewCount() == null) {
            news.setViewCount(0);
        }
        if (news.getAuthor() == null || news.getAuthor().isEmpty()) {
            news.setAuthor("管理员");
        }
        if (news.getStatus() == null || news.getStatus().isEmpty()) {
            news.setStatus("published");
        }
        newsMapper.insert(news);
    }
    
    @Override
    public News findById(Long id) {
        return newsMapper.findById(id);
    }
    
    @Override
    public void update(News news) {
        news.setUpdatedAt(LocalDateTime.now());
        newsMapper.update(news);
    }
    
    @Override
    public void delete(Long id) {
        newsMapper.delete(id);
    }
    
    @Override
    public void incrementViewCount(Long id) {
        newsMapper.incrementViewCount(id);
    }

    @Override
    public List<News> getAllNews() {
        return newsMapper.findAllOrderByPublishTime();
    }

    @Override
    public void deleteById(Long id) {
        newsMapper.delete(id);
    }
    
    @Override
    public List<News> searchNews(String query) {
        return newsMapper.searchNews(query);
    }
}