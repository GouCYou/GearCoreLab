package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.News;
import cn.gcc.gearcorelab.model.Notification;
import cn.gcc.gearcorelab.service.UserService;
import cn.gcc.gearcorelab.service.PostService;
import cn.gcc.gearcorelab.service.CommentService;
import cn.gcc.gearcorelab.service.NewsService;
import cn.gcc.gearcorelab.service.NotificationService;
import cn.gcc.gearcorelab.service.HardwareComponentService;
import cn.gcc.gearcorelab.model.Post;
import cn.gcc.gearcorelab.model.HardwareComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private HardwareComponentService hardwareComponentService;

    /**
     * 管理后台首页
     */
    @GetMapping({"", "/"})
    public String adminIndex(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "admin/index";
    }

    /**
     * 获取统计数据API
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 获取各种统计数据
            long userCount = userService.getTotalUserCount();
            long postCount = postService.getTotalPostCount();
            long commentCount = commentService.getTotalCommentCount();
            long newsCount = newsService.getTotalNewsCount();
            
            stats.put("userCount", userCount);
            stats.put("postCount", postCount);
            stats.put("commentCount", commentCount);
            stats.put("newsCount", newsCount);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            stats.put("error", "获取统计数据失败");
            return ResponseEntity.ok(stats);
        }
    }

    /**
     * 用户管理页面
     */
    @GetMapping("/users")
    public String userManagement(Model model, HttpSession session,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int size) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        if (page < 1) page = 1;
        // 获取用户列表
        List<User> users = userService.getAllUsersWithPagination(page, size);
        long totalUsers = userService.getTotalUserCount();
        
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (totalUsers + size - 1) / size);
        model.addAttribute("totalUsers", totalUsers);
        
        return "admin/users";
    }

    /**
     * 封禁用户
     */
    @PostMapping("/users/{userId}/ban")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> banUser(@PathVariable Long userId,
                                                       @RequestParam String banType,
                                                       @RequestParam(required = false) String banReason,
                                                       @RequestParam(required = false) Long banDuration,
                                                       HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("收到封禁请求 - userId: " + userId + ", banType: " + banType + ", banReason: " + banReason + ", banDuration: " + banDuration);
        
        try {
            userService.banUser(userId, banType, banReason, banDuration);
            
            // 如果封禁的是当前登录用户，清除其session
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null && currentUser.getId().equals(userId)) {
                session.invalidate();
            }
            
            response.put("success", true);
            response.put("message", "用户封禁成功");
            System.out.println("封禁请求处理成功");
        } catch (Exception e) {
            System.out.println("封禁请求处理失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "封禁失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 解封用户
     */
    @PostMapping("/users/{userId}/unban")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unbanUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.unbanUser(userId);
            response.put("success", true);
            response.put("message", "用户解封成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "解封失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 帖子管理页面
     */
    @GetMapping("/posts")
    public String postManagement(Model model, HttpSession session,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        // 获取帖子列表
        // 这里需要在PostService中添加相应的方法
        model.addAttribute("currentPage", page);
        
        return "admin/posts";
    }

    /**
     * 获取帖子列表API
     */
    @GetMapping("/api/posts")
    @ResponseBody
    public ResponseEntity<List<Post>> getPosts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(required = false) String status) {
        try {
            List<Post> posts = postService.getAllPostsForAdmin(page, size, search, status);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    /**
     * 删除帖子API
     */
    @DeleteMapping("/api/posts/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            postService.deletePost(id);
            response.put("success", true);
            response.put("message", "帖子删除成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 新闻管理页面
     */
    @GetMapping("/news")
    public String newsManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        return "admin/news";
    }
    
    /**
     * 获取新闻列表API（支持分页）
     */
    @GetMapping("/api/news")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNewsList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                response.put("success", false);
                response.put("message", "权限不足");
                return ResponseEntity.status(403).body(response);
            }
            
            // 使用统一的分页方法
            List<News> newsList = newsService.listAllWithPagination(page, size);
            long totalNews = newsService.getTotalNewsCount();
            int totalPages = (int) Math.ceil((double) totalNews / size);
            
            response.put("success", true);
            response.put("data", newsList);
            response.put("currentPage", page);
            response.put("totalPages", totalPages);
            response.put("totalNews", totalNews);
            response.put("pageSize", size);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取新闻列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 删除新闻API
     */
    @DeleteMapping("/api/news/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteNews(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                response.put("success", false);
                response.put("message", "权限不足");
                return ResponseEntity.status(403).body(response);
            }
            News news = newsService.findById(id);
            if (news == null) {
                response.put("success", false);
                response.put("message", "新闻不存在");
                return ResponseEntity.status(404).body(response);
            }
            newsService.deleteById(id);
            response.put("success", true);
            response.put("message", "新闻删除成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除新闻失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 新闻创建页面
     */
    @GetMapping("/news/create")
    public String newsCreateForm(Model model) {
        model.addAttribute("news", new News());
        return "admin/news/create";
    }
    
    /**
     * 管理员新闻编辑页面
     */
    @GetMapping("/news/edit/{id}")
    public String newsEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        News news = newsService.findById(id);
        if (news == null) {
            return "redirect:/admin/news";
        }
        
        model.addAttribute("news", news);
        return "admin/news/edit";
    }
    
    /**
     * 管理员新闻编辑处理
     */
    @PostMapping("/news/edit/{id}")
    public String updateNews(@PathVariable Long id,
                           @ModelAttribute News news,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam(required = false) String publishTimeStr,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                return "redirect:/login";
            }
            
            news.setId(id);
            
            // 处理图片上传
            if (!image.isEmpty()) {
                String imageUrl = saveImage(image);
                news.setImageUrl(imageUrl);
            }
            
            // 处理发布时间
            if (publishTimeStr != null && !publishTimeStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                news.setPublishTime(LocalDateTime.parse(publishTimeStr, formatter));
            }
            
            newsService.update(news);
            redirectAttributes.addFlashAttribute("success", "新闻更新成功！");
            return "redirect:/admin/news";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新闻更新失败：" + e.getMessage());
            return "redirect:/admin/news/edit/" + id;
        }
    }

    /**
     * 创建新闻
     */
    @PostMapping("/news/create")
    public String createNews(@ModelAttribute News news, 
                        @RequestParam("image") MultipartFile image,
                        @RequestParam(required = false) String publishTimeStr,
                        RedirectAttributes redirectAttributes) {
        try {
            // 处理图片上传
            if (!image.isEmpty()) {
                String imageUrl = saveImage(image);
                news.setImageUrl(imageUrl);
            }
            
            // 处理发布时间
            if (publishTimeStr != null && !publishTimeStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                news.setPublishTime(LocalDateTime.parse(publishTimeStr, formatter));
            }
            
            newsService.create(news);
            redirectAttributes.addFlashAttribute("success", "新闻创建成功！");
            return "redirect:/admin/news";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新闻创建失败：" + e.getMessage());
            return "redirect:/admin/news/create";
        }
    }

    /**
     * 保存图片文件
     */
    private String saveImage(MultipartFile image) throws IOException {
        // 使用绝对路径，指向项目根目录下的uploads文件夹
        String projectRoot = System.getProperty("user.dir");
        String uploadDir = projectRoot + File.separator + "uploads" + File.separator + "news" + File.separator;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        File file = new File(uploadDir + filename);
        image.transferTo(file);
        
        return "/uploads/news/" + filename;
    }

    /**
     * 硬件配件管理页面
     */
    @GetMapping("/hardware")
    public String hardwareManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        return "admin/hardware";
    }

    /**
     * 获取硬件配件列表API
     */
    @GetMapping("/api/hardware")
    @ResponseBody
    public ResponseEntity<List<HardwareComponent>> getHardwareComponents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type) {
        try {
            List<HardwareComponent> components = hardwareComponentService.getAllComponentsForAdmin(page, size, search, type);
            return ResponseEntity.ok(components);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    /**
     * 删除硬件配件API
     */
    @DeleteMapping("/api/hardware/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteHardwareComponent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            hardwareComponentService.deleteComponent(id);
            response.put("success", true);
            response.put("message", "配件删除成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 通知管理页面
     */
    @GetMapping("/notifications")
    public String notificationManagement(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            return "redirect:/login";
        }
        
        return "admin/notifications";
    }

    /**
     * 获取通知列表API
     */
    @GetMapping("/api/notifications")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNotifications(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                response.put("success", false);
                response.put("message", "权限不足");
                return ResponseEntity.status(403).body(response);
            }
            
            // 获取所有通知
            List<Notification> notifications = notificationService.getAllNotifications();
            response.put("success", true);
            response.put("data", notifications);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取通知失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 发送系统通知
     */
    @PostMapping("/api/notifications")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody Map<String, String> notificationData,
                                                               HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                response.put("success", false);
                response.put("message", "权限不足");
                return ResponseEntity.status(403).body(response);
            }
            
            String title = notificationData.get("title");
            String content = notificationData.get("content");
            String type = notificationData.get("type");
            
            System.out.println("发送通知 - 标题: " + title + ", 内容: " + content + ", 类型: " + type);
            
            if ("ADMIN".equals(type)) {
                notificationService.createAdminNotification(title, content, user.getUsername());
                System.out.println("管理员通知发送完成");
            } else {
                // 系统通知发送给所有用户，这里使用空字符串表示全体用户
                notificationService.createSystemNotification(title, content, "");
                System.out.println("系统通知发送完成");
            }
            
            response.put("success", true);
            response.put("message", "通知发送成功");
        } catch (Exception e) {
            System.err.println("发送通知失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "发送失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 删除通知API
     */
    @DeleteMapping("/api/notifications/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRoles().contains("ADMIN")) {
                response.put("success", false);
                response.put("message", "权限不足");
                return ResponseEntity.status(403).body(response);
            }
            
            notificationService.deleteNotificationById(id);
            response.put("success", true);
            response.put("message", "通知删除成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 搜索新闻API
     */
    @GetMapping("/api/news/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchNews(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<News> newsList = newsService.searchNews(query);
            response.put("success", true);
            response.put("data", newsList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }


}