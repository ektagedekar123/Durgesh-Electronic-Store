package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.*;
import com.lcwd.electronicstore.services.CategoryService;
import com.lcwd.electronicstore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;


    /**
     * @author Ekta
     * @apiNote This method is for creating category
     * @param categoryDto
     * @return CategoryDto
     */


    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        logger.info("Entering request for creating category");
        CategoryDto dto = categoryService.createCategory(categoryDto);
        logger.info("Completed request for creating category");
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    /**
     * @author Ekta
     * @apiNote This method is for updating category
     * @param dto
     * @param categoryId
     * @return CategoryDto
     */
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto dto, @PathVariable String categoryId){
        logger.info("Entering request for updating category with category Id: {}",categoryId);
        CategoryDto categoryDto = categoryService.updateCategory(dto, categoryId);
        logger.info("Completed request for updating category with category Id: {}",categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }


    /**
     * @author Ekta
     * @apiNote This mehtod is for deleting Category
     * @param categoryId
     * @return ApiResponse
     */
    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("catId") String categoryId){
        logger.info("Entering request for deleting category with category Id: {}",categoryId);
        categoryService.deleteCategory(categoryId);
        ApiResponse apiResponse = ApiResponse.builder().message(AppConstants.CATEGORY_DELETE).status(HttpStatus.OK).success(true).build();
        logger.info("Completed request for deleting category with category Id: {}",categoryId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote This method is for getting single category
     * @param categoryId
     * @return CategoryDto
     */
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        logger.info("Entering request to get single category with category Id: {}",categoryId);
        CategoryDto dto = categoryService.getCategory(categoryId);
        logger.info("Completed request to get single category with category Id: {}",categoryId);
        return  ResponseEntity.ok(dto);
    }

    /**
     * @author Ekta
     * @apiNote This methos is for getting all categories with pagination
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageableResponse<CategoryDto>
     */
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


    /**
     * @author Ekta
     * @apiNote This method is for searching category with keywords
     * @param keywords
     * @return List<CategoryDto>
     */
    @GetMapping("/categories/search/{keywords}")
    public ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keywords){
      logger.info("Entering request for searching category with keywords: {}", keywords);
     return new ResponseEntity<>(this. categoryService.searchCategory(keywords), HttpStatus.OK);
    }



    @PostMapping("/categories/image/upload/{categoryId}")
    public ResponseEntity<ImageResponse> uploadFile(@RequestParam("categoryImage") MultipartFile image,
                                                    @PathVariable String categoryId) throws IOException {

        logger.info("Entering request for Uploading image with Category id: {}", categoryId);
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        String imagename = fileService.uploadFile(image, imageUploadPath);


        categoryDto.setCoverImage(imagename);
        categoryService.updateCategory(categoryDto, categoryId);
        logger.info("Image saved in DB");
        ImageResponse response = ImageResponse.builder().imagename(imagename).status(HttpStatus.CREATED).success(true).message(AppConstants.FILE_UPLOADED).build();
        logger.info("Completed request for Uploading image with Category id: {}", categoryId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/categories/image/serve/{categoryId}")
    public void serveImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        logger.info("Entering request for downloading image with categoryId: {}", categoryId);
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        String coverImage = categoryDto.getCoverImage();

        InputStream inputStream = fileService.getResource(imageUploadPath, coverImage);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
        logger.info("Completed request for downloading image with categoryId: {}", categoryId);
    }
}
