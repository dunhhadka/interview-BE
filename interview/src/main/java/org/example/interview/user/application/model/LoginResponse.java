package org.example.interview.user.application.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private UserResponse user;
    private String token;
}
