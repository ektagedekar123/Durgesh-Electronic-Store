package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String productId);

    ProductDto getProduct(String productId);

    PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String title, int pageNo, int pageSize, String sortBy, String sortDir);

    // create Product by categoryId
    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

    // Assigning category to Product
    ProductDto updateProductWithCategory(String productId, String categoryId);

    // Fetch all products by CategoryId
    PageableResponse<ProductDto> getProductsByCategoryId(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
}
