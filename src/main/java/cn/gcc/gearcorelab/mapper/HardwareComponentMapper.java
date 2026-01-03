package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.HardwareComponent;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface HardwareComponentMapper {
    
    /**
     * 根据类型获取所有硬件组件
     */
    @Select("SELECT * FROM hardware_components WHERE type = #{type} AND is_active = 1 ORDER BY sort_order ASC, created_at DESC")
    List<HardwareComponent> findByType(@Param("type") String type);
    
    /**
     * 获取所有硬件组件
     */
    @Select("SELECT * FROM hardware_components WHERE is_active = 1 ORDER BY type, sort_order ASC, created_at DESC")
    List<HardwareComponent> findAll();
    
    /**
     * 根据ID获取硬件组件
     */
    @Select("SELECT * FROM hardware_components WHERE id = #{id}")
    HardwareComponent findById(@Param("id") Long id);
    
    /**
     * 插入新的硬件组件
     */
    @Insert("INSERT INTO hardware_components (name, brand, type, category, specifications, price, image_url, is_active, sort_order, created_at, updated_at) " +
            "VALUES (#{name}, #{brand}, #{type}, #{category}, #{specifications}, #{price}, #{imageUrl}, #{isActive}, #{sortOrder}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HardwareComponent component);
    
    /**
     * 更新硬件组件
     */
    @Update("UPDATE hardware_components SET name = #{name}, brand = #{brand}, type = #{type}, category = #{category}, " +
            "specifications = #{specifications}, price = #{price}, image_url = #{imageUrl}, is_active = #{isActive}, " +
            "sort_order = #{sortOrder}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(HardwareComponent component);
    
    /**
     * 删除硬件组件（软删除）
     */
    @Update("UPDATE hardware_components SET is_active = 0, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据类型统计数量
     */
    @Select("SELECT COUNT(*) FROM hardware_components WHERE type = #{type} AND is_active = 1")
    int countByType(@Param("type") String type);
    
    /**
     * 获取所有类型的统计信息
     */
    @Select("SELECT type, COUNT(*) as count FROM hardware_components WHERE is_active = 1 GROUP BY type")
    @Results({
        @Result(property = "type", column = "type"),
        @Result(property = "count", column = "count")
    })
    List<java.util.Map<String, Object>> getTypeStatistics();
    
    /**
     * 搜索硬件组件
     */
    @Select("SELECT * FROM hardware_components WHERE is_active = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR brand LIKE CONCAT('%', #{keyword}, '%')) ORDER BY sort_order ASC, created_at DESC")
    List<HardwareComponent> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 分页获取所有硬件组件（管理员用）
     */
    @Select("SELECT * FROM hardware_components ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<HardwareComponent> findAllWithPagination(@Param("offset") int offset, @Param("size") int size);
    
    /**
     * 按类型分页获取硬件组件（管理员用）
     */
    @Select("SELECT * FROM hardware_components WHERE type = #{type} ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<HardwareComponent> findByTypeWithPagination(@Param("type") String type, @Param("offset") int offset, @Param("size") int size);
    
    /**
     * 按关键词搜索硬件组件（管理员用，带分页）
     */
    @Select("SELECT * FROM hardware_components WHERE (name LIKE CONCAT('%', #{keyword}, '%') OR brand LIKE CONCAT('%', #{keyword}, '%')) ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<HardwareComponent> searchByKeywordWithPagination(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    
    /**
     * 按类型和关键词搜索硬件组件（管理员用，带分页）
     */
    @Select("SELECT * FROM hardware_components WHERE type = #{type} AND (name LIKE CONCAT('%', #{keyword}, '%') OR brand LIKE CONCAT('%', #{keyword}, '%')) ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<HardwareComponent> searchByTypeAndKeyword(@Param("type") String type, @Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
}