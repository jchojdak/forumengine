package com.forumengine.comment;

import org.springframework.stereotype.Component;

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

}
