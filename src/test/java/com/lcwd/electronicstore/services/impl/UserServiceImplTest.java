package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    //   @Mock
    //   @Autowired
    //   private UserService userService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    static User user;
    static User user1;

    static UserDto dto;
    static UserDto dto1;

    @BeforeAll
    public static void init(){

        user= User.builder().userid("1")
                .email("gedekar@gmail.com")
                .name("Ekta")
                .gender("female")
                .password("eku")
                .imagename("ek1.png")
                .about("I am a Engineer")
                .build();

         user1= User.builder().userid("1")
                .email("gedekar@gmail.com")
                .name("Ekta")
                .gender("female")
                .password("eku")
                .imagename("ek1.png")
                .about("I am a Engineer")
                .build();


        dto= UserDto.builder().userid("1")
                .email("gedekar@gmail.com")
                .name("Ekta")
                .gender("female")
                .password("eku")
                .imagename("ek1.png")
                .about("I am a Engineer")
                .build();

         dto1= UserDto.builder().userid("1")
                .email("gedekar@gmail.com")
                .name("Ekta")
                .gender("female")
                .password("eku")
                .imagename("ek1.png")
                .about("I am a Engineer")
                .build();



    }


    @Test
    void createUser() {

        when(mapper.map(dto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.map(user, UserDto.class)).thenReturn(dto);

        String name = userServiceImpl.createUser(dto).getName();

        assertEquals("Ekta", name);

    }

    @Test
    void updateUser() {

        String userId="1";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.map(user, UserDto.class)).thenReturn(dto);


        String actualId = userServiceImpl.updateUser(dto, userId).getUserid();

        assertEquals(userId, actualId);
        assertThrows(ResourceNotFoundException.class, ()->userServiceImpl.updateUser(dto, "2"));


    }

    @Test
    void deleteUser() {

        String userId="1";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUser(userId);

        verify(userRepository, times(1)).delete(user);
        assertThrows(ResourceNotFoundException.class, ()-> userServiceImpl.deleteUser("2"));
    }

    @Test
    void getAllUser() {

        List<User> userList=List.of(user, user1);

       Page<User> page = new PageImpl<>(userList);

        when(userRepository.findAll((Pageable)Mockito.any())).thenReturn(page);

  //     when(userList.stream().map(u-> mapper.map(u, UserDto.class)).collect(Collectors.toList())).thenReturn(userDtoList);

        PageableResponse<UserDto> allUser = userServiceImpl.getAllUser(1,2, "email", "desc");

        assertEquals(2,allUser.getPageSize());
        assertEquals(2, allUser.getContent().size());
    }

    @Test
    void getUserById() {

        String userId="1";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDto.class)).thenReturn(dto);

        UserDto userDto = userServiceImpl.getUserById(userId);

        assertNotNull(userDto);
        assertEquals(userId, userDto.getUserid());
        assertThrows(ResourceNotFoundException.class, ()-> userServiceImpl.getUserById("2"));
    }

    @Test
    void getUserByEmail() {

        String email="gedekar@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDto.class)).thenReturn(dto);

        UserDto userDto = userServiceImpl.getUserByEmail(email);

        assertNotNull(userDto);
        assertEquals(userDto.getEmail(), email);
        assertThrows(ResourceNotFoundException.class, ()-> userServiceImpl.getUserByEmail("ty65@yahoo.in"));
    }

    @Test
    void searchUser() {

        List<User> userList=List.of(user, user1);
        List<UserDto> userDtoList=List.of(dto, dto1);

        String keywords="ek";
        when(userRepository.findByNameContaining(keywords)).thenReturn(userList);
 //       when(mapper.map(user, UserDto.class)).thenReturn(dto); // whether we write this statement or not, still test case run successfully

        List<UserDto> dtos = userServiceImpl.searchUser(keywords);

        assertNotNull(dtos);
        assertEquals(dtos.size(), 2);
    }

}
