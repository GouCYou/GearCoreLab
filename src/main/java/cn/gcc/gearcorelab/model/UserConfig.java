package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserConfig {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String configData; // JSON格式存储配置信息
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}