package com.example.hotelbookingmanagement.model.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String fullName;
    private String email;
    private String username;
    private String phoneNumber;
    private String password;
}
