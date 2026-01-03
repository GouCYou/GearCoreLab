package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.HardwareComponent;
import cn.gcc.gearcorelab.model.User;
import cn.gcc.gearcorelab.service.HardwareComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hardware")
public class HardwareComponentController {
    
    @Autowired
    private HardwareComponentService hardwareComponentService;
    
    /**
     * 获取所有硬件组件
     */
    @GetMapping("/components")
    public ResponseEntity<Map<String, Object>> getAllComponents() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HardwareComponent> components = hardwareComponentService.getAllComponents();
            
            // 按类型分组
            Map<String, List<HardwareComponent>> groupedComponents = new HashMap<>();
            for (HardwareComponent component : components) {
                String type = component.getType();
                groupedComponents.computeIfAbsent(type, k -> new java.util.ArrayList<>()).add(component);
            }
            
            response.put("success", true);
            response.put("data", groupedComponents);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取硬件组件失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据类型获取硬件组件
     */
    @GetMapping("/components/{type}")
    public ResponseEntity<Map<String, Object>> getComponentsByType(@PathVariable String type) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HardwareComponent> components = hardwareComponentService.getComponentsByType(type);
            response.put("success", true);
            response.put("data", components);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取硬件组件失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 搜索硬件组件
     */
    @GetMapping("/components/search")
    public ResponseEntity<Map<String, Object>> searchComponents(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HardwareComponent> components = hardwareComponentService.searchComponents(keyword);
            response.put("success", true);
            response.put("data", components);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索硬件组件失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取硬件组件统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Integer> stats = hardwareComponentService.getComponentStatistics();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取统计信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取硬件组件详情
     */
    @GetMapping("/components/detail/{id}")
    public ResponseEntity<Map<String, Object>> getComponentDetail(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            HardwareComponent component = hardwareComponentService.getComponentById(id);
            if (component != null) {
                response.put("success", true);
                response.put("data", component);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "硬件组件不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取硬件组件详情失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 添加硬件组件（管理员权限）
     */
    @PostMapping("/components")
    public ResponseEntity<Map<String, Object>> addComponent(@RequestBody HardwareComponent component, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查管理员权限
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            response.put("success", false);
            response.put("message", "权限不足");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            boolean success = hardwareComponentService.addComponent(component);
            if (success) {
                response.put("success", true);
                response.put("message", "添加成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新硬件组件（管理员权限）
     */
    @PutMapping("/components/{id}")
    public ResponseEntity<Map<String, Object>> updateComponent(@PathVariable Long id, @RequestBody HardwareComponent component, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查管理员权限
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            response.put("success", false);
            response.put("message", "权限不足");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            component.setId(id);
            boolean success = hardwareComponentService.updateComponent(component);
            if (success) {
                response.put("success", true);
                response.put("message", "更新成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除硬件组件（管理员权限）
     */
    @DeleteMapping("/components/{id}")
    public ResponseEntity<Map<String, Object>> deleteComponent(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查管理员权限
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            response.put("success", false);
            response.put("message", "权限不足");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            boolean success = hardwareComponentService.deleteComponent(id);
            if (success) {
                response.put("success", true);
                response.put("message", "删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 初始化默认硬件数据（管理员权限）
     */
    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializeData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 检查管理员权限
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoles().contains("ADMIN")) {
            response.put("success", false);
            response.put("message", "权限不足");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            hardwareComponentService.initializeDefaultData();
            response.put("success", true);
            response.put("message", "初始化成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "初始化失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}