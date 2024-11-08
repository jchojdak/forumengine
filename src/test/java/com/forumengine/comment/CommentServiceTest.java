package com.forumengine.comment;

import com.forumengine.category.Category;
import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.exception.CommentNotBelongToPostException;
import com.forumengine.exception.EntityNotFoundException;
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
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long POST_ID = 1L;
    private static final Long SECOND_POST_ID = 2L;
    private static final Long INVALID_POST_ID = 404L;

    private static final Long COMMENT_ID = 1L;
    private static final Long INVALID_COMMENT_ID = 404L;
    private static final String COMMENT_CONTENT = "Test content";

    private static final String SORT_PROPERTIES = "createdAt";

    private static final String NOT_FOUND_MESSAGE = "%s not found";
    private static final String POST_NOT_FOUND_MESSAGE = "Post %s not found";
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment %s not found";
    private static final String COMMENT_NOT_BELONG_TO_POST_MESSAGE = "The comment %s does not belong to the post %s.";

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

    @Test
    void testGetAllComments() {
        // given
        Long postId = 1L;
        Integer page = 0;
        Integer size = 1;
        Sort.Direction sort = Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, SORT_PROPERTIES));

        Page<Comment> commentsPage = new PageImpl<>(List.of(comment, comment), pageable, 1);

        when(commentRepository.findAllByPostId(POST_ID, pageable)).thenReturn(commentsPage);
        when(commentMapper.toCommentDTOs(anyList())).thenReturn(List.of(commentDTO, commentDTO));

        // when
        List<CommentDTO> result = commentService.getAllComments(postId, page, size, sort);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(commentDTO, result.get(0));
        assertEquals(commentDTO, result.get(1));

        verify(commentRepository).findAllByPostId(POST_ID, pageable);
        verify(commentMapper).toCommentDTOs(anyList());
    }

    @Test
    void testGetAllComments_throwEntityNotException_whenPostNotFound() {
        // given
        Long postId = INVALID_POST_ID;
        Integer page = 0;
        Integer size = 1;
        Sort.Direction sort = Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, SORT_PROPERTIES));

        when(commentRepository.findAllByPostId(postId, pageable)).thenReturn(Page.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            commentService.getAllComments(postId, page, size, sort);
        });

        // then
        assertNotNull(result);
        assertEquals(NOT_FOUND_MESSAGE.formatted(INVALID_POST_ID), result.getMessage());

        verify(commentRepository).findAllByPostId(postId, pageable);
        verify(commentMapper, never()).toCommentDTOs(anyList());
    }

    @Test
    void testGetAllComments_withNullParameters() {
        // given
        Long postId = 1L;
        Integer page = null;
        Integer size = null;
        Sort.Direction sort = null;

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, SORT_PROPERTIES));

        Page<Comment> commentsPage = new PageImpl<>(List.of(comment, comment), pageable, 1);

        when(commentRepository.findAllByPostId(POST_ID, pageable)).thenReturn(commentsPage);
        when(commentMapper.toCommentDTOs(anyList())).thenReturn(List.of(commentDTO, commentDTO));

        // when
        List<CommentDTO> result = commentService.getAllComments(postId, page, size, sort);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(commentDTO, result.get(0));
        assertEquals(commentDTO, result.get(1));

        verify(commentRepository).findAllByPostId(POST_ID, pageable);
        verify(commentMapper).toCommentDTOs(anyList());
    }

    @Test
    void testGetCommentById() {
        // given
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentMapper.toCommentDTO(any(Comment.class))).thenReturn(commentDTO);

        // when
        CommentDTO result = commentService.getCommentById(POST_ID, COMMENT_ID);

        // then
        assertNotNull(result);
        assertEquals(commentDTO, result);
        verify(postRepository).findById(anyLong());
        verify(commentRepository).findById(anyLong());
        verify(commentMapper).toCommentDTO(any(Comment.class));
    }

    @Test
    void testGetCommentById_throwEntityNotException_whenPostNotFound() {
        // given
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            commentService.getCommentById(INVALID_POST_ID, COMMENT_ID);
        });

        // then
        assertNotNull(result);
        assertEquals(POST_NOT_FOUND_MESSAGE.formatted(INVALID_POST_ID), result.getMessage());

        verify(postRepository).findById(INVALID_POST_ID);
        verifyNoInteractions(commentRepository, commentMapper);
    }

    @Test
    void testGetCommentById_throwEntityNotException_whenCommentNotFound() {
        // given
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            commentService.getCommentById(POST_ID, INVALID_COMMENT_ID);
        });

        // then
        assertNotNull(result);
        assertEquals(COMMENT_NOT_FOUND_MESSAGE.formatted(INVALID_COMMENT_ID), result.getMessage());

        verify(postRepository).findById(POST_ID);
        verify(commentRepository).findById(INVALID_COMMENT_ID);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testGetCommentById_throwCommentNotBelongToPostException() {
        // given
        Post secondPost = new Post();
        secondPost.setId(SECOND_POST_ID);
        comment.setPost(secondPost);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // when
        CommentNotBelongToPostException result = assertThrows(CommentNotBelongToPostException.class, () -> {
            commentService.getCommentById(POST_ID, COMMENT_ID);
        });

        // then
        assertNotNull(result);
        assertEquals(COMMENT_NOT_BELONG_TO_POST_MESSAGE.formatted(POST_ID, COMMENT_ID), result.getMessage());

        verify(postRepository).findById(POST_ID);
        verify(commentRepository).findById(COMMENT_ID);
        verifyNoInteractions(commentMapper);
    }
}
