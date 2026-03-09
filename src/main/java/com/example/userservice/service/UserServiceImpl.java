package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.kafka.UserEventProducer;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserEventProducer userEventProducer;

    public UserServiceImpl(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = UserMapper.toEntity(userRequest);
        User userCreated = userRepository.save(user);
        UserResponse response = UserMapper.toResponse(userCreated);
        userEventProducer.sendUserCreatedEvent(response);
        return response;
    }

    @Override
    public List<UserResponse> createUsers(List<UserRequest> userRequests) {
        List<User> users = userRequests.stream()
                .map(UserMapper::toEntity)
                .toList();
        List<User> usersCreated = userRepository.saveAll(users);
        List<UserResponse> responses = usersCreated.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        responses.forEach(userEventProducer::sendUserCreatedEvent);
        return responses;
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
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return UserMapper.toResponse(user);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
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


}
