package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPatchRequest {
    //@NotNull(message = "Name cannot be null") For PATCH APIs, fields are supposed to be optional, since the client may update only one field.
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @Min(value = 18, message = "Age should not be less than 18")
    private Integer age;
}
