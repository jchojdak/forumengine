package com.forumengine.category;

import com.forumengine.category.dto.CategoryDTO;
import com.forumengine.category.dto.CreateCategoryDTO;
import com.forumengine.exception.EntityAlreadyExistsException;
import com.forumengine.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 1");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
    }

    @Test
    void testCreateCategory() {
        // given
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        createCategoryDTO.setName(category.getName());
        createCategoryDTO.setDescription(category.getDescription());

        when(categoryMapper.toCategory(createCategoryDTO)).thenReturn(category);
        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        // when
        CategoryDTO result = categoryService.createCategory(createCategoryDTO);

        // then
        assertNotNull(result);
        assertEquals(result.getId(), category.getId());
        assertEquals(result.getName(), category.getName());
        assertEquals(result.getDescription(), category.getDescription());

        verify(categoryMapper).toCategory(createCategoryDTO);
        verify(categoryRepository).existsByName(category.getName());
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void testCreateCategory_throwEntityAlreadyExistsException_whenNameIsFound() {
        //given
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        createCategoryDTO.setName(category.getName());
        createCategoryDTO.setDescription(category.getDescription());

        when(categoryMapper.toCategory(createCategoryDTO)).thenReturn(category);
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        //when & then
        assertThrows(EntityAlreadyExistsException.class, () -> {
            categoryService.createCategory(createCategoryDTO);
        });

        verify(categoryMapper).toCategory(createCategoryDTO);
        verify(categoryRepository).existsByName(category.getName());
        verify(categoryRepository, never()).save(category);
        verify(categoryMapper, never()).toCategoryDTO(any());
    }

    @Test
    void testGetCategoryById() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        // when
        CategoryDTO result = categoryService.getCategoryById(1L);

        //then
        assertNotNull(result);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getName(), "Category 1");
        assertEquals(result.getDescription(), "Description 1");

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void testGetCategoryById_throwNotFoundException_whenCategoryNotFound() {
        // given
        when(categoryRepository.findById(404L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryById(404L);
        });

        verify(categoryRepository).findById(404L);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void testGetAllCategories() {
        // given
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Description 2");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(category2.getId());
        categoryDTO2.setName(category2.getName());
        categoryDTO2.setDescription(category2.getDescription());

        List<Category> categories = Arrays.asList(category, category2);
        List<CategoryDTO> expectedCategoryDTOs = Arrays.asList(categoryDTO, categoryDTO2);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toCategoryDTOs(categories)).thenReturn(expectedCategoryDTOs);

        // when
        List<CategoryDTO> result = categoryService.getAllCategories();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedCategoryDTOs, result);

        verify(categoryRepository).findAll();
        verify(categoryMapper).toCategoryDTOs(categories);
    }

}
