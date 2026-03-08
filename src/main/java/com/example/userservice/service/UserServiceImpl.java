package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository=userRepository;
    }
   /* Entity in Service X (not recommended)
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return  userRepository.findById(id).orElseThrow(()-> new RuntimeException("User id not found!"));
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }*/


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = UserMapper.toEntity(userRequest);
        User userCreated = userRepository.save(user);
        return UserMapper.toResponse(userCreated);
    }

    @Override
    public List<UserResponse> createUsers(List<UserRequest> userRequests) {
        List<User> users = userRequests.stream()
                .map(UserMapper:: toEntity).toList();
        List<User> usersCreated = userRepository.saveAll(users);
        return usersCreated.stream().map(UserMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllUsers() {
          return userRepository.findAll()
                  .stream()
                  .map(UserMapper::toResponse)
                  .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name);
        return UserMapper.toResponse(user);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(Long id) {
         userRepository.deleteById(id);
    }
}
