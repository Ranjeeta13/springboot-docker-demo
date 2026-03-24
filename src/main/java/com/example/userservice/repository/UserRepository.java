package com.example.userservice.repository;

import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    List<User> findByNameContains(String name);

    @Query("select u from User u where u.role=?1" )
    List<User> findUsersByRole(@Param("role") Role role );

    @Query("select u.role, COUNT (u) from User u group by u.role" )
    List<Object[]> countAllRolesGrouped();

    @Transactional
    @Modifying
    @Query(value = "update users  set name=:name where id=:id",nativeQuery = true)
    int updateName(@Param("id") Long id, @Param("name") String name);


    @Query(value = "select * from users",nativeQuery = true)
    Page<User> findUsersInPages (Pageable pageable);

}
