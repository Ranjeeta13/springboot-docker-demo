package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Test
    void updateName() {
        int rowsUpdated = userRepository.updateName(11L, "Simran");
        System.out.println("Rows affected: " + rowsUpdated);

        // To see the change, you must fetch the user again
        User updatedUser = userRepository.findById(11L).orElse(null);
        System.out.println(updatedUser);
    }

    @Test
    void testPagination() {
        // Fetch the 1st page, with 5 users per page, sorted by name
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

        Page<User> userPage = userRepository.findUsersInPages(pageable);

        System.out.println("Total Elements: " + userPage.getTotalElements());
        System.out.println("Total Pages: " + userPage.getTotalPages());
        System.out.println("Current Page Content: " + userPage.getContent());
    }
}