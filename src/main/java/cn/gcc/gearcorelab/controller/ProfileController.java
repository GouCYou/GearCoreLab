package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.model.Comment;
import cn.gcc.gearcorelab.service.UserService;
import cn.gcc.gearcorelab.service.PostService;
import cn.gcc.gearcorelab.service.CommentService;
import cn.gcc.gearcorelab.service.UserFollowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class ProfileController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserFollowService userFollowService;
    
    @Value("${upload.directory}")
    private String uploadDir;

    public ProfileController(UserService userService, PostService postService, CommentService commentService, UserFollowService userFollowService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.userFollowService = userFollowService;
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model, HttpSession session) {
        String username = auth.getName();
        User user = userService.getByUsername(username);
        
        // 获取用户发布的帖子
        List<Post> userPosts = postService.getPostsByUsername(username);
        
        // 获取用户的评论
        List<Comment> userComments = commentService.getCommentsByUsername(username);
        
        // 计算注册天数
        long days = 0;
        if (user != null && user.getCreatedAt() != null) {
            days = ChronoUnit.DAYS.between(user.getCreatedAt().toLocalDate(), LocalDate.now());
        }
        
        model.addAttribute("user", user);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userComments", userComments);
        model.addAttribute("days", days);
        session.setAttribute("user", user);
        return "profile";
    }
    
    @GetMapping("/user/{userId}")
    public String userProfile(@PathVariable Long userId, Model model, Authentication auth) {
        User user = userService.getById(userId);
        if (user == null) {
            return "redirect:/404";
        }
        
        // 获取用户发布的帖子
        List<Post> userPosts = postService.getPostsByUsername(user.getUsername());
        
        // 获取用户的评论
        List<Comment> userComments = commentService.getCommentsByUsername(user.getUsername());
        
        // 计算注册天数
        long days = 0;
        if (user.getCreatedAt() != null) {
            days = ChronoUnit.DAYS.between(user.getCreatedAt().toLocalDate(), LocalDate.now());
        }
        
        // 检查是否是当前用户自己的页面
        boolean isOwnProfile = false;
        if (auth != null) {
            User currentUser = userService.getByUsername(auth.getName());
            isOwnProfile = currentUser != null && currentUser.getId().equals(userId);
        }
        
        model.addAttribute("user", user);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userComments", userComments);
        model.addAttribute("days", days);
        model.addAttribute("isOwnProfile", isOwnProfile);
        
        return "user/profile";
    }
    
    @GetMapping("/user/{userId}/following")
    public String userFollowing(@PathVariable Long userId, Model model) {
        User targetUser = userService.getById(userId);
        if (targetUser == null) {
            return "redirect:/404";
        }
        
        List<User> followingUsers = userFollowService.getFollowings(userId);
        int followingCount = userFollowService.getFollowingCount(userId);
        int followerCount = userFollowService.getFollowerCount(userId);
        
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("users", followingUsers);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("pageTitle", "关注列表");
        model.addAttribute("listType", "关注");
        
        return "user/follow-list";
    }
    
    @GetMapping("/user/{userId}/followers")
    public String userFollowers(@PathVariable Long userId, Model model) {
        User targetUser = userService.getById(userId);
        if (targetUser == null) {
            return "redirect:/404";
        }
        
        List<User> followerUsers = userFollowService.getFollowers(userId);
        int followingCount = userFollowService.getFollowingCount(userId);
        int followerCount = userFollowService.getFollowerCount(userId);
        
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("users", followerUsers);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("pageTitle", "粉丝列表");
        model.addAttribute("listType", "粉丝");
        
        return "user/follow-list";
    }
    
    @PostMapping("/profile/upload-avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("avatar") MultipartFile file,
            Authentication auth,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "只能上传图片文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件大小 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "文件大小不能超过5MB");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = "avatar_" + UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            // 更新用户头像URL
            String avatarUrl = "/uploads/" + filename;
            User user = userService.getByUsername(auth.getName());
            user.setAvatarUrl(avatarUrl);
            userService.save(user);
            
            // 更新session中的用户信息
            session.setAttribute("user", user);
            
            response.put("success", true);
            response.put("avatarUrl", avatarUrl);
            response.put("message", "头像上传成功");
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件保存失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping("/profile/update-signature")
    public ResponseEntity<Map<String, Object>> updateSignature(
            @RequestParam("signature") String signature,
            Authentication auth,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userService.getByUsername(auth.getName());
            user.setSignature(signature);
            userService.save(user);
            
            // 更新session中的用户信息
            session.setAttribute("user", user);
            
            response.put("success", true);
            response.put("message", "个性签名更新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping("/profile/upload-background")
    public ResponseEntity<Map<String, Object>> uploadBackground(
            @RequestParam("background") MultipartFile file,
            Authentication auth,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("success", false);
                response.put("message", "只能上传图片文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件大小 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                response.put("success", false);
                response.put("message", "文件大小不能超过10MB");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = "background_" + UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            // 更新用户背景图片URL
            String backgroundUrl = "/uploads/" + filename;
            User user = userService.getByUsername(auth.getName());
            user.setBackgroundUrl(backgroundUrl);
            userService.save(user);
            
            // 更新session中的用户信息
            session.setAttribute("user", user);
            
            response.put("success", true);
            response.put("backgroundUrl", backgroundUrl);
            response.put("message", "背景图片上传成功");
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件保存失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}