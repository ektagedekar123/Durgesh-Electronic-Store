package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.repositories.CategoryRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.services.ProductService;
import com.lcwd.electronicstore.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductServiceImplTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    private Product product;

    private Category category;

    @BeforeEach
    public void init(){

        product= Product.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        category = Category.builder()
                .title("Mobiles")
                .description("These are available")
                .coverImage("abc.pang").build();

    }



    @Test
   public void createProductTest() {

       Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto productDto = productService.createProduct(this.mapper.map(product, ProductDto.class));
        System.out.println(productDto.getTitle());

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals("Nokia X3", productDto.getTitle());

    }

    @Test
    public void updateProductTest() {

        ProductDto productDto= ProductDto.builder()
                .title("Nokia X2")
                .description("This is also old version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("x2.png")
                .build();

        String productId="hjjkk789";

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto updateProduct = productService.updateProduct(productDto, productId);
        System.out.println(updateProduct.getTitle());

        Assertions.assertNotNull(updateProduct);
        Assertions.assertEquals(productDto.getTitle(), updateProduct.getTitle());
        Assertions.assertThrows(ResourceNotFoundException.class, ()->productService.updateProduct(productDto, "2"));


    }

    @Test
    public void deleteProductTest() {

        String productId="yui6778";

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.deleteProduct("1"));
    }

    @Test
   public void getProductTest() {

        String productId="yui6778";

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProduct(productId);
        System.out.println(productDto.getTitle());

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getPrice(), productDto.getPrice(), "Price Not Matched");
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.getProduct("1"));

    }

    @Test
    public void getAllProductsTest() {

        Product product1= Product.builder()
                .title("Nokia X2")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

       Product product2= Product.builder()
                .title("Nokia X4")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        List<Product> productList = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(productList);
        Mockito.when(productRepository.findAll((Pageable)Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> pageableResponse = productService.getAllProducts(1,3,"title", "asc");

 //       Assertions.assertEquals(3, pageableResponse.getContent().size());    // Same as below line
        Assertions.assertEquals(3, pageableResponse.getPageSize());


    }

    @Test
    public void getAllLiveProductsTest() {

        Product product1= Product.builder()
                .title("Nokia X2")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        Product product2= Product.builder()
                .title("Nokia X4")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        List<Product> productList = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(productList);

        Mockito.when(productRepository.findByLiveTrue((Pageable)Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> pageableResponse = productService.getAllLiveProducts(1, 3, "title", "desc");

        Assertions.assertEquals(3, pageableResponse.getPageSize());
        Assertions.assertEquals(1, pageableResponse.getPageNo());


    }

    @Test
    public void searchByTitleTest() {

        Product product1= Product.builder()
                .title("Nokia X2")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        Product product2= Product.builder()
                .title("Nokia X4")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        String title="kia";

        List<Product> productList = Arrays.asList(product, product1, product2);
        Page<Product> page=new PageImpl<>(productList);

        Mockito.when(productRepository.findByTitleContaining(((Pageable)Mockito.any()), Mockito.anyString())).thenReturn(page);

        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(title, 1, 3, "title", "desc");

        Assertions.assertEquals(1, pageableResponse.getTotalPages());
        Assertions.assertEquals(page.getContent().size(), pageableResponse.getContent().size());


    }

    @Test
    public void createProductWithCategoryTest() {

        String categoryId="56";

        Product product1= Product.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(category)
                .build();


        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product1);

        ProductDto dto = this.mapper.map(product, ProductDto.class);
        ProductDto productDto = productService.createProductWithCategory(dto, categoryId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(category.getTitle(), productDto.getCategory().getTitle());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.createProductWithCategory(dto,"55"));


    }

    @Test
    public void updateProductWithCategoryTest() {

        Product product1= Product.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(category)
                .build();

        String productId= "uio67";
        String categoryId= "dfgg34";

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product1);

        ProductDto productDto = productService.updateProductWithCategory(productId, categoryId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(category.getTitle(), productDto.getCategory().getTitle());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.updateProductWithCategory("67",categoryId));
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.updateProductWithCategory(productId,"55"));

    }

    @Test
    public void getProductsByCategoryId() {

        Product product1= Product.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(category)
                .build();

        Product product2= Product.builder()
                .title("Nokia X2")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(category)
                .build();

        Product product3= Product.builder()
                .title("Nokia X4")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(category)
                .build();

        String categoryId= "gjhjjku";

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<Product> productList= Arrays.asList(product1, product2, product3);
        Page<Product> page=new PageImpl<>(productList);

        Mockito.when(productRepository.findByCategory(Mockito.any(), (Pageable)Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> pageableResponse = productService.getProductsByCategoryId(categoryId, 1, 3, "title", "asc");

        Assertions.assertEquals(page.getSize(), pageableResponse.getPageSize());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> productService.getProductsByCategoryId("yui78",1,3,"title","asc"));

    }
}