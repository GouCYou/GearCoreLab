package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.UserFollowService;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/follow")
public class UserFollowController {
    
    private final UserFollowService userFollowService;
    private final UserService userService;
    
    public UserFollowController(UserFollowService userFollowService, UserService userService) {
        this.userFollowService = userFollowService;
        this.userService = userService;
    }
    
    /**
     * 关注用户
     */
    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> followUser(
            @RequestParam Long userId,
            Authentication auth) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        User currentUser = userService.getByUsername(auth.getName());
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.ok(response);
        }
        
        boolean success = userFollowService.followUser(currentUser.getId(), userId);
        if (success) {
            response.put("success", true);
            response.put("message", "关注成功");
            response.put("followerCount", userFollowService.getFollowerCount(userId));
        } else {
            response.put("success", false);
            response.put("message", "关注失败");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 取消关注
     */
    @PostMapping("/unfollow")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unfollowUser(
            @RequestParam Long userId,
            Authentication auth) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        User currentUser = userService.getByUsername(auth.getName());
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return ResponseEntity.ok(response);
        }
        
        boolean success = userFollowService.unfollowUser(currentUser.getId(), userId);
        if (success) {
            response.put("success", true);
            response.put("message", "取消关注成功");
            response.put("followerCount", userFollowService.getFollowerCount(userId));
        } else {
            response.put("success", false);
            response.put("message", "取消关注失败");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 检查关注状态
     */
    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFollowStatus(
            @RequestParam Long userId,
            Authentication auth) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("isFollowing", false);
            return ResponseEntity.ok(response);
        }
        
        User currentUser = userService.getByUsername(auth.getName());
        if (currentUser == null) {
            response.put("isFollowing", false);
            return ResponseEntity.ok(response);
        }
        
        boolean isFollowing = userFollowService.isFollowing(currentUser.getId(), userId);
        response.put("isFollowing", isFollowing);
        response.put("followerCount", userFollowService.getFollowerCount(userId));
        response.put("followingCount", userFollowService.getFollowingCount(userId));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取关注数
     */
    @GetMapping("/following/count/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFollowingCount(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = userFollowService.getFollowingCount(userId);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取粉丝数
     */
    @GetMapping("/followers/count/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFollowersCount(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        int count = userFollowService.getFollowerCount(userId);
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取关注列表页面
     */
    @GetMapping("/following/{username}")
    public String getFollowingList(@PathVariable String username, Model model) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return "redirect:/";
        }
        
        List<User> followings = userFollowService.getFollowings(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("followings", followings);
        model.addAttribute("type", "following");
        
        return "user/follow-list";
    }
    
    /**
     * 获取粉丝列表页面
     */
    @GetMapping("/followers/{username}")
    public String getFollowersList(@PathVariable String username, Model model) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return "redirect:/";
        }
        
        List<User> followers = userFollowService.getFollowers(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("followers", followers);
        model.addAttribute("type", "followers");
        
        return "user/follow-list";
    }
}