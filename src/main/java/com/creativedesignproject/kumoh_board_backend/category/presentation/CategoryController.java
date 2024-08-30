package com.creativedesignproject.kumoh_board_backend.category.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creativedesignproject.kumoh_board_backend.category.dto.request.UpdateCategoryRequestDto;
import com.creativedesignproject.kumoh_board_backend.category.domain.entity.Category;
import com.creativedesignproject.kumoh_board_backend.category.dto.request.CategoryDto;
import com.creativedesignproject.kumoh_board_backend.category.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerCategory(@RequestBody Category categoryEntity) {
        categoryService.registerCategory(categoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update/{category_id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long category_id, @RequestBody @Valid UpdateCategoryRequestDto dto) {
        categoryService.updateCategory(category_id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{category_id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long category_id) {
        categoryService.deleteCategoryName(category_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categoryList")
    public ResponseEntity<List<CategoryDto>> getCategory() {
        List<CategoryDto> categoryDtoList = categoryService.getCategoryList();
        return ResponseEntity.ok().body(categoryDtoList);
    }
}
