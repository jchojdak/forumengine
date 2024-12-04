package com.forumengine.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {

    private Long id;
    private Long authorId;
    private Long categoryId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
