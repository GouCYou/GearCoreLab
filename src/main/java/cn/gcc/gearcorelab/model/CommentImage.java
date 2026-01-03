package cn.gcc.gearcorelab.model;

import lombok.Data;

@Data
public class CommentImage {
    private Long id;
    private Long commentId;
    private String imageUrl;
}
