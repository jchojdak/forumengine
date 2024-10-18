package com.forumengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forumengine.comment.Comment;
import com.forumengine.comment.CommentDTO;
import com.forumengine.user.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CommentDTO> generateCommentDTOs(int numberOfComments) {
        numberOfComments++;

        return IntStream.range(1, numberOfComments)
                .mapToObj(i -> {
                    CommentDTO comment = new CommentDTO();
                    comment.setId((long) i);
                    comment.setAuthorId((long) i);
                    comment.setAuthorId(1L + i);
                    comment.setContent("Comment " + i);
                    return comment;
                })
                .collect(Collectors.toList());
    }

    public static List<Comment> generateComments(int numberOfComments, User author) {
        numberOfComments++;

        return IntStream.range(1, numberOfComments)
                .mapToObj(i -> {
                    Comment comment = new Comment();
                    comment.setId((long) i);
                    comment.setAuthor(author);
                    comment.setContent("Comment " + i);
                    return comment;
                })
                .collect(Collectors.toList());
    }
}
