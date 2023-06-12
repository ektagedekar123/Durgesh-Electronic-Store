package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;

import java.util.List;

public interface CategoryServiceI {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);


    void deleteCategory(String categoryId);

    CategoryDto getCategory(String categoryId);

    PageableResponse<CategoryDto> getAllCategories();

    List<CategoryDto> searchCategory(String keywords);
}
