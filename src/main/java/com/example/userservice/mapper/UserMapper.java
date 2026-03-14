package com.example.userservice.mapper;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    /*
    public static User toEntity(UserRequest userRequest){
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setAge(userRequest.getAge());

        return user;
    }
    public static UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }*/
    public  UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
    public  User toEntity(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

}
