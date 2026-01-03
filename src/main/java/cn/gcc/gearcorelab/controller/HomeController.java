package cn.gcc.gearcorelab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页路由控制器
 * 负责处理应用程序根路径的访问请求
 * 
 * 主要功能：
 * - 将根路径("/")重定向到社区首页
 * - 为访客提供直接进入社区的便捷入口
 * - 避免根路径返回空白页面，提升用户体验
 */
@Controller  // Spring MVC控制器注解，标识这是一个Web控制器类
public class HomeController {

    /**
     * 处理根路径访问请求
     * 当用户访问应用程序根目录时，自动重定向到社区首页
     * 
     * @return 重定向指令，将用户导向/community路径
     */
    @GetMapping("/")  // 映射HTTP GET请求到根路径
    public String root() {
        // 使用Spring MVC的重定向机制，将用户请求转发到社区首页
        // "redirect:"前缀告诉Spring执行HTTP重定向而不是服务器内部转发
        return "redirect:/community";
    }
}