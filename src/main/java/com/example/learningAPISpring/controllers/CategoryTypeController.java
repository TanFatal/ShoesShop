package com.example.learningAPISpring.controllers;

import com.example.learningAPISpring.dto.DTO.CategoryTypedDTO;
import com.example.learningAPISpring.entity.CategoryType;
import com.example.learningAPISpring.service.CategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoryType")
public class CategoryTypeController {
    @Autowired
    private CategoryTypeService categoryTypeService;

    @PostMapping
    public ResponseEntity<CategoryType> createCategoryType(@RequestBody CategoryTypedDTO dto) {
        CategoryType created = categoryTypeService.createCategoryType(dto);
        return ResponseEntity.ok(created);
    }
}

