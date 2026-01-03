package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.model.UserConfig;
import cn.gcc.gearcorelab.service.UserConfigService;
import cn.gcc.gearcorelab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-configs")
public class UserConfigController {
    
    @Autowired
    private UserConfigService userConfigService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取当前用户的所有配置单
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserConfigs(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User currentUser = userService.getByUsername(authentication.getName());
            List<UserConfig> configs = userConfigService.getUserConfigs(currentUser.getId());
            
            response.put("success", true);
            response.put("data", configs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取配置单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取指定配置单（公开访问）
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserConfig(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserConfig config = userConfigService.getPublicUserConfig(id);
            
            if (config == null) {
                response.put("success", false);
                response.put("message", "配置单不存在");
                return ResponseEntity.notFound().build();
            }
            
            response.put("success", true);
            response.put("data", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取配置单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 保存配置单
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveUserConfig(@RequestBody UserConfig userConfig, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User currentUser = userService.getByUsername(authentication.getName());
            
            // 检查配置单数量限制
            int currentCount = userConfigService.getUserConfigCount(currentUser.getId());
            if (currentCount >= 20) { // 最大20个配置单
                response.put("success", false);
                response.put("message", "最多只能保存20个配置单");
                return ResponseEntity.badRequest().body(response);
            }
            
            userConfig.setUserId(currentUser.getId());
            UserConfig savedConfig = userConfigService.saveUserConfig(userConfig);
            
            response.put("success", true);
            response.put("data", savedConfig);
            response.put("message", "配置单保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "保存配置单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新配置单
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUserConfig(@PathVariable Long id, @RequestBody UserConfig userConfig, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User currentUser = userService.getByUsername(authentication.getName());
            
            // 验证配置单所有权
            UserConfig existingConfig = userConfigService.getUserConfig(id, currentUser.getId());
            if (existingConfig == null) {
                response.put("success", false);
                response.put("message", "配置单不存在或无权访问");
                return ResponseEntity.notFound().build();
            }
            
            userConfig.setId(id);
            userConfig.setUserId(currentUser.getId());
            UserConfig updatedConfig = userConfigService.updateUserConfig(userConfig);
            
            response.put("success", true);
            response.put("data", updatedConfig);
            response.put("message", "配置单更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新配置单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除配置单
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserConfig(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            User currentUser = userService.getByUsername(authentication.getName());
            boolean deleted = userConfigService.deleteUserConfig(id, currentUser.getId());
            
            if (!deleted) {
                response.put("success", false);
                response.put("message", "配置单不存在或无权删除");
                return ResponseEntity.notFound().build();
            }
            
            response.put("success", true);
            response.put("message", "配置单删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除配置单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}