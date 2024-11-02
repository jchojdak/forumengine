package com.forumengine.comment;

import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
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
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Add a new comment to the post", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New comment successfully added to the post."),
            @ApiResponse(responseCode = "404", description = "Post not found.", content = @Content)
    })
    public CommentDTO createComment(@PathVariable Long postId, @RequestBody @Valid CreateCommentDTO createCommentDTO, Authentication authentication) {
        String authorName = authentication.getName();

        return commentService.createComment(postId, createCommentDTO, authorName);
    }

    @GetMapping
    @Operation(summary = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of comments."),
            @ApiResponse(responseCode = "404", description = "Comments not found.", content = @Content)
    })
    public List<CommentDTO> getAllComments(@PathVariable Long postId,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) Sort.Direction sort) {
        return commentService.getAllComments(postId, page, size, sort);
    }

}
