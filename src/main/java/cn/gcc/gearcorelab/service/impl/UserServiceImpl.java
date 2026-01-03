package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.UserMapper;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.context.annotation.Lazy;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final SessionRegistry sessionRegistry;
    private final Random random = new Random();

    public UserServiceImpl(UserMapper userMapper, JavaMailSender mailSender, PasswordEncoder passwordEncoder, @Lazy SessionRegistry sessionRegistry) {
        this.userMapper = userMapper;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userMapper.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        
        // 检查用户是否被封禁
        System.out.println("用户登录检查 - username: " + username + ", banType: " + u.getBanType() + ", banReason: " + u.getBanReason() + ", banUntil: " + u.getBanUntil() + ", isBanned: " + u.isBanned());
        
        // 不在这里抛出异常，而是通过UserDetails的enabled字段来控制
        // 如果用户被封禁，enabled将为false，Spring Security会自动抛出DisabledException
        
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.isEnabled() && !u.isBanned(),  // 同时检查邮箱验证和封禁状态
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public User getById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 查询并填充角色
            user.setRoles(userMapper.selectRolesByUserId(user.getId()));
        }
        return user;
    }

    @Override
    public void register(User user) {
        // 1. 检查用户名是否已存在
        User existing = userMapper.findByUsername(user.getUsername());
        if (existing != null) {
            if (existing.isEnabled()) {
                throw new RuntimeException("用户名已存在");
            }
            // 账户已创建但未激活，更新验证码与密码等信息即可
            String newCode = generateCode();
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
            existing.setEmailVerifyCode(newCode);
            existing.setCreatedAt(LocalDateTime.now());
            userMapper.update(existing);
            
            // 检查是否已有角色，如果没有则分配默认'USER'角色
            if (existing.getRoles() == null || existing.getRoles().isEmpty()) {
                userMapper.insertUserRole(existing.getId(), "USER");
            }
            
            sendVerificationCode(existing.getEmail());
            return;
        }

        // 2. 检查邮箱是否已被使用
        User mailOwner = userMapper.findByEmail(user.getEmail());
        if (mailOwner != null) {
            if (mailOwner.isEnabled()) {
                throw new RuntimeException("邮箱已被使用");
            }
            // 邮箱对应账户未激活，更新验证码即可
            String newCode = generateCode();
            mailOwner.setUsername(user.getUsername());
            mailOwner.setPassword(passwordEncoder.encode(user.getPassword()));
            mailOwner.setEmailVerifyCode(newCode);
            mailOwner.setCreatedAt(LocalDateTime.now());
            userMapper.update(mailOwner);
            
            // 检查是否已有角色，如果没有则分配默认'USER'角色
            if (mailOwner.getRoles() == null || mailOwner.getRoles().isEmpty()) {
                userMapper.insertUserRole(mailOwner.getId(), "USER");
            }
            
            sendVerificationCode(mailOwner.getEmail());
            return;
        }

        // 3. 全新注册
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String code = generateCode();
        user.setEmailVerifyCode(code);
        userMapper.insert(user);
        
        // 为新用户分配默认的'USER'角色
        userMapper.insertUserRole(user.getId(), "USER");
        
        sendVerificationCode(user.getEmail());
    }

    @Override
    public void sendVerificationCode(String email) {
        User u = userMapper.findByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("邮箱未注册：" + email);
        }
        String code = generateCode();
        u.setEmailVerifyCode(code);
        userMapper.update(u);

        sendHtmlEmail(email, "【GearCoreLab】邮箱验证码", createVerificationEmailHtml(code));
    }

    @Override
    public boolean verifyEmail(String username, String code) {
        User u = userMapper.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        if (!code.equals(u.getEmailVerifyCode())) {
            return false;
        }
        u.setEnabled(true);
        userMapper.update(u);
        return true;
    }

    @Override
    public void save(User user) {
        userMapper.update(user);
    }
    
    @Override
    public java.util.List<User> searchUsers(String query) {
        return userMapper.searchUsers(query);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void unbanUser(Long userId) {
        System.out.println("解封用户 - userId: " + userId);
        userMapper.unbanUser(userId);
        System.out.println("解封用户操作完成");
    }

    /**
     * 清除指定用户的所有活跃session
     */
    private void invalidateUserSessions(String username) {
        try {
            List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
            for (Object principal : allPrincipals) {
                if (principal instanceof org.springframework.security.core.userdetails.User) {
                    org.springframework.security.core.userdetails.User userDetails = 
                        (org.springframework.security.core.userdetails.User) principal;
                    if (username.equals(userDetails.getUsername())) {
                        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                        for (SessionInformation session : sessions) {
                            session.expireNow();
                        }
                        System.out.println("已清除用户 " + username + " 的 " + sessions.size() + " 个活跃session");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("清除用户session时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void banUser(Long userId, String banType, String banReason, Long banDuration) {
        System.out.println("UserServiceImpl.banUser 被调用 - userId: " + userId + ", banType: " + banType + ", banReason: " + banReason + ", banDuration: " + banDuration);
        userMapper.banUser(userId, banType, banReason, banDuration);
        
        // 清除被封禁用户的所有活跃session
        User bannedUser = userMapper.findById(userId);
        if (bannedUser != null) {
            invalidateUserSessions(bannedUser.getUsername());
        }
        
        System.out.println("UserServiceImpl.banUser 执行完成");
    }

    @Override
    public java.util.List<User> getAllUsersWithPagination(int page, int size) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;
        return userMapper.getAllUsersWithPagination(offset, size);
    }

    @Override
    public long getTotalUserCount() {
        return userMapper.countAllUsers();
    }

    private String generateCode() {
        int v = random.nextInt(9000) + 1000;
        return String.valueOf(v);
    }
    
    @Override
    public void sendPasswordResetCode(String email) {
        User u = userMapper.findByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("邮箱未注册：" + email);
        }
        String code = generateCode();
        u.setEmailVerifyCode(code);
        userMapper.update(u);
        
        sendHtmlEmail(email, "【GearCoreLab】密码重置验证码", createPasswordResetEmailHtml(code));
    }
    
    @Override
    public boolean resetPassword(String email, String code, String newPassword) {
        User u = userMapper.findByEmail(email);
        if (u == null) {
            return false;
        }
        if (!code.equals(u.getEmailVerifyCode())) {
            return false;
        }
        u.setPassword(passwordEncoder.encode(newPassword)); // 加密新密码
        u.setEmailVerifyCode(null); // 清除验证码
        userMapper.update(u);
        return true;
    }
    
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("cctnetwork@163.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
        }
    }
    
    private String createVerificationEmailHtml(String code) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>邮箱验证码</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #74b9ff; min-height: 100vh; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 15px; box-shadow: 0 8px 32px rgba(0,0,0,0.15); overflow: hidden; }\n" +
                "        .header { background: #007bff; color: white; padding: 40px 30px; text-align: center; position: relative; }\n" +
                "        .logo-container { display: flex; align-items: center; justify-content: center; margin-bottom: 15px; }\n" +
                "        .logo-text { font-size: 28px; font-weight: bold; }\n" +
                "        .header-title { font-size: 18px; opacity: 0.9; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .code-box { background: #f8f9ff; border: 2px solid #007bff; border-radius: 12px; padding: 25px; text-align: center; margin: 25px 0; box-shadow: 0 4px 15px rgba(0, 123, 255, 0.1); }\n" +
                "        .code { font-size: 36px; font-weight: bold; color: #007bff; letter-spacing: 8px; text-shadow: 0 2px 4px rgba(0, 123, 255, 0.2); }\n" +
                "        .warning { background: #fff8e1; border-left: 4px solid #ff9800; padding: 18px; margin: 25px 0; border-radius: 8px; box-shadow: 0 2px 8px rgba(255, 152, 0, 0.1); }\n" +
                "        .footer { background: #f8f9fa; padding: 25px; text-align: center; color: #6c757d; font-size: 14px; }\n" +
                "        .highlight { color: #667eea; font-weight: 600; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <div class='logo-container'>\n" +
                "                <div class='logo-text'>GearCoreLab</div>\n" +
                "            </div>\n" +
                "            <div class='header-title'>邮箱验证</div>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <h2 style='color: #2d3748; margin-bottom: 20px;'>您好！</h2>\n" +
                "            <p style='color: #4a5568; line-height: 1.6; margin-bottom: 25px;'>感谢您注册 <span class='highlight'>GearCoreLab</span>！请使用以下验证码完成邮箱验证：</p>\n" +
                "            <div class='code-box'>\n" +
                "                <div style='color: #667eea; font-size: 14px; margin-bottom: 10px; font-weight: 600;'>您的验证码</div>\n" +
                "                <div class='code'>" + code + "</div>\n" +
                "                <div style='color: #718096; font-size: 12px; margin-top: 10px;'>请在10分钟内使用</div>\n" +
                "            </div>\n" +
                "            <p style='color: #4a5568; line-height: 1.6;'>验证码有效期为 <span class='highlight'>10分钟</span>，请尽快完成验证。</p>\n" +
                "            <div class='warning'>\n" +
                "                <strong style='color: #e65100;'>🔒 安全提醒：</strong>如果这不是您本人的操作，请忽略此邮件。请勿将验证码告诉他人。\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p style='margin: 0 0 10px 0;'>此邮件由系统自动发送，请勿回复。</p>\n" +
                "            <p style='margin: 0;'>© 2024 <span class='highlight'>GearCoreLab</span>. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
    
    private String createPasswordResetEmailHtml(String code) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>密码重置验证码</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden; }\n" +
                "        .header { background: #007bff; color: white; padding: 30px; text-align: center; }\n" +
                "        .logo { font-size: 28px; font-weight: bold; margin-bottom: 10px; }\n" +
                "        .content { padding: 40px 30px; }\n" +
                "        .code-box { background-color: #f8f9fa; border: 2px dashed #007bff; border-radius: 8px; padding: 20px; text-align: center; margin: 20px 0; }\n" +
                "        .code { font-size: 32px; font-weight: bold; color: #007bff; letter-spacing: 5px; }\n" +
                "        .warning { background-color: #f8d7da; border-left: 4px solid #dc3545; padding: 15px; margin: 20px 0; border-radius: 4px; }\n" +
                "        .footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #6c757d; font-size: 14px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <div class='logo'>GearCoreLab</div>\n" +
                "            <div>密码重置验证码</div>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <h2>密码重置请求</h2>\n" +
                "            <p>我们收到了您的密码重置请求。请使用以下验证码重置您的密码：</p>\n" +
                "            <div class='code-box'>\n" +
                "                <div class='code'>" + code + "</div>\n" +
                "            </div>\n" +
                "            <p>验证码有效期为10分钟，请尽快完成密码重置。</p>\n" +
                "            <div class='warning'>\n" +
                "                <strong>重要提醒：</strong>如果您没有申请密码重置，请立即忽略此邮件并检查您的账户安全。请勿将验证码告诉任何人。\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>此邮件由系统自动发送，请勿回复。</p>\n" +
                "            <p>© 2024 GearCoreLab. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
