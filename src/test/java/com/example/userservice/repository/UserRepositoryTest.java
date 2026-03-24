package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.userservice.enums.Role.USER;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findNameContains() {
        List<User> userList=userRepository.findByNameContains("an");
        for(User user:userList){
            System.out.println(user);
        }
    }

    @Test
    void countUsersByRole() {
        List<User> userList=userRepository.findUsersByRole(USER);
        for(User user:userList){
            System.out.println(user);
        }
    }

    @Test
    void countRolesByUser() {
        List<Object[]> results = userRepository.countAllRolesGrouped();

        for (Object[] row : results) {
            System.out.println("Role: " + row[0] + " | Count: " + row[1]);
        }
    }
}