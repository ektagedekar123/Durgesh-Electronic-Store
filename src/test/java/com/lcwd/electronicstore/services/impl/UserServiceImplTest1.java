package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest       // So, we can use @Autowired annotation on userService field
public class UserServiceImplTest1 {

    @MockBean
    private UserRepository userRepository;

//    @MockBean   // so that mock repository will be available for spring for injecting, so repository will be automatically autowired in UserService
//    private RoleRepository roleRepository;         // so we don't neeed to use @InjectMocks on userService field


//    @InjectMocks    // Becoz of using @MockBean for injecting, we don't need to use @InjectMocks
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;


    User user;
    //   Role role;

    String roleId;

    @BeforeEach
    public void init(){

        //     role= Role.builder().roleId("abc").roleName("NORMAL").build();

        user= User.builder().name("Ekta")
                .email("gedekarEkta123@gmail.com")
                .about("This is Testing method")
                .imagename("ekta.png")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

          roleId="abc";
    }


    // create user Test case
    @Test
    public void createUserTest(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

//        Mockito.when(roleRepository.findById(Mockito.anyString()).thenReturn(Optional.of(role));

       UserDto dto= userService.createUser(mapper.map(user, UserDto.class));
       System.out.println(dto.getName());

       Assertions.assertNotNull(dto);
       Assertions.assertEquals("Ekta", dto.getName());

    }

    // update user test case
    @Test
    public void updateUserTest(){

        String userId="rtyu67889";

        UserDto userDto= UserDto.builder()
                .name("Ekta Gedekar")
                .about("This is updating user")
                .gender("Female")
                .imagename("eku.jpg").build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);
        System.out.println(updatedUser.getName());

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(userDto.getImagename(), updatedUser.getImagename());
//        Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.updateUser(userDto, userId));  // Not thrown exception
                                                                                                   // becoz we have used findById(Mockito.anyString()))
    }

    // delete user Test case
    @Test
    public void deleteUserTest(){

        String userId= "yuio678";

        Mockito.when(userRepository.findById("yuio678")).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.deleteUser("1"));
    }


    // get all users Test case
    @Test
    public void getAllUserTest(){

       User user1= User.builder().name("Shweta")
                .email("sh4563@gmail.com")
                .about("This is Testing method")
                .imagename("shweta.png")
                .password("sh56")
                .gender("female")
//             .roles(Set.of(role))
                .build();

       User user2= User.builder().name("Raju")
                .email("raju6788@gmail.com")
                .about("This is Testing method")
                .imagename("raju.png")
                .password("raju78")
                .gender("male")
//             .roles(Set.of(role))
                .build();

        List<User> userList = Arrays.asList(user, user1, user2);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable)Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> allUser = userService.getAllUser(1, 2, "name", "asc");

        Assertions.assertEquals(3, allUser.getContent().size());

    }

    @Test
    public void getUserByIdTest(){

        String userId="uio7";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName(), "Name not matched");
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.getUserById("1"));

    }

    @Test
    public void getUserByEmailTest(){

        String emailId="gedekar@gmai.com";

        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail(emailId);
        System.out.println(userDto.getEmail());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.getUserByEmail("raju@gmail.com"));
    }

    @Test
    public void searchUser(){

        User user2= User.builder().name("Shweta")
                .email("raju6788@gmail.com")
                .about("This is Testing method")
                .imagename("raju.png")
                .password("raju78")
                .gender("male")
//             .roles(Set.of(role))
                .build();

        User user3= User.builder().name("Prajakta")
                .email("raju6788@gmail.com")
                .about("This is Testing method")
                .imagename("raju.png")
                .password("raju78")
                .gender("male")
//             .roles(Set.of(role))
                .build();

        String keyword= "ta";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(Arrays.asList(user, user2, user3));

        List<UserDto> userDtoList = userService.searchUser(keyword);

        Assertions.assertEquals(3, userDtoList.size());
    }


/*    @Test
    public void findUserByEmailOptionalTest(){

        String emailId="gedekar@gmail.com";

        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));

        Optional<User> userOptional=userService.findUserByEmailOptional(emailId);

        Assertions.assertTrue(userOptional.isPresent());
        User user1 = userOptional.get();
        Assertions.assertEquals(user.getEmail(), user1.getEmail(), "Email does not matched");
    }  */
}
