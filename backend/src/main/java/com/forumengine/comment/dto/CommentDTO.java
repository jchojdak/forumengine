package com.forumengine.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Long id;
    private Long postId;
    private Long authorId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
