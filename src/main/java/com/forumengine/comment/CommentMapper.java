package com.forumengine.comment;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setAuthorId(comment.getAuthor().getId());
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());

        return commentDTO;
    }

    public List<CommentDTO> toCommentDTOs(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

}
