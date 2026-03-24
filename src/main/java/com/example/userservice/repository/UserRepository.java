package com.example.userservice.repository;

import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    List<User> findByNameContains(String name);

    @Query("select u from User u where u.role=?1" )
    List<User> countUsersByRole(@Param("role") Role role );

    @Query("select u.role, COUNT (u) from User u group by u.role" )
    List<Object[]> countAllRolesGrouped();
}
