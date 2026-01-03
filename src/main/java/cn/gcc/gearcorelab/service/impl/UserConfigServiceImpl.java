package cn.gcc.gearcorelab.service.impl;

import cn.gcc.gearcorelab.mapper.UserConfigMapper;
import cn.gcc.gearcorelab.model.UserConfig;
import cn.gcc.gearcorelab.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserConfigServiceImpl implements UserConfigService {
    
    @Autowired
    private UserConfigMapper userConfigMapper;
    
    @Override
    public List<UserConfig> getUserConfigs(Long userId) {
        return userConfigMapper.findByUserId(userId);
    }
    
    @Override
    public UserConfig getUserConfig(Long id, Long userId) {
        return userConfigMapper.findByIdAndUserId(id, userId);
    }
    
    @Override
    public UserConfig saveUserConfig(UserConfig userConfig) {
        userConfig.setCreatedAt(LocalDateTime.now());
        userConfig.setUpdatedAt(LocalDateTime.now());
        userConfigMapper.insert(userConfig);
        return userConfig;
    }
    
    @Override
    public UserConfig updateUserConfig(UserConfig userConfig) {
        userConfig.setUpdatedAt(LocalDateTime.now());
        userConfigMapper.update(userConfig);
        return userConfig;
    }
    
    @Override
    public boolean deleteUserConfig(Long id, Long userId) {
        return userConfigMapper.deleteByIdAndUserId(id, userId) > 0;
    }
    
    @Override
    public int getUserConfigCount(Long userId) {
        return userConfigMapper.countByUserId(userId);
    }
    
    @Override
    public UserConfig getPublicUserConfig(Long id) {
        return userConfigMapper.findById(id);
    }
}