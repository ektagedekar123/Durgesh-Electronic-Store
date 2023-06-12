package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        logger.info("Entering request for creating category");
        CategoryDto dto = categoryService.createCategory(categoryDto);
        logger.info("Completed request for creating category");
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto dto, @PathVariable String categoryId){
        logger.info("Entering request for updating category with category Id: {}",categoryId);
        CategoryDto categoryDto = categoryService.updateCategory(dto, categoryId);
        logger.info("Completed request for updating category with category Id: {}",categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("catId") String categoryId){
        logger.info("Entering request for deleting category with category Id: {}",categoryId);
        categoryService.deleteCategory(categoryId);
        ApiResponse apiResponse = ApiResponse.builder().message(AppConstants.CATEGORY_DELETE).status(HttpStatus.OK).success(true).build();
        logger.info("Completed request for deleting category with category Id: {}",categoryId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        logger.info("Entering request to get single category with category Id: {}",categoryId);
        CategoryDto dto = categoryService.getCategory(categoryId);
        logger.info("Completed request to get single category with category Id: {}",categoryId);
        return  ResponseEntity.ok(dto);
    }

    @GetMapping("/categories")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue =  AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue =  AppConstants.SORT_ByTitle, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir

    ){
        logger.info("Entering request for getting all categories with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}",pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<PageableResponse<CategoryDto>>(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/categories/search/{keywords}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keywords){
      logger.info("Entering request for searching category with keywords: {}", keywords);
     return new ResponseEntity<>(this. categoryService.searchCategory(keywords), HttpStatus.OK);
    }
}
