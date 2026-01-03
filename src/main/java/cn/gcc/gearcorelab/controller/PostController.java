// src/main/java/cn/gcc/gearcorelab/controller/PostController.java
package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.Comment;
import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.CommentLikeService;
import cn.gcc.gearcorelab.service.CommentService;
import cn.gcc.gearcorelab.service.PostService;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final UserService userService;

    public PostController(PostService postService,
                          CommentService commentService,
                          CommentLikeService commentLikeService,
                          UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
        this.userService = userService;
    }

    /**
     * 社区首页：帖列表
     */
    @GetMapping("")
    public String list(Model model, Authentication auth, 
                      @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10; // 每页10个帖子
        
        // 获取分页数据
        List<Post> posts = postService.listAllWithPagination(page, pageSize);
        long totalPosts = postService.getTotalPostCount();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
        
        Long currentUserId = null;
        if (auth != null) {
            User u = userService.getByUsername(auth.getName());
            currentUserId = (u != null ? u.getId() : null);
        }
        // 为每个帖子设置当前用户是否点赞
        if (currentUserId != null) {
            for (Post post : posts) {
                boolean liked = postService.liked(post.getId(), currentUserId);
                post.setLiked(liked);
            }
        }
        
        model.addAttribute("posts", posts);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPosts", totalPosts);
        
        return "community/list";
    }

    /**
     * 发帖页面
     */
    @GetMapping("/create")
    public String showCreate() {
        return "community/create";
    }

    /**
     * 处理发帖请求
     */
    @PostMapping("/create")
    public String create(@RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @RequestParam(value = "configId", required = false) Long configId,
                         @RequestParam("images") MultipartFile[] images,
                         Authentication auth) throws IOException {
        // 获取当前登录用户
        if (auth == null) {
            return "redirect:/login";
        }
        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return "redirect:/login";
        }

        // 构造 Post 对象
        Post post = new Post();
        post.setUserId(u.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setConfigId(configId);

        // 将图片保存到本地，并收集 URL
        List<String> imageUrls = new ArrayList<>();
        String uploadDirName = "uploads";
        java.nio.file.Path uploadDirPath = java.nio.file.Paths.get(System.getProperty("user.dir"), uploadDirName);
        // 若目录不存在则创建
        java.nio.file.Files.createDirectories(uploadDirPath);

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String original = file.getOriginalFilename();
                String ext = "";
                int dot = original.lastIndexOf('.');
                if (dot >= 0) {
                    ext = original.substring(dot);
                }
                String filename = java.util.UUID.randomUUID().toString().replaceAll("-", "") + ext;
                java.nio.file.Path destPath = uploadDirPath.resolve(filename);
                // 保存文件
                file.transferTo(destPath.toFile());
                // 供前端访问的 URL，应以 /uploads 开头
                imageUrls.add("/uploads/" + filename);
            }
        }

        // 调用服务创建
        postService.create(post, imageUrls);
        return "redirect:/community";
    }

    /**
     * 查看帖子详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long postId,
                         Model model,
                         Authentication auth) {
        // 加载帖子
        Post post = postService.getById(postId);
        model.addAttribute("post", post);

        // 当前用户ID
        Long currentUserId = null;
        if (auth != null) {
            User u = userService.getByUsername(auth.getName());
            currentUserId = (u != null ? u.getId() : null);
        }

        // 帖子点赞情况
        boolean postLiked = (currentUserId != null && postService.liked(postId, currentUserId));
        int postLikeCount = postService.countLikes(postId);
        post.setLiked(postLiked);
        post.setLikeCount(postLikeCount);
        model.addAttribute("liked", postLiked);
        model.addAttribute("likeCount", postLikeCount);

        // 评论列表及每条评论的点赞状态/数量
        List<Comment> comments = commentService.listForPost(postId);
        for (Comment c : comments) {
            int cnt = commentLikeService.countLikes(c.getId());
            boolean liked = (currentUserId != null && commentLikeService.liked(c.getId(), currentUserId));
            c.setLikeCount(cnt);
            c.setLiked(liked);
        }
        model.addAttribute("comments", comments);
        model.addAttribute("currentUserId", currentUserId);

        return "community/detail";
    }

    /**
     * 删除帖子（只有作者本人或管理员可删）
     */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long postId,
                         Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }
        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return "redirect:/login";
        }
        boolean isAdmin = u.getRoles() != null && u.getRoles().contains("ADMIN");
        postService.delete(postId, u.getId(), isAdmin);
        return "redirect:/community";
    }

    /**
     * 帖子点赞/取消赞，AJAX 接口
     */
    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<String> toggleLike(@PathVariable("id") Long postId,
                             Authentication auth) {
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }
        
        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }

        boolean existed = postService.liked(postId, u.getId());
        if (existed) {
            postService.unlike(postId, u.getId());
            return ResponseEntity.ok("unliked");
        } else {
            postService.like(postId, u.getId());
            return ResponseEntity.ok("liked");
        }
    }
}
