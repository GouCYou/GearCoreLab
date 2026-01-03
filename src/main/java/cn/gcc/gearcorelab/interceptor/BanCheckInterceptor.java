package cn.gcc.gearcorelab.interceptor;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户封禁检查拦截器
 * 在用户访问受保护资源前检查用户是否被封禁，确保被封禁用户无法继续使用系统功能
 * 
 * 主要功能：
 * - 拦截所有需要登录的请求
 * - 检查当前登录用户的封禁状态
 * - 自动踢出被封禁的用户
 * - 实时更新用户会话信息
 * 
 * 工作流程：
 * 1. 获取当前用户会话
 * 2. 从数据库获取用户最新状态
 * 3. 检查用户是否被封禁
 * 4. 如果被封禁则清除会话并重定向到登录页
 * 5. 如果正常则更新会话中的用户信息
 * 
 * 安全特性：
 * - 实时检查封禁状态（防止缓存过期问题）
 * - 自动清理被封禁用户的会话
 * - 重定向到带错误信息的登录页面
 * 
 * @author GearCoreLab
 * @version 1.0
 * @since 2024
 */
@Component
public class BanCheckInterceptor implements HandlerInterceptor {

    /**
     * 用户服务，用于获取用户最新信息和封禁状态
     */
    @Autowired
    private UserService userService;

    /**
     * 请求预处理方法
     * 在控制器方法执行前检查用户封禁状态
     * 
     * 处理逻辑：
     * 1. 获取当前HTTP会话
     * 2. 检查会话中是否有用户信息
     * 3. 从数据库获取用户最新状态（确保实时性）
     * 4. 检查用户是否被封禁
     * 5. 被封禁用户：清除会话，重定向到登录页
     * 6. 正常用户：更新会话信息，允许继续访问
     * 
     * 安全考虑：
     * - 使用getSession(false)避免创建新会话
     * - 实时从数据库获取用户状态，防止缓存问题
     * - 被封禁用户会话立即失效
     * - 重定向包含错误参数，便于前端显示封禁信息
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象（控制器方法）
     * @return true表示允许继续处理请求，false表示拦截请求
     * @throws Exception 处理过程中可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前HTTP会话（不创建新会话）
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // 从会话中获取用户信息
            User user = (User) session.getAttribute("user");
            
            if (user != null) {
                // 从数据库重新获取用户信息，确保封禁状态是最新的
                User currentUser = userService.getByUsername(user.getUsername());
                
                // 检查用户是否被封禁
                if (currentUser != null && currentUser.isBanned()) {
                    // 用户已被封禁，立即清除会话
                    session.invalidate();
                    
                    // 重定向到登录页面，并携带封禁错误信息
                    response.sendRedirect("/login?error=banned");
                    
                    // 拦截请求，不允许继续处理
                    return false;
                }
                
                // 用户状态正常，更新会话中的用户信息（保持数据同步）
                session.setAttribute("user", currentUser);
            }
        }
        
        // 允许请求继续处理
        return true;
    }
}