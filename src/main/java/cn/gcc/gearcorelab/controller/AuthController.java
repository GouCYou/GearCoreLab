package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器
 * 负责处理用户注册、登录、邮箱验证、密码重置等认证相关的Web请求
 * 
 * 主要功能：
 * - 用户注册流程（注册 -> 邮箱验证 -> 激活账户）
 * - 用户登录页面展示（实际登录逻辑由Spring Security处理）
 * - 忘记密码流程（发送重置码 -> 验证重置码 -> 重置密码）
 * - 邮箱验证流程（发送验证码 -> 验证激活账户）
 * 
 * 安全特性：
 * - 所有敏感操作都需要邮箱验证
 * - 验证码有时效性限制
 * - 密码重置需要双重验证（邮箱+验证码）
 * 
 * @author GearCoreLab
 * @version 1.0
 * @since 2024
 */
@Controller
public class AuthController {

    /**
     * 用户服务，提供用户认证相关的业务逻辑
     */
    private final UserService userService;

    /**
     * 构造函数，通过依赖注入获取用户服务
     * 
     * @param userService 用户服务实例，用于处理用户认证业务逻辑
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 显示用户注册页面
     * 提供用户注册表单界面
     * 
     * @return 注册页面模板路径
     */
    @GetMapping("/register")
    public String showRegister() {
        return "auth/register";
    }

    /**
     * 处理用户注册请求
     * 接收用户注册信息，创建新用户账户并发送邮箱验证码
     * 
     * 注册流程：
     * 1. 验证用户输入信息（用户名、邮箱、密码等）
     * 2. 检查用户名和邮箱是否已存在
     * 3. 创建新用户账户（初始状态为未激活）
     * 4. 发送邮箱验证码
     * 5. 跳转到邮箱验证页面
     * 
     * @param user 用户注册信息，通过表单绑定获取
     * @param m 模型对象，用于向视图传递数据
     * @return 成功时跳转到验证页面，失败时返回注册页面并显示错误信息
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model m) {
        try {
            // 调用用户服务进行注册
            userService.register(user);
            
            // 注册成功，准备跳转到邮箱验证页面
            m.addAttribute("username", user.getUsername());
            m.addAttribute("email", user.getEmail());
            return "auth/verify";
        } catch (IllegalArgumentException e) {
            // 注册失败，返回注册页面并显示错误信息
            m.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    /**
     * 显示邮箱验证页面
     * 通过GET请求参数获取用户名和邮箱，用于页面渲染
     * 
     * 使用场景：
     * - 用户注册后跳转到此页面
     * - 用户需要重新验证邮箱时访问
     * 
     * @param username 待验证的用户名
     * @param email 待验证的邮箱地址
     * @param m 模型对象，用于向视图传递数据
     * @return 邮箱验证页面模板路径
     */
    @GetMapping("/verify-email")
    public String showVerify(@RequestParam String username,
                             @RequestParam String email,
                             Model m) {
        // 将用户名和邮箱传递给视图，用于页面显示
        m.addAttribute("username", username);
        m.addAttribute("email", email);
        return "auth/verify";
    }

    /**
     * 处理邮箱验证请求
     * 验证用户提交的邮箱验证码，激活用户账户
     * 
     * 验证流程：
     * 1. 接收用户名、邮箱和验证码
     * 2. 调用用户服务验证验证码
     * 3. 验证成功则激活账户并跳转到登录页
     * 4. 验证失败则返回验证页面并显示错误信息
     * 
     * 安全考虑：
     * - 验证码有时效性限制
     * - 验证码只能使用一次
     * - 验证失败不会泄露敏感信息
     * 
     * @param username 用户名
     * @param email 邮箱地址
     * @param code 用户输入的验证码
     * @param m 模型对象，用于向视图传递数据
     * @return 验证成功跳转到登录页，失败返回验证页面
     */
    @PostMapping("/verify-email")
    public String verify(@RequestParam String username,
                         @RequestParam String email,
                         @RequestParam String code,
                         Model m) {
        // 调用用户服务验证邮箱验证码
        if (userService.verifyEmail(username, code)) {
            // 验证成功，跳转到登录页面
            return "redirect:/login";
        }
        
        // 验证失败，返回验证页面并显示错误信息
        m.addAttribute("username", username);
        m.addAttribute("email", email);
        m.addAttribute("error", "验证码错误，请重新输入");
        return "auth/verify";
    }

    /**
     * 显示用户登录页面
     * 提供用户登录表单界面
     * 
     * 注意：实际的登录验证逻辑由Spring Security处理，
     * 此方法仅负责展示登录页面
     * 
     * @return 登录页面模板路径
     */
    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }
    
    /**
     * 显示忘记密码页面
     * 提供用户输入邮箱以重置密码的表单界面
     * 
     * @return 忘记密码页面模板路径
     */
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "auth/forgot-password";
    }
    
    /**
     * 处理忘记密码请求
     * 向用户邮箱发送密码重置验证码
     * 
     * 处理流程：
     * 1. 验证邮箱地址是否存在
     * 2. 生成密码重置验证码
     * 3. 发送验证码到用户邮箱
     * 4. 跳转到密码重置页面
     * 
     * 安全特性：
     * - 验证码有时效性限制
     * - 即使邮箱不存在也不会泄露此信息（统一返回成功消息）
     * 
     * @param email 用户输入的邮箱地址
     * @param m 模型对象，用于向视图传递数据
     * @return 成功时跳转到重置密码页面，失败时返回忘记密码页面
     */
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model m) {
        try {
            // 发送密码重置验证码到用户邮箱
            userService.sendPasswordResetCode(email);
            
            // 发送成功，跳转到密码重置页面
            m.addAttribute("success", "验证码已发送到您的邮箱，请查收");
            m.addAttribute("email", email);
            return "auth/reset-password";
        } catch (Exception e) {
            // 发送失败，返回忘记密码页面并显示错误信息
            m.addAttribute("error", e.getMessage());
            return "auth/forgot-password";
        }
    }
    
    /**
     * 显示密码重置页面
     * 通过GET请求参数获取邮箱地址，用于页面渲染
     * 
     * 使用场景：
     * - 用户在忘记密码页面提交邮箱后跳转到此页面
     * - 用户需要重新输入密码重置信息时访问
     * 
     * @param email 待重置密码的邮箱地址
     * @param m 模型对象，用于向视图传递数据
     * @return 密码重置页面模板路径
     */
    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String email, Model m) {
        // 将邮箱地址传递给视图，用于页面显示和表单回填
        m.addAttribute("email", email);
        return "auth/reset-password";
    }
    
    /**
     * 处理密码重置请求
     * 验证重置码并更新用户密码
     * 
     * 重置流程：
     * 1. 验证两次输入的密码是否一致
     * 2. 验证邮箱和重置验证码
     * 3. 更新用户密码
     * 4. 跳转到登录页面
     * 
     * 安全特性：
     * - 需要邮箱和验证码双重验证
     * - 验证码有时效性限制
     * - 密码确认机制防止输入错误
     * - 重置成功后验证码立即失效
     * 
     * @param email 邮箱地址
     * @param code 密码重置验证码
     * @param newPassword 新密码
     * @param confirmPassword 确认密码
     * @param m 模型对象，用于向视图传递数据
     * @return 重置成功跳转到登录页，失败返回重置页面
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                               @RequestParam String code,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               Model m) {
        // 验证两次输入的密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            m.addAttribute("error", "两次输入的密码不一致");
            m.addAttribute("email", email);
            return "auth/reset-password";
        }
        
        // 调用用户服务进行密码重置
        if (userService.resetPassword(email, code, newPassword)) {
            // 重置成功，跳转到登录页面
            m.addAttribute("success", "密码重置成功，请使用新密码登录");
            return "auth/login";
        } else {
            // 重置失败，返回重置页面并显示错误信息
            m.addAttribute("error", "验证码错误或已过期，请重新获取");
            m.addAttribute("email", email);
            return "auth/reset-password";
        }
    }
}
