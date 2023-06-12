package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.repositories.CategoryRepository;
import com.lcwd.electronicstore.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper mapper;



    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Initiating dao layer to create category");
        String randomId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(randomId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepo.save(category);
        log.info("Completed dao layer to create category");
        return this.mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        log.info("Initiating dao layer for updating category with categoryId: {}", categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exceptions!!"));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepo.save(category);
        log.info("Completed dao layer for updating category with categoryId: {}", categoryId);
        return  mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("Initiating dao layer for deleting category with category id: {}",categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exceptions!!"));
        categoryRepo.delete(category);
        log.info("Completed dao layer for deleting category with category id: {}",categoryId);
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        log.info("Initiating dao layer to get single category with category id: {}",categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exception!!"));
        log.info("Completed dao layer to get single category with category id: {}",categoryId);
        return this.mapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Initiating dao layer to get all categories  with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",pageNo, pageSize, sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        log.info("Sort object is created with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",pageNo, pageSize, sortBy, sortDir);
        Pageable pageable= PageRequest.of(pageNo-1, pageSize, sort);

        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        log.info("Page object is created with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",pageNo, pageSize, sortBy, sortDir);
        PageableResponse<CategoryDto> pageableResponse = PageHelper.getPageableResponse(categoryPage, CategoryDto.class);
        log.info("Initiating dao layer to get all categories  with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",pageNo, pageSize, sortBy, sortDir);
        return pageableResponse;
    }

    @Override
    public List<CategoryDto> searchCategory(String keywords) {
        log.info("Initiating dao layer to search category with keywords: {}",keywords );
        List<Category> categories = categoryRepo.findByTitleContaining(keywords);
        List<CategoryDto> dtos = categories.stream().map(i -> mapper.map(i, CategoryDto.class)).collect(Collectors.toList());
        log.info("Completed dao layer to search category with keywords: {}",keywords );
        return dtos;
    }
}
