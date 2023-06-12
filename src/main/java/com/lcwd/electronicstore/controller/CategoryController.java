package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){

        CategoryDto dto = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto dto, @PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.updateCategory(dto, categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("catId") String CategoryId){
        categoryService.deleteCategory(CategoryId);
        ApiResponse apiResponse = ApiResponse.builder().message("Category deleted successfully!!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String CategoryId){
        CategoryDto dto = categoryService.getCategory(CategoryId);
        return  ResponseEntity.ok(dto);
    }

    @GetMapping("/categories")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(@RequestParam(value= "pageNo", defaultValue= AppConstants.PAGE_NO) Integer pageNo,
                                                              @RequestParam(value = "pageSize", defaultValue= AppConstants.PAGE_SIZE) Integer pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue= AppConstants.SORT_BY) String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue= AppConstants.SORT_DIR) String sortDir){

        return new ResponseEntity<>(this.categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);

    }

    @GetMapping("/categories/search/{keywords}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keywords){
     return new ResponseEntity<>(this. categoryService.searchCategory(keywords), HttpStatus.OK);
    }
}
