package com.forumengine.post;

import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "Add a new post", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New post successfully added."),
            @ApiResponse(responseCode = "404", description = "Category not found.", content = @Content)
    })
    public PostDTO createPost(@RequestBody @Valid CreatePostDTO createPostDTO, Authentication authentication) {
        String authorName = authentication.getName();

        return postService.createPost(createPostDTO, authorName);
    }

    @GetMapping
    @Operation(summary = "Get all posts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of posts.")
    public List<PostDTO> getAllPosts(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) Sort.Direction sort) {
        return postService.getAllPosts(page, size, sort);
    }
}
