package com.lcwd.electronicstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import com.lcwd.electronicstore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest    // It loads context of Spring Boot automatically so that we can Autowired any component, but we can load specific context also by other way
@AutoConfigureMockMvc   // It will configure MockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;


    private User user;
//   private Role role;

    @BeforeEach
    public void init(){

        //     role= Role.builder().roleId("abc").roleName("NORMAL").build();

        user= User.builder().name("Ekta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();


    }

    @Test
   public void createUserTest() throws Exception {

        UserDto dto = mapper.map(user, UserDto.class);
        dto.setCreatedBy("Ekta");
        dto.setLastModifiedBy("Ekta");
        dto.setIsActive("yes");
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);


        //Actual Request for url

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }

    // Converting User object to Json i.e. String form, becoz Json data is always in String form
    private String convertObjectToJsonString(Object user){

        try{
            return new ObjectMapper().writeValueAsString(user);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateUserTest() throws Exception {

        String userId= "3456ggh";
        UserDto dto = this.mapper.map(user, UserDto.class);

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/users/"+userId)
          //              .headers(HttpHeaders.AUTHORIZATION,"Bearer copypaste token here from postman")   // Becoz this is Private api after applying Security, so we have to pass token
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());


    }

    @Test
    public void deleteUserTest() throws Exception {

        String userId="788yui";

        Mockito.doNothing().when(userService);
        userService.deleteUser(Mockito.anyString());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/"+userId)
                        // .headers(HttpHeaders.AUTHORIZATION,"Bearer copypaste token here from postman")   // Becoz this is Private api after applying Security, so we have to pass token
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
   public void getUserTest() throws Exception {

        UserDto dto = this.mapper.map(user, UserDto.class);

        String userId="78yujmm";

        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getAllUsersTest() throws Exception {

       UserDto dto1= UserDto.builder().name("Ekta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto2= UserDto.builder().name("Shweta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto3= UserDto.builder().name("Prajakta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto4= UserDto.builder().name("Raju")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(dto1, dto2, dto3, dto4));
        pageableResponse.setPageNo(100);
        pageableResponse.setPageSize(100);
        pageableResponse.setTotalPages(10);
        pageableResponse.setTotalElements(1000);
        pageableResponse.setLastPage(true);

        Mockito.when(userService.getAllUser(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByEmailTest() throws Exception {

        UserDto dto = this.mapper.map(user, UserDto.class);

        String email="gedeka123@gmail.com";
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/email/"+email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());


    }

    @Test
    public void searchUsersTest() throws Exception {

        UserDto dto1= UserDto.builder().name("Ekta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto2= UserDto.builder().name("Shweta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto3= UserDto.builder().name("Prajakta")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        UserDto dto4= UserDto.builder().name("Ankita")
                .email("gedekarkta123@gmail.com")
                .about("This is Testing method")
                .imagename("eku.jpeg")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        String keyword="ta";
        Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(Arrays.asList(dto1, dto2, dto3, dto4));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search/"+keyword)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void uploadImageTest() {
    }

    @Test
    void serveImageTest() {
    }
}