package com.jenkins.saas.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "username should not be empty")
    private String username;
    @NotBlank(message = "password should not be empty")
    private String password;
}
