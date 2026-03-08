package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> createUsers(List<UserRequest> userRequests);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
    UserResponse getUserByName(String name);

    void deleteAllUsers();

    void deleteUserById(Long id);

}
