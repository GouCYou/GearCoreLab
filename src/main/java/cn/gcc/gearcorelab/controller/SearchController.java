package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.News;
import cn.gcc.gearcorelab.service.PostService;
import cn.gcc.gearcorelab.service.UserService;
import cn.gcc.gearcorelab.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;

@Controller
public class SearchController {

    private final PostService postService;
    private final UserService userService;
    private final NewsService newsService;

    public SearchController(PostService postService, UserService userService, NewsService newsService) {
        this.postService = postService;
        this.userService = userService;
        this.newsService = newsService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Post> posts = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<News> news = new ArrayList<>();
        
        if (query != null && !query.trim().isEmpty()) {
            // 搜索帖子（标题和内容）
            posts = postService.searchPosts(query.trim());
            
            // 搜索用户（用户名）
            users = userService.searchUsers(query.trim());
            
            // 搜索新闻（标题和内容）
            news = newsService.searchNews(query.trim());
        }
        
        model.addAttribute("query", query);
        model.addAttribute("posts", posts);
        model.addAttribute("users", users);
        model.addAttribute("news", news);
        model.addAttribute("totalResults", posts.size() + users.size() + news.size());
        
        return "search";
    }
}