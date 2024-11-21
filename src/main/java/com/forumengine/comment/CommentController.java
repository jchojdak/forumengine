package com.forumengine.comment;

import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.comment.dto.UpdateCommentRequest;
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

import java.security.Principal;
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
    public CommentDTO createComment(@PathVariable Long postId,
                                    @RequestBody @Valid CreateCommentDTO createCommentDTO,
                                    Authentication authentication) {
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

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully deleted."),
            @ApiResponse(responseCode = "400", description = "The comment does not belong to the post.", content = @Content),
            @ApiResponse(responseCode = "403", description = "User doesn't have permission.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post or comment not found.", content = @Content)
    })
    public void deleteCommentById(@PathVariable Long postId,
                           @PathVariable Long commentId,
                           Authentication auth) {
        commentService.deleteCommentById(postId, commentId, auth);
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "Get comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comment."),
            @ApiResponse(responseCode = "400", description = "The comment does not belong to the post.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post or comment not found.", content = @Content)
    })
    public CommentDTO getById(@PathVariable Long postId,
                              @PathVariable Long commentId) {
        return commentService.getCommentById(postId, commentId);
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Update comment by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully updated."),
            @ApiResponse(responseCode = "400", description = "The comment does not belong to the post.", content = @Content),
            @ApiResponse(responseCode = "403", description = "User doesn't have permission.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post or comment not found.", content = @Content)
    })
    public CommentDTO updateCommentById(@PathVariable Long postId,
                                        @PathVariable Long commentId,
                                        @RequestBody @Valid UpdateCommentRequest request,
                                        Principal principal) {
        String username = principal.getName();

        return commentService.updateCommentById(postId, commentId, username, request);
    }

}
