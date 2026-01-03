package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.model.Comment;
import cn.gcc.gearcorelab.service.UserService;
import cn.gcc.gearcorelab.service.PostService;
import cn.gcc.gearcorelab.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public UserController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/user/profile/{username}")
    public String userProfile(@PathVariable String username, Model model, Authentication auth) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return "redirect:/";
        }
        
        // 如果访问的是当前登录用户自己的页面，跳转到个人中心
        if (auth != null && auth.isAuthenticated() && username.equals(auth.getName())) {
            return "redirect:/profile";
        }
        
        // 获取用户发布的帖子
        List<Post> userPosts = postService.getPostsByUsername(username);
        
        // 获取用户的评论
        List<Comment> userComments = commentService.getCommentsByUsername(username);
        
        model.addAttribute("user", user);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userComments", userComments);
        model.addAttribute("isOwnProfile", false);
        
        return "user/profile";
    }
    
    /**
     * 获取当前登录用户信息的API端点
     */
    @GetMapping("/api/user/current")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户未登录");
                return ResponseEntity.status(401).body(response);
            }
            
            response.put("success", true);
            response.put("data", user);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}