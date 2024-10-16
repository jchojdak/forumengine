package com.forumengine.post.dto;

import com.forumengine.comment.CommentDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
