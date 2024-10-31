package com.forumengine.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentDTO {

    @NotBlank
    @Size(min = 2, max = 300)
    private String content;

}
