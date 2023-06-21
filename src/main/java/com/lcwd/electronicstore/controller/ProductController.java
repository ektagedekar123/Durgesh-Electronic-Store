package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.*;
import com.lcwd.electronicstore.services.FileService;
import com.lcwd.electronicstore.services.ProductService;
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
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    private Logger logger= LoggerFactory.getLogger(ProductController.class);

    /**
     * @Author Ekta
     * @apiNote This method is for creating product
     * @param dto
     * @return ProductDto
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        logger.info("Entering request for creating product");
        ProductDto productDto = productService.createProduct(dto);
        logger.info("Completed request for creating product");
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    /**
     * @Author Ekta
     * @apiNote This method is for updating product
     * @param dto
     * @param productId
     * @return ProductDto
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto dto, @PathVariable String productId) {
        logger.info("Entering request for updating product with product id: {}",productId);
        ProductDto productDto = productService.updateProduct(dto, productId);
        logger.info("Completed request for updating product with product id: {}",productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * @Author Ekta
     * @apiNote This method is for deleting Product
     * @param productId
     * @return ApiResponse
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        logger.info("Enetring request for deleting product with product id: {}",productId);
        productService.deleteProduct(productId);
        ApiResponse apiResponse = ApiResponse.builder().message("Product deleted successfully!!").success(true).status(HttpStatus.OK).build();
        logger.info("Completed request for deleting product with product id: {}",productId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * @Author Ekta
     * @apiNote This method is for getting single Product
     * @param productId
     * @return ProductDto
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        logger.info("Entering request for getting single product with product id: {}",productId);
        ProductDto productDto = productService.getProduct(productId);
        logger.info("Completed request for getting single product with product id: {}",productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * @Author Ekta
     * @apiNote This methos is for getting all products with pagination
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageableResponse<ProductDto>
     */
    @GetMapping("/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        logger.info("Entering request for getting all products");
        PageableResponse<ProductDto> pageableResponse = this.productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);
        logger.info("Completed request for getting all products");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @Author Ekta
     * @apiNote This method is for getting all live products with pagination
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageableResponse<ProductDto>
     */
    @GetMapping("/products/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        logger.info("Entering request for getting all live products");
        PageableResponse<ProductDto> pageableResponse = this.productService.getAllLiveProducts(pageNo, pageSize, sortBy, sortDir);
        logger.info("Completed request for getting all live products");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    /**
     * @Author Ekta
     * @apiNote This method is for searching all products with pagination
     * @param title
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return PageableResponse<ProductDto>
     */
    @GetMapping("/products/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(@PathVariable("subTitle") String title,
                                                                      @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
                                                                      @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_ByTitle, required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        logger.info("Entering request for searching products by title with keywords: {}",title);
        PageableResponse<ProductDto> pageableResponse = this.productService.searchByTitle(title, pageNo, pageSize, sortBy, sortDir);
        logger.info("Completed request for searching products by title with keywords: {}",title);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }



    @PostMapping("/products/image/upload/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId,
                                                     @RequestParam("productimage") MultipartFile image) throws IOException {
        logger.info("Entering request for Uploading image with Product id: {}", productId);
        String filename = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getProduct(productId);
        productDto.setProductImage(filename);
        ProductDto dto = productService.updateProduct(productDto, productId);
        logger.info("Image saved in DB");
        ImageResponse response = ImageResponse.builder().imagename(dto.getProductImage()).message(AppConstants.FILE_UPLOADED).status(HttpStatus.CREATED).success(true).build();
        logger.info("Completed request for Uploading image with Product id: {}", productId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/products/image/serve/{productId}")
    public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        logger.info("Entering request for downloading image with productId: {}", productId);
        ProductDto productDto = productService.getProduct(productId);
        String productImage = productDto.getProductImage();

        InputStream inputStream = fileService.getResource(imagePath, productImage);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
        logger.info("Completed request for downloading image with productId: {}", productId);
    }

}
