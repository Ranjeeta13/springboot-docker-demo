package com.example.userservice.controller;

import com.example.userservice.dto.UserPatchRequest;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
   public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
       UserResponse userResponse=userService.createUser(userRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

   }

   @PostMapping("/createUsers")
   public ResponseEntity<List<UserResponse>> createUsers(@Valid @RequestBody List<UserRequest> userRequests) {
       if (userRequests == null || userRequests.isEmpty()) {
           return ResponseEntity
                   .badRequest()
                   .build();
       }
       List<UserResponse> userResponseList=userService.createUsers(userRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseList);
   }

   @GetMapping("/{id}")
   public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        UserResponse userResponse= userService.getUserById(id);
        if(userResponse==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponse);
   }

   @GetMapping
   public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> userResponses= userService.getAllUsers();
        if(userResponses==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponses);
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {

        userService.deleteAllUsers();

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse=userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }
   //DTO-based patching
   @PatchMapping("/{id}")
   public ResponseEntity<UserResponse> updateUserPartially(@PathVariable Long id, @Valid @RequestBody UserPatchRequest userPatchRequest) {
        UserResponse patchedUser = userService.patchUser(id,userPatchRequest);
        return ResponseEntity.ok(patchedUser);
   }

   /*
   @PatchMapping("/{id}")
   public ResponseEntity<UserResponse> updateUserPartially(@Valid @PathVariable Long id, @RequestBody Map<String,Object> updates) {

        UserResponse patchedUser = userService.patchUser(id,updates);
        return ResponseEntity.ok(patchedUser);
   }
    //    @PatchMapping("/{id}/email")
    // public ResponseEntity<UserResponse> updateEmail(
    //         @PathVariable Long id,
    //         @RequestBody String email) {
    //
    //     return ResponseEntity.ok(userService.updateEmail(id, email));
    // }
   Controller -Entity X(not recommended)
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
