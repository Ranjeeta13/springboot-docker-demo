package com.example.userservice.entity;

import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userTest(){
       List<User> users = userRepository.findAll();
       System.out.println(users.getFirst().getName());
       Assertions.assertNotNull(users);
    }
}