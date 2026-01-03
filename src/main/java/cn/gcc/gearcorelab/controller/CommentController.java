package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.Comment;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.CommentLikeService;
import cn.gcc.gearcorelab.service.CommentService;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 评论相关接口
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService likeService;
    private final UserService userService;

    public CommentController(CommentService commentService,
                             CommentLikeService likeService,
                             UserService userService) {
        this.commentService = commentService;
        this.likeService = likeService;
        this.userService = userService;
    }

    /**
     * 发表评论
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> add(@RequestParam Long postId,
                      @RequestParam String content,
                      @RequestParam(value = "image", required = false) MultipartFile image,
                      Authentication auth) throws IOException {
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }
        
        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }
        
        Comment c = new Comment();
        c.setPostId(postId);
        c.setUserId(u.getId());
        c.setContent(content);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            java.nio.file.Path uploadDir = java.nio.file.Paths.get(System.getProperty("user.dir"), "uploads");
            java.nio.file.Files.createDirectories(uploadDir);
            String original = image.getOriginalFilename();
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) {
                ext = original.substring(dot);
            }
            String filename = java.util.UUID.randomUUID().toString().replaceAll("-", "") + ext;
            java.nio.file.Path dest = uploadDir.resolve(filename);
            image.transferTo(dest.toFile());
            imageUrl = "/uploads/" + filename;
        }

        commentService.add(c, imageUrl);
        return ResponseEntity.ok("success");
    }

    /**
     * 删除评论
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Long id,
                         Authentication auth) {
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }
        
        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }
        
        boolean isAdmin = u.getRoles().contains("ADMIN");
        try {
            commentService.delete(id, u.getId(), isAdmin);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    /**
     * 点赞/取消点赞
     */
    @PostMapping("/like/{id}")
    @ResponseBody
    public ResponseEntity<String> toggleLike(@PathVariable Long id, Authentication auth) {
        if (auth == null || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }

        User u = userService.getByUsername(auth.getName());
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login");
        }

        boolean existed = likeService.liked(id, u.getId());
        if (existed) {
            likeService.unlike(id, u.getId());
            return ResponseEntity.ok("unliked");
        } else {
            try {
                likeService.like(id, u.getId());
                return ResponseEntity.ok("liked");
            } catch (Exception e) {
                // 捕获唯一约束异常，返回友好提示
                return ResponseEntity.ok("already-liked");
            }
        }
    }
}