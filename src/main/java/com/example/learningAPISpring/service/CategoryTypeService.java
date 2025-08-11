package com.example.learningAPISpring.service;

import com.example.learningAPISpring.dto.DTO.CategoryTypedDTO;
import com.example.learningAPISpring.entity.CategoryType;
import com.example.learningAPISpring.entity.Category;
import com.example.learningAPISpring.repository.CategoryTypeRepository;
import com.example.learningAPISpring.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryTypeService {
    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryType createCategoryType(CategoryTypedDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        CategoryType categoryType = CategoryType.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .category(category)
                .build();
        return categoryTypeRepository.save(categoryType);
    }
}

