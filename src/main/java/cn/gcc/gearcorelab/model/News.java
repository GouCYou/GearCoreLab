package cn.gcc.gearcorelab.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class News {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String category;
    private String author;
    private String imageUrl;
    private Integer viewCount;
    private String status;
    private LocalDateTime publishTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}