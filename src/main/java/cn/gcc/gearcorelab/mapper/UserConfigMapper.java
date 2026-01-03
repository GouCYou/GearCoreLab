package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.UserConfig;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserConfigMapper {
    
    @Select("SELECT * FROM user_configs WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<UserConfig> findByUserId(Long userId);
    
    @Select("SELECT * FROM user_configs WHERE id = #{id} AND user_id = #{userId}")
    UserConfig findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    @Insert("INSERT INTO user_configs (user_id, title, description, config_data, created_at, updated_at) " +
            "VALUES (#{userId}, #{title}, #{description}, #{configData}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserConfig userConfig);
    
    @Update("UPDATE user_configs SET title = #{title}, description = #{description}, " +
            "config_data = #{configData}, updated_at = #{updatedAt} WHERE id = #{id} AND user_id = #{userId}")
    int update(UserConfig userConfig);
    
    @Delete("DELETE FROM user_configs WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM user_configs WHERE user_id = #{userId}")
    int countByUserId(Long userId);
    
    @Select("SELECT * FROM user_configs WHERE id = #{id}")
    UserConfig findById(Long id);
}