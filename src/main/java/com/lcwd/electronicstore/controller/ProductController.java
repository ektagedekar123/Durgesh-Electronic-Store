package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {

        ProductDto productDto = productService.createProduct(dto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto dto, @PathVariable String productId) {

        ProductDto productDto = productService.updateProduct(dto, productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {

        productService.deleteProduct(productId);
        ApiResponse apiResponse = ApiResponse.builder().message("Product deleted successfully!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {

        ProductDto productDto = productService.getProduct(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PageableResponse<ProductDto> pageableResponse = this.productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/products/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        PageableResponse<ProductDto> pageableResponse = this.productService.getAllLiveProducts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
    @GetMapping("/products/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(@PathVariable("subTitle") String title,
                                                                      @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
                                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PageableResponse<ProductDto> pageableResponse = this.productService.searchByTitle(title, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

}
