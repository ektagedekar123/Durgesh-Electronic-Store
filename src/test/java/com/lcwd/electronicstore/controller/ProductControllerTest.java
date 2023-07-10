package com.lcwd.electronicstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Product product;
    private ProductDto productDto1;
    private ProductDto productDto2;

    private String productId;

    @BeforeEach
    public void init(){

        product= Product.builder()
                .title("Nokia X4")
                .description("This is old version")
                .price(12000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x4.png")
                .build();

        productDto1= ProductDto.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(15000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .build();

        productDto2= ProductDto.builder()
                .title("Nokia X2")
                .description("This is old version")
                .price(20000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x2.png")
                .build();


        productId= "tyu78";

    }
    @Test
    public void createProductTest() throws Exception {

        ProductDto productDto = this.mapper.map(product, ProductDto.class);
        Mockito .when(productService.createProduct(Mockito.any())).thenReturn(productDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/productapi/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(converObjectToJsonString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }

    private String converObjectToJsonString(Object product){

        try{
           return new ObjectMapper().writeValueAsString(product);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Test
    public void updateProductTest() throws Exception {

       Mockito.when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(mapper.map(product, ProductDto.class));

       this.mockMvc.perform(MockMvcRequestBuilders.put("/productapi/products/"+productId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(converObjectToJsonString(product))
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").exists());

    }

    @Test
    public void deleteProductTest() throws Exception {

        Mockito.doNothing().when(productService);
        productService.deleteProduct(Mockito.anyString());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/productapi/products/"+productId)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getSingleProductTest() throws Exception {

        Mockito.when(productService.getProduct(Mockito.anyString())).thenReturn(mapper.map(product, ProductDto.class));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/productapi/products/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    public void getAllProductsTest() throws Exception {

        ProductDto productDto = this.mapper.map(product, ProductDto.class);
        List<Object> productDtoList = Arrays.asList(productDto, productDto1, productDto2);

       PageableResponse pageableResponse= PageableResponse.builder().content(productDtoList)
                .pageNo(10)
                .pageSize(5)
                .totalElements(3)
                .totalPages(100)
                .lastPage(false)
                .build();

        Mockito.when(productService.getAllProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/productapi/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllLiveProductsTest() throws Exception {

        ProductDto productDto = this.mapper.map(product, ProductDto.class);
        List<Object> productDtoList = Arrays.asList(productDto, productDto1, productDto2);

        PageableResponse pageableResponse= PageableResponse.builder().content(productDtoList)
                .pageNo(10)
                .pageSize(5)
                .totalElements(3)
                .totalPages(100)
                .lastPage(false)
                .build();

        Mockito.when(productService.getAllLiveProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/productapi/products/live")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }

    @Test
   public void searchProductsTest() throws Exception {

        String keyword="kia";

        ProductDto productDto = this.mapper.map(product, ProductDto.class);
        List<Object> productDtoList = Arrays.asList(productDto, productDto1, productDto2);

        PageableResponse pageableResponse= PageableResponse.builder().content(productDtoList)
                .pageNo(10)
                .pageSize(5)
                .totalElements(3)
                .totalPages(100)
                .lastPage(false)
                .build();

        Mockito.when(productService.searchByTitle(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/productapi/products/search/"+keyword)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void uploadImage() {
    }

    @Test
    void serveImage() {
    }
}