package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.repositories.CategoryRepository;
import com.lcwd.electronicstore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper mapper;



    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String randomId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(randomId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepo.save(category);
        return this.mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exceptions!!"));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepo.save(category);
        return  mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exceptions!!"));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exception!!"));
        return this.mapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = PageHelper.getPageableResponse(categoryPage, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public List<CategoryDto> searchCategory(String keywords) {
        List<Category> categories = categoryRepo.findByTitleContaining(keywords);
        List<CategoryDto> dtos = categories.stream().map(i -> mapper.map(i, CategoryDto.class)).collect(Collectors.toList());
        return dtos;
    }
}
