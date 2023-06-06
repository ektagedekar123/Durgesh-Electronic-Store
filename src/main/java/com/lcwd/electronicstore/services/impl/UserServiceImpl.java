package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    /**
     * @author Ekta
     * @implNote This method is for creating User
     * @param userDto
     * @return UserDto
     */
    @Override
    public UserDto createUser(UserDto userDto) {
         log.info("Initiating dao layer to create user");
        // generate unique id in String format
        String userId = UUID.randomUUID().toString();
        userDto.setUserid(userId);

        User user = this.modelMapper.map(userDto, User.class);

        User saveUser = userRepository.save(user);

 //       UserDto newDto = entityToDto(saveUser);
        UserDto newDto = this.modelMapper.map(saveUser, UserDto.class);
        log.info("Completed dao layer to create user");
        return newDto;
    }

    /**
     * @author Ekta
     * @apiNote This method is for updating user
     * @param userId
     * @param userDto
     * @return UserDto
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        log.info("Initiating dao layer for updating user with user id: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));

        user.setName(userDto.getName());
    //    user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
    //    user.setCreatedOn(LocalDateTime.now());
        user.setCreatedBy(userDto.getCreatedBy());
        user.setLastModifiedBy(userDto.getLastModifiedBy());
        user.setIsActive(userDto.getIsActive());
        user.setImagename(userDto.getImagename());

        User updatedUser = userRepository.save(user);

 //       UserDto updatedDto = entityToDto(updatedUser);
        log.info("Completed dao layer for updating user with user id: {}",userId);
        return  this.modelMapper.map(updatedUser, UserDto.class);
    }

    /**
     * @author Ekta
     * @implNote This method is for deleting user
     * @param userId
     * @return ApiResponse
     */
    @Override
    public void deleteUser(String userId) {
        log.info("Initiating dao layer for deleting user with user id: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));
        log.info("Completed dao layer for deleting user with user id: {}",userId);
        userRepository.delete(user);
    }

    /**
     * @author Ekta
     * @implNote This method is for getting all users
     * @return List<UserDto
     */
    @Override
    public PageableResponse<UserDto> getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Initiating dao layer for getting all users");
 //       Sort sort = Sort.by(sortBy);
        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);

    /*    List<User> users = page.getContent();
        //     List<UserDto> userDtos = allUser.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        List<UserDto> userDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

        PageableResponse response=new PageableResponse();
        response.setContent(userDtos);
        response.setPageNo(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        log.info("Completed dao layer for getting all users");  */

        PageableResponse<UserDto> pageableResponse = PageHelper.getPageableResponse(page, UserDto.class);
        return pageableResponse;
    }

    /**
     * @author Ekta
     * @implNote This method is for getting single user
     * @param userId
     * @return UserDto
     */
    @Override
    public UserDto getUserById(String userId) {
        log.info("Initiating dao layer for getting single user by userId: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));
 //       return entityToDto(user);
        log.info("Completed dao layer for getting single user by userId: {}",userId);
        return this.modelMapper.map(user, UserDto.class);
    }

    /**
     * @author Ekta
     * @implNote This method is for getting user by email
     * @param email
     * @return UserDto
     */
    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Initiating dao layer for getting user by email: {}",email);
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email id: "+email));
        log.info("Completed dao layer for getting user by email: {}",email);
        return this.modelMapper.map(user, UserDto.class);
    }

    /**
     * @author Ekta
     * @implNote This method is for searching user by keywords
     * @param keywords
     * @return List<UserDto
     */
    @Override
    public List<UserDto> searchUser(String keywords) {
        log.info("Initiating dao layer for searching user by keywords: {}",keywords);
        List<User> users = this.userRepository.findByNameContaining(keywords);
        List<UserDto> dtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed dao layer for searching user by keywords: {}",keywords);
        return dtos;
    }


    // Converting UserDto to User
    private User dtoToEntity(UserDto userDto){

       User user = User.builder()
               .userid(userDto.getUserid())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .imagename(userDto.getImagename()).build();

        return user;
    }

    // Converting User to UserDto
    private UserDto entityToDto(User user){

        UserDto userDto = UserDto.builder()
                .userid(user.getUserid())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .about(user.getAbout())
                .gender(user.getGender())
                .imagename(user.getImagename()).build();

        return userDto;
    }
}
