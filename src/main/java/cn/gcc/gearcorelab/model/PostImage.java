package cn.gcc.gearcorelab.model;

import lombok.Data;

@Data
public class PostImage {
    private Long id;
    private Long postId;
    private String imageUrl;
    private Integer sortOrder;
}
