package org.example.galacticmarket.service;

import org.example.galacticmarket.dto.CategoryDTO;
import org.example.galacticmarket.mappers.CategoryMapper;
import org.example.galacticmarket.mappers.CategoryMapperImpl;
import org.example.galacticmarket.repository.CategoryRepository;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = new CategoryMapperImpl();
        categoryService = new CategoryService(categoryRepository, categoryMapper);
    }

    @Test
    void testGetAllCategories() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.randomUUID());
        categoryEntity.setName("Dairy");

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(categoryEntity));

        List<CategoryDTO> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Dairy", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_existingCategory() {
        UUID existingCategoryId = UUID.randomUUID();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(existingCategoryId);
        categoryEntity.setName("Dairy");

        when(categoryRepository.findById(existingCategoryId)).thenReturn(Optional.of(categoryEntity));

        CategoryDTO categoryDTO = categoryService.getCategoryById(existingCategoryId);

        assertNotNull(categoryDTO);
        assertEquals("Dairy", categoryDTO.getName());
        verify(categoryRepository, times(1)).findById(existingCategoryId);
    }

    @Test
    void testGetCategoryById_nonExistentCategory() {
        UUID nonExistentId = UUID.randomUUID();

        when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.getCategoryById(nonExistentId));
        assertEquals("Category not found with id: " + nonExistentId, exception.getMessage());
        verify(categoryRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testCreateCategory() {
        CategoryDTO newCategory = CategoryDTO.builder()
                .name("Beverages")
                .build();

        CategoryEntity categoryEntity = categoryMapper.toEntity(newCategory);
        categoryEntity.setId(UUID.randomUUID());

        when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity);

        CategoryDTO createdCategory = categoryService.createCategory(newCategory);

        assertNotNull(createdCategory);
        assertNotNull(createdCategory.getId());
        assertEquals(newCategory.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(Mockito.any(CategoryEntity.class));
    }

    @Test
    void testUpdateCategory_existingCategory() {
        UUID existingCategoryId = UUID.randomUUID();
        CategoryEntity existingEntity = new CategoryEntity();
        existingEntity.setId(existingCategoryId);
        existingEntity.setName("Dairy");

        CategoryDTO updatedCategory = CategoryDTO.builder()
                .name("Frozen Foods")
                .build();

        when(categoryRepository.findById(existingCategoryId)).thenReturn(Optional.of(existingEntity));
        when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(existingEntity);

        CategoryDTO result = categoryService.updateCategory(existingCategoryId, updatedCategory);

        assertNotNull(result);
        assertEquals(updatedCategory.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(existingCategoryId);
        verify(categoryRepository, times(1)).save(Mockito.any(CategoryEntity.class));
    }

    @Test
    void testDeleteCategory_existingCategory() {
        UUID existingCategoryId = UUID.randomUUID();

        when(categoryRepository.existsById(existingCategoryId)).thenReturn(true);

        assertDoesNotThrow(() -> categoryService.deleteCategory(existingCategoryId));
        verify(categoryRepository, times(1)).existsById(existingCategoryId);
        verify(categoryRepository, times(1)).deleteById(existingCategoryId);
    }

    @Test
    void testDeleteCategory_nonExistentCategory() {
        UUID nonExistentId = UUID.randomUUID();

        when(categoryRepository.existsById(nonExistentId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.deleteCategory(nonExistentId));
        assertEquals("Category not found with id: " + nonExistentId, exception.getMessage());
        verify(categoryRepository, times(1)).existsById(nonExistentId);
        verify(categoryRepository, never()).deleteById(nonExistentId);
    }
}