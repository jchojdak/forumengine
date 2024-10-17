package com.forumengine.post;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class PostIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Category testCategory;

    private static final String ENDPOINT = "/posts";

    private static final Long CATEGORY_ID = 1L;
    private static final Long INVALID_CATEGORY_ID = 404L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";
    private static final String AUTHOR_ROLE = "USER";

    private static final Long POST_ID = 1L;
    private static final Long INVALID_POST_ID = 404L;
    private static final String POST_TITLE = "Example post title";
    private static final String POST_CONTENT = "Example post content";

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
                .andExpect(jsonPath("$.id").value(POST_ID))
                .andExpect(jsonPath("$.authorId").value(AUTHOR_ID))
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
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
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
}
