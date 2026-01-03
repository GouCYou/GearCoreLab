package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 * 表示系统中的用户信息，包含基本信息、认证信息、个人资料和管理信息
 * 
 * 数据库映射：对应users表
 * 主要功能：
 * - 用户基本信息管理（用户名、邮箱等）
 * - 用户认证和授权（密码、角色、激活状态）
 * - 个人资料定制（头像、签名、背景）
 * - 用户管理功能（封禁状态、封禁原因等）
 */
@Data  // Lombok注解，自动生成getter、setter、toString、equals和hashCode方法
public class User {
    
    /**
     * 用户唯一标识符
     * 数据库主键，自增长类型
     */
    private Long id;
    
    /**
     * 用户名
     * 用于登录和显示的唯一用户标识
     * 要求：唯一性、非空
     */
    private String username;
    
    /**
     * 用户密码
     * 存储加密后的密码哈希值，不存储明文密码
     * 使用BCrypt等安全哈希算法加密
     */
    private String password;
    
    /**
     * 用户邮箱地址
     * 用于账户验证、密码重置和通知发送
     * 要求：唯一性、有效邮箱格式
     */
    private String email;

    /**
     * 账户激活状态
     * true: 邮箱已验证，账户可正常使用
     * false: 邮箱未验证，账户功能受限
     */
    private boolean enabled;

    /**
     * 邮箱验证码
     * 用于邮箱验证和密码重置的临时验证码
     * 验证完成后应清空此字段
     */
    private String emailVerifyCode;

    /**
     * 用户注册时间
     * 记录用户首次注册的时间戳
     * 用于统计分析和数据管理
     */
    private LocalDateTime createdAt;

    /**
     * 用户角色列表
     * 定义用户的权限级别，支持多角色
     * 常见角色：["USER"] - 普通用户, ["ADMIN"] - 管理员
     * 用于Spring Security权限控制
     */
    private List<String> roles;

    /**
     * 用户头像URL
     * 存储用户自定义头像的文件路径或URL
     * 支持本地存储和云存储
     */
    private String avatarUrl;
    
    /**
     * 个性签名
     * 用户自定义的个人简介或座右铭
     * 显示在用户资料和帖子中
     */
    private String signature;
    
    /**
     * 个人主页背景图URL
     * 用户自定义的个人主页背景图片
     * 用于个性化用户界面
     */
    private String backgroundUrl;
    
    /**
     * 封禁类型
     * TEMPORARY: 临时封禁，有明确的解封时间
     * PERMANENT: 永久封禁，需要管理员手动解封
     * null: 未被封禁
     */
    private String banType;
    
    /**
     * 封禁原因
     * 记录用户被封禁的具体原因
     * 用于管理员审核和用户申诉
     */
    private String banReason;
    
    /**
     * 临时封禁截止时间
     * 仅在banType为TEMPORARY时有效
     * 系统会自动检查此时间来判断是否解封
     */
    private LocalDateTime banUntil;
    
    /**
     * 手动添加的getter和setter方法
     * 虽然使用了@Data注解，但为了兼容性和特殊需求，保留手动定义的方法
     */
    
    /**
     * 获取用户ID
     * @return 用户唯一标识符
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置用户ID
     * @param id 用户唯一标识符
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取用户名
     * @return 用户名字符串
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * @param username 用户名字符串
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取加密后的密码
     * @return 密码哈希值
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置加密后的密码
     * @param password 密码哈希值
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取用户邮箱
     * @return 邮箱地址字符串
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 设置用户邮箱
     * @param email 邮箱地址字符串
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 检查账户是否已激活
     * @return true表示已激活，false表示未激活
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 设置账户激活状态
     * @param enabled true表示激活，false表示未激活
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * 获取邮箱验证码
     * @return 验证码字符串
     */
    public String getEmailVerifyCode() {
        return emailVerifyCode;
    }
    
    /**
     * 设置邮箱验证码
     * @param emailVerifyCode 验证码字符串
     */
    public void setEmailVerifyCode(String emailVerifyCode) {
        this.emailVerifyCode = emailVerifyCode;
    }
    
    /**
     * 获取用户注册时间
     * @return 注册时间戳
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * 设置用户注册时间
     * @param createdAt 注册时间戳
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * 获取用户角色列表
     * @return 角色字符串列表
     */
    public List<String> getRoles() {
        return roles;
    }
    
    /**
     * 设置用户角色列表
     * @param roles 角色字符串列表
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    /**
     * 获取用户头像URL
     * @return 头像文件路径或URL
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    /**
     * 设置用户头像URL
     * @param avatarUrl 头像文件路径或URL
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    /**
     * 获取用户个性签名
     * @return 个性签名字符串
     */
    public String getSignature() {
        return signature;
    }
    
    /**
     * 设置用户个性签名
     * @param signature 个性签名字符串
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    /**
     * 获取用户背景图URL
     * @return 背景图文件路径或URL
     */
    public String getBackgroundUrl() {
        return backgroundUrl;
    }
    
    /**
     * 设置用户背景图URL
     * @param backgroundUrl 背景图文件路径或URL
     */
    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
    
    /**
     * 检查用户是否处于封禁状态
     * 根据封禁类型和封禁时间判断用户当前是否被封禁
     * 
     * 判断逻辑：
     * 1. 如果banType为null，表示未被封禁
     * 2. 如果banType为PERMANENT，表示永久封禁
     * 3. 如果banType为TEMPORARY，需要检查当前时间是否在封禁期内
     * 
     * @return true表示用户当前被封禁，false表示用户可正常使用
     */
    public boolean isBanned() {
        // 检查封禁类型是否为空，为空表示未被封禁
        if (banType == null) {
            return false;
        }
        
        // 永久封禁直接返回true
        if ("PERMANENT".equals(banType)) {
            return true;
        }
        
        // 临时封禁需要检查时间
        if ("TEMPORARY".equals(banType) && banUntil != null) {
            LocalDateTime now = LocalDateTime.now();
            // 如果当前时间在封禁截止时间之前，表示仍在封禁期内
            boolean isBefore = now.isBefore(banUntil);
            // 调试信息：记录时间比较结果（生产环境应移除或使用日志框架）
            System.out.println("时间比较调试 - 当前时间: " + now + ", 封禁截止时间: " + banUntil + ", isBefore: " + isBefore);
            return isBefore;
        }
        
        // 其他情况默认为未封禁
        return false;
    }
    
    /**
     * 获取封禁类型
     * @return 封禁类型字符串（TEMPORARY/PERMANENT/null）
     */
    public String getBanType() {
        return banType;
    }
    
    /**
     * 设置封禁类型
     * @param banType 封禁类型字符串（TEMPORARY/PERMANENT/null）
     */
    public void setBanType(String banType) {
        this.banType = banType;
    }
    
    /**
     * 获取封禁原因
     * @return 封禁原因描述字符串
     */
    public String getBanReason() {
        return banReason;
    }
    
    /**
     * 设置封禁原因
     * @param banReason 封禁原因描述字符串
     */
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }
    
    /**
     * 获取临时封禁截止时间
     * @return 封禁截止时间戳，仅在临时封禁时有效
     */
    public LocalDateTime getBanUntil() {
        return banUntil;
    }
    
    /**
     * 设置临时封禁截止时间
     * @param banUntil 封禁截止时间戳，仅在临时封禁时有效
     */
    public void setBanUntil(LocalDateTime banUntil) {
        this.banUntil = banUntil;
    }
}
