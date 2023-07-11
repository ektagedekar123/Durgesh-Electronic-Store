package com.lcwd.electronicstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.payloads.CategoryDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.services.CategoryService;
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
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private Category category;

    private ProductDto productDto;

    private String categoryId;

    @BeforeEach
    public void init(){

        category = Category.builder()
                .title("Mobiles")
                .description("These are available")
                .coverImage("abc.pang").build();

        categoryId= "yuiu567";

        productDto= ProductDto.builder()
                .title("Nokia X3")
                .description("This is old version")
                .price(15000)
                .discountedPrice(2000)
                .quantity(10)
                .live(true)
                .stock(true)
                .productImage("x3.png")
                .category(mapper.map(category, CategoryDto.class))
                .build();


    }

    @Test
    public void createCategory() throws Exception {

        CategoryDto categoryDto = this.mapper.map(category, CategoryDto.class);
        categoryDto.setIsActive("Yes");
        categoryDto.setCreatedBy("Ekta");
        categoryDto.setLastModifiedBy("Ekta");

        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(categoryDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categoryapi/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(categoryDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    private String convertObjectToJsonString(Object category){

        try {
            return new ObjectMapper().writeValueAsString(category);
        }catch(Exception e){
            e.printStackTrace();
            return null;

        }
    }

    @Test
    public void updateCategoryTest() throws Exception {
        CategoryDto categoryDto = this.mapper.map(category, CategoryDto.class);
        categoryDto.setIsActive("Yes");
        categoryDto.setCreatedBy("Ekta");
        categoryDto.setLastModifiedBy("Ekta");

        Mockito.when(categoryService.updateCategory(Mockito.any(), Mockito.anyString())).thenReturn(categoryDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/categoryapi/categories/"+categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(categoryDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void deleteCategoryTest() throws Exception {

        Mockito.doNothing().when(categoryService);
        categoryService.deleteCategory(Mockito.anyString());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/categoryapi/categories/"+categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());


    }

    @Test
    public void getCategoryTest() throws Exception {

        Mockito.when(categoryService.getCategory(Mockito.anyString())).thenReturn(this.mapper.map(category, CategoryDto.class));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categoryapi/categories/"+categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    public void getAllCategoriesTest() throws Exception {

        CategoryDto dto = this.mapper.map(category, CategoryDto.class);

        CategoryDto dto1 = CategoryDto.builder()
                .title("Machines")
                .description("These are available")
                .coverImage("abc.pang").build();

        CategoryDto dto2 = CategoryDto.builder()
                .title("Bulbs")
                .description("These are available")
                .coverImage("abc.pang").build();

        List<Object> dtoList = Arrays.asList(dto, dto1, dto2);

        PageableResponse pageableResponse= PageableResponse.builder()
                .content(dtoList)
                .pageNo(10)
                .pageSize(8)
                .totalElements(3)
                .totalPages(100)
                .lastPage(true)
                .build();


        Mockito.when(categoryService.getAllCategories(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categoryapi/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void searchCategoryTest() throws Exception {

        CategoryDto dto = this.mapper.map(category, CategoryDto.class);

        CategoryDto dto1 = CategoryDto.builder()
                .title("Machines")
                .description("These are available")
                .coverImage("abc.pang").build();

        List<CategoryDto> dtoList = Arrays.asList(dto, dto1);

        String keyword= "m";

        Mockito.when(categoryService.searchCategory(Mockito.anyString())).thenReturn(dtoList);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categoryapi/categories/search/"+keyword)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void uploadFile() {
    }

    @Test
    void serveImage() {
    }

    @Test
    public void createProductWithCategoryIdTest() throws Exception {

        productDto.setCreatedBy("Ekta");
        productDto.setLastModifiedBy("Ekta");
        productDto.setIsActive("Yes");
        Mockito.when(productService.createProductWithCategory(Mockito.any(), Mockito.anyString())).thenReturn(productDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categoryapi/categories/"+categoryId+"/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(productDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").exists());

    }

    @Test
    public void updateCategoryOfProductTest() throws Exception {

        String productId="678gbn";

        Mockito.when(productService.updateProductWithCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(productDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/categoryapi/categories/"+categoryId+"/products/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    public void getProductsByCategoryTest() throws Exception {

        CategoryDto dto = this.mapper.map(category, CategoryDto.class);

        CategoryDto dto1 = CategoryDto.builder()
                .title("Machines")
                .description("These are available")
                .coverImage("abc.pang").build();

        CategoryDto dto2 = CategoryDto.builder()
                .title("Bulbs")
                .description("These are available")
                .coverImage("abc.pang").build();

        List<Object> dtoList = Arrays.asList(dto, dto1, dto2);

        PageableResponse pageableResponse= PageableResponse.builder()
                .content(dtoList)
                .pageNo(10)
                .pageSize(8)
                .totalElements(3)
                .totalPages(100)
                .lastPage(true)
                .build();

        Mockito.when(productService.getProductsByCategoryId(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString()))
                .thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categoryapi/categories/"+categoryId+"/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}