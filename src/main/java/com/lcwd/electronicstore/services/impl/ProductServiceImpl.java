package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.services.ProductService;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String imagePath;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Initiating dao layer to create product");
        String randomId = UUID.randomUUID().toString();
        productDto.setProductid(randomId);
        Product product = this.modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        log.info("Completed dao layer to create product");
        return this.modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        log.info("Initiating dao layer to update product with product id: {}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setProductImage(productDto.getProductImage());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setCreatedBy(productDto.getCreatedBy());
        product.setLastModifiedBy(productDto.getLastModifiedBy());
        product.setIsActive(productDto.getIsActive());

        Product updatedProduct = this.productRepository.save(product);
        log.info("Completed dao layer to update product with product id: {}",productId);
        return this.modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        log.info("Initiating dao layer to delete product with product id: {}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        String fullPath= imagePath + product.getProductImage();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            log.info("product image is not found in folder");
            ex.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        log.info("Product image is deleted from folder");
        this.productRepository.delete(product);
        log.info("Completed dao layer to delete product with product id: {}",productId);
    }

    @Override
    public ProductDto getProduct(String productId) {
        log.info("Initiating dao layer to get single product with product id: {}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PRODUCT_NOT_FOUND + productId));
        log.info("Completed dao layer to get single product with product id: {}",productId);
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Initiating dao layer to get all products with pagination");
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<Product> productPage = this.productRepository.findAll(pageable);

        PageableResponse<ProductDto> response = PageHelper.getPageableResponse(productPage, ProductDto.class);
        log.info("Completed dao layer to get all products with pagination");
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Initiating dao layer to get all live products with pagination");
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<Product> page = this.productRepository.findByLiveTrue(pageable);
        log.info("Completed dao layer to get all live products with pagination");
       return PageHelper.getPageableResponse(page, ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Initiating dao layer to search all products by title with keywords: {}",title);
        Sort sort= sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<Product> page = this.productRepository.findByTitleContaining(pageable, title);
        log.info("Completed dao layer to search all products by title with keywords: {}",title);
        return PageHelper.getPageableResponse(page, ProductDto.class);
    }
}
