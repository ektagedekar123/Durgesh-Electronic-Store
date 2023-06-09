package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.ImageResponse;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.services.FileService;
import com.lcwd.electronicstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    /**
     * @author Ekta
     * @apiNote This method is for creating user
     * @param userDto
     * @return UserDto
     */
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        logger.info("Entering request for creating user");
        UserDto dto = this.userService.createUser(userDto);
        logger.info("Completed request for creating user");
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    /**
     * @author Ekta
     * @apiNote THis method is for updating user
     * @param uid
     * @param userdto
     * @return UserDto
     */
    @PutMapping("/users/{userid}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userid") String uid, @RequestBody UserDto userdto){
        logger.info("Entering request for updating user with user id: {}",uid);
        UserDto updatedUserDto = this.userService.updateUser(userdto, uid);
        logger.info("Completed request for updating user with user id: {}",uid);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote THis method is for deleting user
     * @param userid
     * @return ApiResponse
     */
    @DeleteMapping("/users/{userid}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userid){
        logger.info("Entering request for deleting user with user id: {}",userid);
        this.userService.deleteUser(userid);
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage(AppConstants.USER_DELETE);
        apiResponse.setSuccess(true);
        apiResponse.setStatus(HttpStatus.OK);
        logger.info("Completed request for deleting user with user id: {}",userid);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote  This method is for getting single user
     * @param userid
     * @return UserDto
     */
    @GetMapping("/users/{userid}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userid){
        logger.info("Entering request to get single user with user id: {}",userid);
        return new ResponseEntity<>(this.userService.getUserById(userid), HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote This method is for getting all users
     * @return List<UserDto
     */
    @GetMapping("/users")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue =  "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue =  "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ){
        logger.info("Entering request for getting all users");
        return new ResponseEntity<>(userService.getAllUser(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote This method is for getting user by email
     * @param email
     * @return UserDto
     */
    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        logger.info("Entering request for getting user by email: {}",email);
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    /**
     * @author Ekta
     * @apiNote This method is for searching user by keywords
     * @param keywords
     * @return List<UserDto
     */
    @GetMapping("/users/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUsers(@PathVariable String keywords){
        logger.info("Entering request for searching user with keywords: {}",keywords);
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }

    @PostMapping("/users/image/{userId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("userimage") MultipartFile image,
                                                     @PathVariable String userId) throws IOException {
        String imagename = fileService.uploadFile(image, imageUploadPath);

        UserDto userDto = userService.getUserById(userId);
        userDto.setImagename(imagename);
        UserDto updatedUser = userService.updateUser(userDto, userId);

        ImageResponse imageResponse=ImageResponse.builder().imagename(imagename).status(HttpStatus.CREATED).success(true).message("File uploaded successfully").build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

}
