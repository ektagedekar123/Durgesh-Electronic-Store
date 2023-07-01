package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

    static UserDto dto;

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

        dto= UserDto.builder().userid("1")
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



    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void searchUser() {
    }

}
