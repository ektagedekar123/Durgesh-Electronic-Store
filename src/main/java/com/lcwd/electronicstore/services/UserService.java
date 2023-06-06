package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;

import java.util.List;

public interface UserService {

    // create user
    UserDto createUser(UserDto userDto);

    // update User
    UserDto updateUser(UserDto userDto, String userId);

    // delete User
    void deleteUser(String userId);

    // get all users
    PageableResponse<UserDto> getAllUser(int pageNo, int pageSize, String sortBy, String sortDir);

    // get single User by Id
    UserDto getUserById(String userId);

    // get User by email
    UserDto getUserByEmail(String email);

    // search User
    List<UserDto> searchUser(String keywords);


    // Other user specific futures
}
