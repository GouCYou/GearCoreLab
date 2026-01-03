package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/build")
public class BuildController {

    @GetMapping
    public String buildPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", "admin".equals(user.getUsername()));
        return "build/index";
    }
    
    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 检查是否为管理员
        if (!"admin".equals(user.getUsername())) {
            return "redirect:/build?error=access_denied";
        }
        
        model.addAttribute("user", user);
        return "build/admin";
    }
    
    @GetMapping("/check-admin")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        
        if (user == null) {
            response.put("isAdmin", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        boolean isAdmin = "admin".equals(user.getUsername());
        response.put("isAdmin", isAdmin);
        response.put("username", user.getUsername());
        
        return ResponseEntity.ok(response);
    }
}