package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String productId);

    ProductDto getProduct(String productId);

    PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortDir, String sortBy);

    PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortDir, String sortBy);

    PageableResponse<ProductDto> searchByTitle(String title, int pageNo, int pageSize, String sortDir, String sortBy);
}
