package com.example.userservice.service;

import com.example.userservice.dto.UserPatchRequest;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> createUsers(List<UserRequest> userRequests);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
    UserResponse getUserByName(String name);

    void deleteAllUsers();

    void deleteUserById(Long id);

    //UserResponse patchUser(Long id, Map<String, Object> updates);
    UserResponse updateUser(Long id,UserRequest userRequest);

    UserResponse patchUser(Long id, UserPatchRequest userPatchRequest);
}
