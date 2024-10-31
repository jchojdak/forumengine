package com.forumengine.comment;

import com.forumengine.category.Category;
import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.post.Post;
import com.forumengine.post.PostRepository;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long POST_ID = 1L;

    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_CONTENT = "Test content";

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    private Category category;
    private User author;
    private Post post;
    private Comment comment;
    private CommentDTO commentDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        author = new User();
        author.setId(AUTHOR_ID);
        author.setUsername(AUTHOR_USERNAME);

        now = LocalDateTime.now();

        post = new Post();
        post.setId(POST_ID);
        post.setAuthor(author);
        post.setCategory(category);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(COMMENT_CONTENT);

        commentDTO = new CommentDTO();
        commentDTO.setId(COMMENT_ID);
        commentDTO.setPostId(POST_ID);
        commentDTO.setAuthorId(AUTHOR_ID);
        commentDTO.setContent(COMMENT_CONTENT);
    }

    @Test
    void testCreateComment() {
        // given
        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setContent(COMMENT_CONTENT);

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(AUTHOR_USERNAME)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDTO(any(Comment.class))).thenReturn(commentDTO);

        // when
        CommentDTO result = commentService.createComment(POST_ID, createCommentDTO, AUTHOR_USERNAME);

        // then
        assertNotNull(result);
        assertEquals(commentDTO, result);

        verify(postRepository).findById(POST_ID);
        verify(userRepository).findByUsername(AUTHOR_USERNAME);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toCommentDTO(comment);
    }

}
