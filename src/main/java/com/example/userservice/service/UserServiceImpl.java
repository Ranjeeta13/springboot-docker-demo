package com.example.userservice.service;

import com.example.userservice.dto.UserPatchRequest;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.kafka.UserEventProducer;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        // Transient
        User user = userMapper.toEntity(userRequest);

        // Persistent / Managed
        User savedUser = userRepository.save(user);

        UserResponse response = userMapper.toResponse(savedUser);

        // Send Kafka event after commit (non-deprecated)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                userEventProducer.sendUserCreatedEvent(response);
            }
        });

        return response;
    }

    @Override
    @Transactional
    public List<UserResponse> createUsers(List<UserRequest> userRequests) {
        // Transient
        List<User> users = userRequests.stream()
                .map(userMapper::toEntity)
                .toList();

        // Persistent / Managed
        List<User> savedUsers = userRepository.saveAll(users);

        List<UserResponse> responses = savedUsers.stream()
                .map(userMapper::toResponse)
                .toList();

        // Send Kafka events after commit (non-deprecated)
        responses.forEach(response ->
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        userEventProducer.sendUserCreatedEvent(response);
                    }
                })
        );

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) throw new UserNotFoundException(name);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Persistent / Managed
        modelMapper.map(userRequest, user);

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse patchUser(Long id, UserPatchRequest userPatchRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        modelMapper.map(userPatchRequest, user);

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    /*@Override
    public UserResponse patchUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

       if(updates.containsKey("name")){
            user.setName(updates.get("name").toString());
       }
       if(updates.containsKey("email")){
            user.setEmail(updates.get("email").toString());
       }
       if(updates.containsKey("age")){
           user.setAge(Integer.parseInt(updates.get("age").toString()));
       }
       userRepository.save(user);
       return userMapper.toResponse(user);
    }

    Entity in Service X (not recommended)
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
