package com.forumengine.category;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.category.dto.CreateCategoryDTO;
import jakarta.transaction.Transactional;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200AndCategoryDetails_whenCategoryWasCreated() throws Exception {
        // given
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        createCategoryDTO.setName("Test name");
        createCategoryDTO.setDescription("Test description");

        // when
        ResultActions result = mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(createCategoryDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.description").value("Test description"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn409_whenDuplicateCategoryIsPosted() throws Exception {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName("Example category name");
        category.setDescription("Example description");
        categoryRepository.save(category);

        CreateCategoryDTO duplicateCategory = new CreateCategoryDTO();
        duplicateCategory.setName(category.getName());
        duplicateCategory.setDescription(category.getDescription());

        // when
        ResultActions result = mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(duplicateCategory)));

        // then
        result.andExpect(status().is(409));
    }

    @Test
    void shouldReturn200AndCategoryDetails_whenGetById() throws Exception {
        // given
        Category category = new Category();
        category.setName("Test 123");
        category.setDescription("Test description");
        Category savedCategory = categoryRepository.save(category);

        // when
        ResultActions result = mockMvc.perform(get("/categories/" + savedCategory.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Test 123"))
                .andExpect(jsonPath("$.description").value("Test description"));
    }

    @Test
    void shouldReturn404_whenGetByIdAndCategoryNotFound() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/categories/8329293")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().is(404));
    }

    @Test
    void shouldReturn200AndCategoryList_whenGetCategories() throws Exception {
        // given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Description 1");
        Category savedCategory1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Description 2");
        Category savedCategory2 = categoryRepository.save(category2);

        // when
        ResultActions result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(savedCategory1.getId()))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].id").value(savedCategory2.getId()))
                .andExpect(jsonPath("$[1].name").value("Category 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200_whenCategoryDeletedSuccessfully() throws Exception {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setName("Delete me");
        category.setDescription("Description");
        categoryRepository.save(category);

        // when
        ResultActions result = mockMvc.perform(delete("/categories/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200));

        // and
        mockMvc.perform(get("/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
