package com.forumengine.category;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.category.dto.CreateCategoryDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class CategoryIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT = "/categories";
    private static final String NOT_FOUND_ENDPOINT = "/categories/8329293";
    private static final String BY_ID_ENDPOINT = "/categories/%s";

    private static final String ROLE_ADMIN = "ADMIN";

    private static final String FIRST_CATEGORY_NAME = "Test name";
    private static final String FIRST_CATEGORY_DESCRIPTION = "Test description";

    private static final String SECOND_CATEGORY_NAME = "Example category name";
    private static final String SECOND_CATEGORY_DESCRIPTION = "Example description";

    private static final String THIRD_CATEGORY_NAME = "Test 123";
    private static final String THIRD_CATEGORY_DESCRIPTION = "Test description";

    private static final String FOURTH_CATEGORY_NAME = "Category 1";
    private static final String FOURTH_CATEGORY_DESCRIPTION = "Description 1";

    private static final String FIFTH_CATEGORY_NAME = "Category 2";
    private static final String FIFTH_CATEGORY_DESCRIPTION = "Description 2";

    private static final String SIXTH_CATEGORY_NAME = "Delete me";
    private static final String SIXTH_CATEGORY_DESCRIPTION = "Description";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldReturn200AndCategoryDetails_whenCategoryWasCreated() throws Exception {
        // given
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        createCategoryDTO.setName(FIRST_CATEGORY_NAME);
        createCategoryDTO.setDescription(FIRST_CATEGORY_DESCRIPTION);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(createCategoryDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value(FIRST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(FIRST_CATEGORY_DESCRIPTION));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldReturn409_whenDuplicateCategoryIsPosted() throws Exception {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName(SECOND_CATEGORY_NAME);
        category.setDescription(SECOND_CATEGORY_DESCRIPTION);
        categoryRepository.save(category);

        CreateCategoryDTO duplicateCategory = new CreateCategoryDTO();
        duplicateCategory.setName(category.getName());
        duplicateCategory.setDescription(category.getDescription());

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(duplicateCategory)));

        // then
        result.andExpect(status().is(409));
    }

    @Test
    void shouldReturn200AndCategoryDetails_whenGetById() throws Exception {
        // given
        Category category = new Category();
        category.setName(THIRD_CATEGORY_NAME);
        category.setDescription(THIRD_CATEGORY_DESCRIPTION);
        Category savedCategory = categoryRepository.save(category);

        // when
        ResultActions result = mockMvc.perform(get(BY_ID_ENDPOINT.formatted(savedCategory.getId()))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value(THIRD_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(THIRD_CATEGORY_DESCRIPTION));
    }

    @Test
    void shouldReturn404_whenGetByIdAndCategoryNotFound() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(NOT_FOUND_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().is(404));
    }

    @Test
    void shouldReturn200AndCategoryList_whenGetCategories() throws Exception {
        // given
        Category category1 = new Category();
        category1.setName(FOURTH_CATEGORY_NAME);
        category1.setDescription(FOURTH_CATEGORY_DESCRIPTION);
        Category savedCategory1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName(FIFTH_CATEGORY_NAME);
        category2.setDescription(FIFTH_CATEGORY_DESCRIPTION);
        Category savedCategory2 = categoryRepository.save(category2);

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                //.andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(savedCategory1.getId()))
                .andExpect(jsonPath("$[0].name").value(FOURTH_CATEGORY_NAME))
                .andExpect(jsonPath("$[0].description").value(FOURTH_CATEGORY_DESCRIPTION))
                .andExpect(jsonPath("$[1].id").value(savedCategory2.getId()))
                .andExpect(jsonPath("$[1].name").value(FIFTH_CATEGORY_NAME))
                .andExpect(jsonPath("$[1].description").value(FIFTH_CATEGORY_DESCRIPTION));

    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldReturn200_whenCategoryDeletedSuccessfully() throws Exception {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName(SIXTH_CATEGORY_NAME);
        category.setDescription(SIXTH_CATEGORY_DESCRIPTION);
        Category savedCategory = categoryRepository.save(category);

        // when
        ResultActions result = mockMvc.perform(delete(BY_ID_ENDPOINT.formatted(savedCategory.getId()))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200));

        // and
        mockMvc.perform(get(BY_ID_ENDPOINT.formatted(savedCategory.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
