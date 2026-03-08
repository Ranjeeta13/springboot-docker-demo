package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

   @PostMapping("/createUser")
   public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
       return userService.createUser(userRequest);

   }

   @PostMapping("/createUsers")
   public List<UserResponse> createUsers(@Valid @RequestBody List<UserRequest> userRequests) {
        return userService.createUsers(userRequests);
   }

   @GetMapping("/{id}")
   public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserById(id);
   }

   @GetMapping
   public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
   }

  /* Controller -Entity X(not recommended)
   @PostMapping
   public User createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
   }
   @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
   }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }*/

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
    }

    @DeleteMapping
    public void deleteAllUsers(){
        userService.deleteAllUsers();
    }

    /*
    Controller to repository not recommended
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public List<User> createUser(@Valid @RequestBody List<User> users) {
        return userRepository.saveAll(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
     */
}
