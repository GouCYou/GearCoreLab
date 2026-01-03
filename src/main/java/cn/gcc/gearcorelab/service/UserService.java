package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户业务服务接口
 * 定义用户管理相关的核心业务逻辑，继承Spring Security的UserDetailsService接口
 * 
 * 主要功能模块：
 * - 用户基础信息管理（查询、保存）
 * - 用户注册和邮箱验证流程
 * - 密码重置功能
 * - 用户搜索功能
 * - 管理员用户管理功能（封禁、解封、分页查询）
 * - Spring Security集成（用户认证）
 * 
 * 安全特性：
 * - 所有敏感操作都需要邮箱验证
 * - 支持用户封禁管理
 * - 集成Spring Security认证体系
 * 
 * @author GearCoreLab
 * @version 1.0
 * @since 2024
 */
public interface UserService extends UserDetailsService {
    
    /**
     * 根据用户ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    User getById(Long id);
    
    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    User getByUsername(String username);
    
    /**
     * 用户注册
     * 创建新用户账户并发送邮箱验证码
     * 
     * 注册流程：
     * 1. 验证用户名和邮箱是否已存在
     * 2. 加密用户密码
     * 3. 创建用户账户（初始状态为未激活）
     * 4. 发送邮箱验证码
     * 
     * @param user 用户注册信息
     * @throws IllegalArgumentException 当用户名或邮箱已存在时抛出
     */
    void register(User user);
    
    /**
     * 发送邮箱验证码
     * 向指定邮箱发送验证码用于账户激活
     * 
     * @param email 目标邮箱地址
     */
    void sendVerificationCode(String email);
    
    /**
     * 验证邮箱验证码
     * 验证用户提交的验证码并激活账户
     * 
     * @param username 用户名
     * @param code 验证码
     * @return 验证成功返回true，失败返回false
     */
    boolean verifyEmail(String username, String code);
    
    /**
     * 保存用户信息
     * 更新用户数据到数据库
     * 
     * @param user 要保存的用户对象
     */
    void save(User user);
    
    /**
     * 发送密码重置验证码
     * 向用户邮箱发送密码重置验证码
     * 
     * @param email 用户邮箱地址
     * @throws IllegalArgumentException 当邮箱不存在时抛出
     */
    void sendPasswordResetCode(String email);
    
    /**
     * 重置用户密码
     * 验证重置码并更新用户密码
     * 
     * @param email 用户邮箱
     * @param code 密码重置验证码
     * @param newPassword 新密码
     * @return 重置成功返回true，失败返回false
     */
    boolean resetPassword(String email, String code, String newPassword);
    
    /**
     * 搜索用户
     * 根据关键词搜索用户（支持用户名、邮箱等字段）
     * 
     * @param query 搜索关键词
     * @return 匹配的用户列表
     */
    java.util.List<User> searchUsers(String query);
    
    /**
     * 获取用户总数
     * 统计系统中的用户总数量
     * 
     * @return 用户总数
     */
    long getTotalUserCount();
    
    /**
     * 分页获取所有用户
     * 支持管理员查看和管理用户列表
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 用户列表
     */
    java.util.List<User> getAllUsersWithPagination(int page, int size);
    
    /**
     * 封禁用户
     * 管理员功能，对违规用户进行封禁处理
     * 
     * @param userId 要封禁的用户ID
     * @param banType 封禁类型（如：违规发言、恶意行为等）
     * @param banReason 封禁原因描述
     * @param banDuration 封禁时长（毫秒），null表示永久封禁
     */
    void banUser(Long userId, String banType, String banReason, Long banDuration);
    
    /**
     * 解封用户
     * 管理员功能，解除用户的封禁状态
     * 
     * @param userId 要解封的用户ID
     */
    void unbanUser(Long userId);
}
