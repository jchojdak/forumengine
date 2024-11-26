package com.forumengine.post;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.comment.Comment;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.UpdatePostRequest;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class PostIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT = "/posts";

    private static final Long CATEGORY_ID = 1L;
    private static final Long INVALID_CATEGORY_ID = 404L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";
    private static final String INVALID_AUTHOR_USERNAME = "invalidUsername123";
    private static final String AUTHOR_ROLE = "USER";

    private static final String ERROR_MESSAGE_404 = "NOT_FOUND";
    private static final String ACCESS_DENIED_MESSAGE_UPDATE = "You do not have permission to update this post.";

    private static final Long POST_ID = 1L;
    private static final Long INVALID_POST_ID = 404L;
    private static final String POST_TITLE = "Example post title";
    private static final String NEW_POST_TITLE = "Hello world";
    private static final String POST_CONTENT = "Example post content";
    private static final String NEW_POST_CONTENT = "Hello world, new content";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User invalidUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        testCategory = categoryRepository.save(category);

        User user = new User();
        user.setId(AUTHOR_ID);
        user.setUsername(AUTHOR_USERNAME);

        testUser = userRepository.save(user);
    }

    @Test
    @Transactional
    @WithMockUser(username = AUTHOR_USERNAME, roles = AUTHOR_ROLE)
    void shouldReturn200AndPostDetails_whenPostWasCreated() throws Exception {
        // given
        CreatePostDTO createPostDTO = new CreatePostDTO();
        createPostDTO.setCategoryId(testCategory.getId());
        createPostDTO.setTitle(POST_TITLE);
        createPostDTO.setContent(POST_CONTENT);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(createPostDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.authorId").value(testUser.getId()))
                .andExpect(jsonPath("$.categoryId").value(testCategory.getId()))
                .andExpect(jsonPath("$.title").value(POST_TITLE))
                .andExpect(jsonPath("$.content").value(POST_CONTENT));
    }

    @Test
    @Transactional
    @WithMockUser(username = AUTHOR_USERNAME, roles = AUTHOR_ROLE)
    void shouldReturn404_whenPostNotCreatedAndCategoryNotFound() throws Exception {
        // given
        CreatePostDTO createPostDTO = new CreatePostDTO();
        createPostDTO.setCategoryId(INVALID_CATEGORY_ID);
        createPostDTO.setTitle(POST_TITLE);
        createPostDTO.setContent(POST_CONTENT);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(createPostDTO)));

        // then
        result.andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value(ERROR_MESSAGE_404))
                .andExpect(jsonPath("$.message").value(INVALID_POST_ID + " not found"));
    }

    @Test
    @Transactional
    void shouldReturn200AndPostList_whenGetAllPosts() throws Exception {
        // given
        Post post = new Post();
        post.setTitle(POST_TITLE);
        post.setContent(POST_CONTENT);
        post.setAuthor(testUser);
        post.setCategory(testCategory);
        Post savedPost = postRepository.save(post);

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(savedPost.getId()))
                .andExpect(jsonPath("$[0].authorId").value(testUser.getId()))
                .andExpect(jsonPath("$[0].categoryId").value(testCategory.getId()))
                .andExpect(jsonPath("$[0].title").value(POST_TITLE))
                .andExpect(jsonPath("$[0].content").value(POST_CONTENT));
    }

    @Test
    @Transactional
    void shouldReturn200AndPostDetails_whenGetPostById() throws Exception {
        // given
        List<Comment> comments = TestUtils.generateComments(3, testUser);

        Post post = new Post();
        post.setTitle(POST_TITLE);
        post.setContent(POST_CONTENT);
        post.setAuthor(testUser);
        post.setCategory(testCategory);

        for (Comment comment : comments) {
            comment.setPost(post);
        }
        post.setComments(comments);

        Post savedPost = postRepository.save(post);

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT + "/" + savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(savedPost.getId()))
                .andExpect(jsonPath("$.authorId").value(testUser.getId()))
                .andExpect(jsonPath("$.categoryId").value(testCategory.getId()))
                .andExpect(jsonPath("$.title").value(POST_TITLE))
                .andExpect(jsonPath("$.content").value(POST_CONTENT))
                .andExpect(jsonPath("$.comments", hasSize(3)))
                .andExpect(jsonPath("$.comments[0].content").value("Comment 1"))
                .andExpect(jsonPath("$.comments[1].content").value("Comment 2"))
                .andExpect(jsonPath("$.comments[2].content").value("Comment 3"));
    }

    @Test
    @Transactional
    void shouldReturn404_whenGetPostByIdAndNotFound() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT + "/" + INVALID_POST_ID)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(404));
    }

    @Test
    @Transactional
    @WithMockUser(username = AUTHOR_USERNAME, roles = AUTHOR_ROLE)
    void shouldReturn200AndPostDetails_whenPostIsUpdatedSuccessfully() throws Exception {
        // given
        Post post = new Post();
        post.setTitle(POST_TITLE);
        post.setContent(POST_CONTENT);
        post.setAuthor(testUser);
        post.setCategory(testCategory);
        Post savedPost = postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle(NEW_POST_TITLE);
        request.setContent(NEW_POST_CONTENT);

        // when
        ResultActions result = mockMvc.perform(patch(ENDPOINT + "/" + savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(request)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.title").value(NEW_POST_TITLE))
                .andExpect(jsonPath("$.content").value(NEW_POST_CONTENT));
    }

}
