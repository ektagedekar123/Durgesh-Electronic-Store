package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDto createProduct(ProductDto productDto) {

        String randomId = UUID.randomUUID().toString();
        productDto.setProductid(randomId);
        Product product = this.modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return this.modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());

        Product updatedProduct = this.productRepository.save(product);

        return this.modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        this.productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortDir, String sortBy) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<Product> productPage = this.productRepository.findAll(pageable);

        PageableResponse<ProductDto> response = PageHelper.getPageableResponse(productPage, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<Product> page = this.productRepository.findByLiveTrue(pageable);
       return PageHelper.getPageableResponse(page, ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, int pageNo, int pageSize, String sortDir, String sortBy) {
        Sort sort= sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page = this.productRepository.findByTitleContaining(pageable, title);
        return PageHelper.getPageableResponse(page, ProductDto.class);
    }
}
