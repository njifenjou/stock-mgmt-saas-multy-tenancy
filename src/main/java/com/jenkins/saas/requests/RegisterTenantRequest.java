package com.jenkins.saas.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterTenantRequest {
    @NotBlank(message = "Company name should not be empty")
    private String companyName;
    @NotBlank(message = "Company code should not be empty")
    private String companyCode;
    @NotBlank(message = " email should not be empty")
    private String email;
    @NotBlank(message = "admin full name should not be empty")
    private String adminFullName;
    @NotBlank(message = "admin email should not be empty")
    private String adminEmail;
    @NotBlank(message = "admin user name should not be empty")
    private String adminUsername;
    @NotBlank(message = "admin password should not be empty")
    private String adminPassword;
}
