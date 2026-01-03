package cn.gcc.gearcorelab.config;

import cn.gcc.gearcorelab.interceptor.BanCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BanCheckInterceptor banCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户封禁检查拦截器
        registry.addInterceptor(banCheckInterceptor)
                // 拦截所有请求路径
                .addPathPatterns("/**")
                // 排除不需要封禁检查的路径
                .excludePathPatterns(
                        // 用户认证相关页面（允许被封禁用户访问以便查看封禁信息）
                        "/login",          // 登录页面
                        "/register",       // 注册页面
                        "/verify-email",   // 邮箱验证页面
                        "/forgot-password", // 忘记密码页面
                        "/reset-password",  // 重置密码页面
                        
                        // 静态资源（确保页面样式和脚本正常加载）
                        "/css/**",         // CSS样式文件
                        "/js/**",          // JavaScript脚本文件
                        "/img/**",         // 图片资源
                        "/uploads/**",     // 用户上传文件
                        
                        // 系统页面
                        "/error"           // 错误页面
                );
    }
}