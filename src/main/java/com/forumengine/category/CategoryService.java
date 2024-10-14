package com.forumengine.category;

import com.forumengine.category.dto.CategoryDTO;
import com.forumengine.category.dto.CreateCategoryDTO;
import com.forumengine.exception.EntityAlreadyExistsException;
import com.forumengine.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        Category category = categoryMapper.toCategory(createCategoryDTO);
        if (categoryRepository.existsByName(category.getName())) {
            throw new EntityAlreadyExistsException(category.getName());
        }

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDTO(savedCategory);
    }

    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new EntityNotFoundException(id.toString());
        }

        return categoryMapper.toCategoryDTO(category.get());
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categoryMapper.toCategoryDTOs(categories);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
