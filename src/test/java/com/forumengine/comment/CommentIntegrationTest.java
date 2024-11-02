package com.forumengine.comment;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.post.Post;
import com.forumengine.post.PostRepository;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CommentIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT = "/posts/%s/comments";

    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_USERNAME = "testAuthor";
    private static final String AUTHOR_ROLE = "USER";

    private static final Long CATEGORY_ID = 1L;

    private static final Long POST_ID = 1L;
    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_DESCRIPTION = "Test description";

    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_CONTENT = "Test content";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Category testCategory;
    private Post testPost;
    private LocalDateTime now;

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

        now = LocalDateTime.now();

        Post post = new Post();
        post.setId(POST_ID);
        post.setAuthor(testUser);
        post.setCategory(testCategory);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        testPost = postRepository.save(post);
    }

    @Test
    @Transactional
    @WithMockUser(username = AUTHOR_USERNAME, roles = AUTHOR_ROLE)
    void shouldReturn200AndCommentDetails_whenCommentWasCreated() throws Exception {
        // given
        Long postId = testPost.getId();

        CreateCommentDTO createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setContent(COMMENT_CONTENT);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT.formatted(postId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(createCommentDTO)));

        //then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.postId").value(testPost.getId()))
                .andExpect(jsonPath("$.authorId").value(testUser.getId()))
                .andExpect(jsonPath("$.content").value(COMMENT_CONTENT));
    }

    @Test
    @Transactional
    void shouldReturn200AndListOfComments_whenGetAllComments() throws Exception {
        // given
        Long postId = testPost.getId();

        Comment comment1 = new Comment();
        comment1.setContent(COMMENT_CONTENT);
        comment1.setPost(testPost);
        comment1.setAuthor(testUser);
        Comment savedComment1 = commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent(COMMENT_CONTENT + COMMENT_CONTENT);
        comment2.setPost(testPost);
        comment2.setAuthor(testUser);
        Comment savedComment2 = commentRepository.save(comment2);

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT.formatted(postId))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(savedComment1.getId()))
                .andExpect(jsonPath("$[0].content").value(COMMENT_CONTENT))
                .andExpect(jsonPath("$[0].postId").value(postId))
                .andExpect(jsonPath("$[0].authorId").value(testUser.getId()))
                .andExpect(jsonPath("$[1].id").value(savedComment2.getId()))
                .andExpect(jsonPath("$[1].content").value(COMMENT_CONTENT + COMMENT_CONTENT))
                .andExpect(jsonPath("$[1].postId").value(postId))
                .andExpect(jsonPath("$[1].authorId").value(testUser.getId()));
    }

}
