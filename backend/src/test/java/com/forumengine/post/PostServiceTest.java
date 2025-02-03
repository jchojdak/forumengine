package com.forumengine.post;

import com.forumengine.TestUtils;
import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.exception.AccessDeniedException;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostCommentsDTO;
import com.forumengine.post.dto.PostDTO;
import com.forumengine.post.dto.UpdatePostRequest;
import com.forumengine.security.CustomUserDetails;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";
    private static final String INVALID_AUTHOR_USERNAME = "invalidAuthor";

    private static final Long CATEGORY_ID = 1L;
    private static final Long INVALID_CATEGORY_ID = 404L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long POST_ID = 1L;
    private static final Long INVALID_POST_ID = 404L;
    private static final String POST_TITLE = "Example post title";
    private static final String NEW_POST_TITLE = "Hello world";
    private static final String POST_CONTENT = "Example post content";
    private static final String NEW_POST_CONTENT = "Hello world, new content";

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private Authentication auth;

    private Category category;
    private User author;
    private Post post;
    private PostDTO postDTO;
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
        post.setComments(List.of());
        post.setCategory(category);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        postDTO = new PostDTO();
        postDTO.setId(POST_ID);
        postDTO.setAuthorId(author.getId());
        postDTO.setCategoryId(category.getId());
        postDTO.setTitle(POST_TITLE);
        postDTO.setContent(POST_CONTENT);
        postDTO.setCreatedAt(now);
        postDTO.setUpdatedAt(now);
    }

    @Test
    void testCreatePost() {
        // given
        String authorName = AUTHOR_USERNAME;

        CreatePostDTO createPostDTO = new CreatePostDTO();
        createPostDTO.setCategoryId(CATEGORY_ID);
        createPostDTO.setContent(POST_CONTENT);
        createPostDTO.setTitle(POST_TITLE);

        when(userRepository.findByUsername(authorName)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(postMapper.toPost(createPostDTO)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toPostDTO(post)).thenReturn(postDTO);

        // when
        PostDTO result = postService.createPost(createPostDTO, authorName);

        // then
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals(author.getId(), result.getAuthorId());
        assertEquals(category.getId(), result.getCategoryId());
        assertEquals(createPostDTO.getTitle(), result.getTitle());
        assertEquals(createPostDTO.getContent(), result.getContent());

        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(postMapper).toPost(createPostDTO);
        verify(postRepository).save(post);
        verify(userRepository).findByUsername(authorName);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(postMapper).toPostDTO(post);
    }

    @Test
    void testCreatePost_throwsNotFoundException_whenCategoryNotFound() {
        // given
        String authorName = AUTHOR_USERNAME;

        CreatePostDTO createPostDTO = new CreatePostDTO();
        createPostDTO.setCategoryId(INVALID_CATEGORY_ID);
        createPostDTO.setContent(POST_CONTENT);
        createPostDTO.setTitle(POST_TITLE);

        when(userRepository.findByUsername(authorName)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(INVALID_CATEGORY_ID)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            postService.createPost(createPostDTO, authorName);
        });

        // then
        assertEquals(INVALID_CATEGORY_ID + " not found", result.getMessage());

        verify(userRepository).findByUsername(authorName);
        verify(categoryRepository).findById(INVALID_CATEGORY_ID);
        verify(postMapper, never()).toPost(any(CreatePostDTO.class));
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testCreatePost_throwsNotFoundException_whenUserNotFound() {
        // given
        String authorName = INVALID_AUTHOR_USERNAME;

        CreatePostDTO createPostDTO = new CreatePostDTO();
        createPostDTO.setCategoryId(CATEGORY_ID);
        createPostDTO.setContent(POST_CONTENT);
        createPostDTO.setTitle(POST_TITLE);

        when(userRepository.findByUsername(authorName)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            postService.createPost(createPostDTO, authorName);
        });

        // then
        assertEquals(INVALID_AUTHOR_USERNAME + " not found", result.getMessage());

        verify(userRepository).findByUsername(authorName);
        verify(categoryRepository, never()).findById(anyLong());
        verify(postMapper, never()).toPost(any(CreatePostDTO.class));
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testGetAllPosts() {
        // given
        Integer page = 0;
        Integer size = 1;
        Sort.Direction sort = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, "createdAt"));

        Page<Post> postsPage = new PageImpl<>(List.of(post), pageable, 1);

        when(postRepository.findAll(pageable)).thenReturn(postsPage);
        when(postMapper.toPostDTOs(anyList())).thenReturn(List.of(postDTO));

        // when
        List<PostDTO> result = postService.getAllPosts(page, size, sort);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(postDTO, result.get(0));

        verify(postRepository).findAll(pageable);
        verify(postMapper).toPostDTOs(anyList());
    }

    @Test
    void getAllPostsByCategoryId() {
        // given
        Integer page = 0;
        Integer size = 1;
        Sort.Direction sort = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, "createdAt"));

        Page<Post> postsPage = new PageImpl<>(List.of(post), pageable, 1);

        when(postRepository.findByCategoryId(CATEGORY_ID, pageable)).thenReturn(postsPage);
        when(postMapper.toPostDTOs(anyList())).thenReturn(List.of(postDTO));

        // when
        List<PostDTO> result = postService.getAllPostsByCategoryId(page, size, sort, CATEGORY_ID);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(postDTO, result.get(0));

        verify(postRepository).findByCategoryId(CATEGORY_ID, pageable);
        verify(postMapper).toPostDTOs(anyList());
    }

    @Test
    void testGetAllPosts_withNullParameters() {
        // given
        Integer page = null;
        Integer size = null;
        Sort.Direction sort = null;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "createdAt"));

        Page<Post> postsPage = new PageImpl<>(List.of(post), pageable, 1);

        when(postRepository.findAll(pageable)).thenReturn(postsPage);
        when(postMapper.toPostDTOs(anyList())).thenReturn(List.of(postDTO));

        // when
        List<PostDTO> result = postService.getAllPosts(page, size, sort);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(postDTO, result.get(0));

        verify(postRepository).findAll(pageable);
        verify(postMapper).toPostDTOs(anyList());
    }

    @Test
    void testGetPostById() {
        // given
        Long id = POST_ID;

        PostCommentsDTO postCommentsDTO = new PostCommentsDTO();
        postCommentsDTO.setId(id);
        postCommentsDTO.setTitle(post.getTitle());
        postCommentsDTO.setCategoryId(category.getId());
        postCommentsDTO.setUpdatedAt(now);
        postCommentsDTO.setCreatedAt(now);
        postCommentsDTO.setAuthorId(author.getId());
        postCommentsDTO.setContent(post.getContent());

        List<CommentDTO> comments = TestUtils.generateCommentDTOs(3);

        postCommentsDTO.setComments(comments);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toPostCommentsDTO(any())).thenReturn(postCommentsDTO);

        // when
        PostCommentsDTO result = postService.getPostById(id);

        // then
        assertNotNull(result);
        assertEquals(postCommentsDTO.getId(), result.getId());
        assertEquals(postCommentsDTO.getTitle(), result.getTitle());
        assertEquals(postCommentsDTO.getContent(), result.getContent());
        assertEquals(postCommentsDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(postCommentsDTO.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(3, result.getComments().size());

        verify(postRepository).findById(id);
        verify(postMapper).toPostCommentsDTO(any(Post.class));
    }

    @Test
    void testGetPostById_throwsNotFoundException_whenPostNotFound() {
        // given
        Long id = INVALID_POST_ID;

        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            postService.getPostById(id);
        });

        // then
        assertNotNull(result);
        assertEquals(INVALID_POST_ID + " not found", result.getMessage());

        verify(postRepository).findById(id);
        verify(postMapper, never()).toPostCommentsDTO(any(Post.class));
    }

    @Test
    void testUpdatePostById() {
        // given
        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle(NEW_POST_TITLE);
        request.setContent(NEW_POST_CONTENT);

        Post updatedPost = post;
        updatedPost.setTitle(NEW_POST_TITLE);
        updatedPost.setContent(NEW_POST_CONTENT);

        PostDTO updatedPostDTO = postDTO;
        updatedPostDTO.setTitle(NEW_POST_TITLE);
        updatedPostDTO.setContent(NEW_POST_CONTENT);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(author));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toPostDTO(any(Post.class))).thenReturn(updatedPostDTO);

        // when
        PostDTO result = postService.updatePostById(POST_ID, AUTHOR_USERNAME, request);

        // then
        assertNotNull(result);
        assertEquals(NEW_POST_TITLE, result.getTitle());
        assertEquals(NEW_POST_CONTENT, result.getContent());

        verify(postRepository).findById(POST_ID);
        verify(userRepository).findByUsername(AUTHOR_USERNAME);
        verify(postRepository).save(any(Post.class));
        verify(postMapper).toPostDTO(any(Post.class));
    }

    @Test
    void testUpdatePostById_throwsAccessDeniedException_whenUserIsNotAuthor() {
        // given
        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle(NEW_POST_TITLE);
        request.setContent(NEW_POST_CONTENT);

        Post updatedPost = post;
        updatedPost.setTitle(NEW_POST_TITLE);
        updatedPost.setContent(NEW_POST_CONTENT);

        PostDTO updatedPostDTO = postDTO;
        updatedPostDTO.setTitle(NEW_POST_TITLE);
        updatedPostDTO.setContent(NEW_POST_CONTENT);

        User anotherUser = new User();
        anotherUser.setId(AUTHOR_ID + 1);
        anotherUser.setUsername(INVALID_AUTHOR_USERNAME);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(anotherUser));

        // when
        AccessDeniedException result = assertThrows(AccessDeniedException.class, () -> {
            postService.updatePostById(POST_ID, INVALID_AUTHOR_USERNAME, request);
        });

        // then
        assertNotNull(result);

        verify(postRepository).findById(POST_ID);
        verify(userRepository).findByUsername(INVALID_AUTHOR_USERNAME);
        verify(postRepository, never()).save(any(Post.class));
        verify(postMapper, never()).toPostDTO(any(Post.class));
    }

    @Test
    void testDeletePostById() {
        // given
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(author));

        // when
        postService.deletePostById(POST_ID, auth);

        // then
        verify(postRepository).delete(any(Post.class));
    }

    @Test
    void testDeletePostById_throwsEntityNotFoundException_whenPostIsNotFound() {
        // given
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());
        //when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(author));

        // when
        assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePostById(POST_ID, auth);
        });

        // then
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void testDeletePostById_throwsAccessDeniedException_whenUserIsNotAuthor() {
        // given
        User anotherUser = new User();
        anotherUser.setId(AUTHOR_ID + 1);
        anotherUser.setUsername(AUTHOR_USERNAME.repeat(2));

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(anotherUser));

        // when
        assertThrows(AccessDeniedException.class, () -> {
            postService.deletePostById(POST_ID, auth);
        });

        // then
        verify(postRepository, never()).delete(any(Post.class));
    }

}
