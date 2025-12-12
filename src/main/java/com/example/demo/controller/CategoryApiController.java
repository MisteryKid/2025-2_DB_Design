package com.example.demo.controller;


import com.example.demo.domain.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
    private final CategoryRepository categoryRepository;

    public CategoryApiController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 특정 부모 ID에 속하는 모든 하위 카테고리를 JSON으로 반환합니다.
     * URL: /api/categories/{parentId}
     */
    @GetMapping("/{parentId}")
    public List<Category> getSubcategories(@PathVariable Long parentId) {


        if (parentId == 0) {
            // ID가 0이거나 기타 조건일 경우 빈 목록 반환
            return List.of();
        }
        return categoryRepository.findByParent_Id(parentId);
    }

}
