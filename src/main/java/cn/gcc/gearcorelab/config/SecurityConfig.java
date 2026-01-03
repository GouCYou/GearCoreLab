package cn.gcc.gearcorelab.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import cn.gcc.gearcorelab.service.UserService;
import cn.gcc.gearcorelab.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;    // UserServiceImpl

    @Autowired
    private PasswordEncoder passwordEncoder;          // 来自 AppConfig
    
    @Autowired
    private UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return auth.build();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz
                        .antMatchers(
                                "/register",
                                "/verify-email",
                                "/login",
                                "/forgot-password",
                                "/reset-password",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/uploads/**",
                                "/",
                                "/community",
                                "/community/**"
                        ).permitAll()
                        .antMatchers("/notifications/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(10)
                        .sessionRegistry(sessionRegistry())
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, 
                                                       HttpServletResponse response, 
                                                       Authentication authentication) 
                                                       throws IOException, ServletException {
                        // 获取用户信息并存储到session
                        String username = authentication.getName();
                        User user = userService.getByUsername(username);
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                        
                        // 重定向到首页
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new org.springframework.security.web.authentication.AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, 
                                                       HttpServletResponse response, 
                                                       org.springframework.security.core.AuthenticationException exception) 
                                                       throws IOException, ServletException {
                        String errorMessage = "登录失败";
                        if (exception instanceof org.springframework.security.authentication.DisabledException) {
                            // 检查是否是因为封禁导致的禁用
                            String username = request.getParameter("username");
                            if (username != null) {
                                try {
                                    User user = userService.getByUsername(username);
                                    if (user != null && user.isBanned()) {
                                        String banMessage = "账户已被封禁";
                                        if (user.getBanReason() != null && !user.getBanReason().trim().isEmpty()) {
                                            banMessage += "，原因：" + user.getBanReason();
                                        }
                                        if ("TEMPORARY".equals(user.getBanType()) && user.getBanUntil() != null) {
                                            banMessage += "，解封时间：" + user.getBanUntil().toString().replace("T", " ");
                                        } else if ("PERMANENT".equals(user.getBanType())) {
                                            banMessage += "，永久封禁";
                                        }
                                        errorMessage = banMessage;
                                    } else {
                                        errorMessage = "账户未激活，请先验证邮箱";
                                    }
                                } catch (Exception e) {
                                    errorMessage = "账户未激活，请先验证邮箱";
                                }
                            } else {
                                errorMessage = "账户未激活，请先验证邮箱";
                            }
                        } else if (exception instanceof org.springframework.security.authentication.BadCredentialsException) {
                            errorMessage = "用户名或密码错误";
                        }
                        
                        // 将错误信息存储到session中
                        HttpSession session = request.getSession();
                        session.setAttribute("loginError", errorMessage);
                        
                        // 重定向回登录页面
                        response.sendRedirect("/login?error");
                    }
                })
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
    
    /**
     * 添加SpringSecurityDialect，使Thymeleaf能够使用Spring Security标签
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
    
    /**
     * 配置SessionRegistry Bean
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
