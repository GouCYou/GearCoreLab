package cn.gcc.gearcorelab.mapper;

import cn.gcc.gearcorelab.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void insert(User user);
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);

    // ----- 新增这两个方法 -----
    User findById(@Param("id") Long id);
    void update(User user);
    java.util.List<String> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 获取所有用户列表
     */
    java.util.List<User> findAll();
    
    /**
     * 搜索用户
     */
    java.util.List<User> searchUsers(String query);

    /**
     * 封禁用户
     */
    void banUser(@Param("userId") Long userId,
                 @Param("banType") String banType,
                 @Param("banReason") String banReason,
                 @Param("banDuration") Long banDuration);

    /**
     * 解封用户
     */
    void unbanUser(@Param("userId") Long userId);

    /**
     * 分页获取用户列表
     */
    java.util.List<User> getAllUsersWithPagination(@Param("offset") int offset, @Param("size") int size);

    /**
     * 获取用户总数
     */
    long countAllUsers();
    
    /**
     * 插入用户角色
     */
    void insertUserRole(@Param("userId") Long userId, @Param("roleName") String roleName);
}
