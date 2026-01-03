package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.News;
import cn.gcc.gearcorelab.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsApiController {
    
    @Autowired
    private NewsService newsService;
    
    // 获取新闻详情的API端点
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNewsDetail(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            News news = newsService.findById(id);
            if (news == null) {
                response.put("success", false);
                response.put("message", "新闻不存在");
                return ResponseEntity.notFound().build();
            }
            
            // 增加浏览次数
            newsService.incrementViewCount(id);
            
            response.put("success", true);
            response.put("data", news);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取新闻详情失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}