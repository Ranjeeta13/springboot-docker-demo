package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

    @NotNull(message = "Name cannot be null")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @Min(value = 18, message = "Age should not be less than 18")
    private Integer age;
}
