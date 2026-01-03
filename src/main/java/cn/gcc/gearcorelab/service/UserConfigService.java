package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.UserConfig;
import java.util.List;

public interface UserConfigService {
    
    /**
     * 获取用户的所有配置单
     */
    List<UserConfig> getUserConfigs(Long userId);
    
    /**
     * 根据ID获取用户配置单
     */
    UserConfig getUserConfig(Long id, Long userId);
    
    /**
     * 保存用户配置单
     */
    UserConfig saveUserConfig(UserConfig userConfig);
    
    /**
     * 更新用户配置单
     */
    UserConfig updateUserConfig(UserConfig userConfig);
    
    /**
     * 删除用户配置单
     */
    boolean deleteUserConfig(Long id, Long userId);
    
    /**
     * 获取用户配置单数量
     */
    int getUserConfigCount(Long userId);
    
    /**
     * 根据ID获取配置单（公开访问，不验证用户权限）
     */
    UserConfig getPublicUserConfig(Long id);
}