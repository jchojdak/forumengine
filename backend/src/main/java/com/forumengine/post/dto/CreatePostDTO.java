package com.forumengine.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostDTO {

    @NotNull
    private Long categoryId;

    @Size(min = 1, max = 30)
    private String title;

    @Size(min = 1, max = 512)
    private String content;

}
