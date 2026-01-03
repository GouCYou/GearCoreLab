package cn.gcc.gearcorelab.service;

import cn.gcc.gearcorelab.model.HardwareComponent;
import java.util.List;
import java.util.Map;

public interface HardwareComponentService {
    
    /**
     * 根据类型获取硬件组件列表
     */
    List<HardwareComponent> getComponentsByType(String type);
    
    /**
     * 获取所有硬件组件
     */
    List<HardwareComponent> getAllComponents();
    
    /**
     * 根据ID获取硬件组件
     */
    HardwareComponent getComponentById(Long id);
    
    /**
     * 添加硬件组件
     */
    boolean addComponent(HardwareComponent component);
    
    /**
     * 更新硬件组件
     */
    boolean updateComponent(HardwareComponent component);
    
    /**
     * 删除硬件组件
     */
    boolean deleteComponent(Long id);
    
    /**
     * 搜索硬件组件
     */
    List<HardwareComponent> searchComponents(String keyword);
    
    /**
     * 获取各类型组件统计信息
     */
    Map<String, Integer> getComponentStatistics();
    
    /**
     * 初始化默认硬件数据
     */
    void initializeDefaultData();
    
    /**
     * 批量导入硬件组件
     */
    boolean batchImportComponents(List<HardwareComponent> components);
    
    /**
     * 管理员获取硬件组件列表（带分页和搜索）
     */
    List<HardwareComponent> getAllComponentsForAdmin(int page, int size, String search, String type);
}