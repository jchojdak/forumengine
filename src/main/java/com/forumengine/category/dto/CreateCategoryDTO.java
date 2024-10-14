package com.forumengine.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDTO {

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @Size(max = 255)
    private String description;

}
