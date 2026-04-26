package com.jenkins.saas.auth;

import com.jenkins.saas.requests.RegisterTenantRequest;
import com.jenkins.saas.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ap/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private TenantService tenantService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody final LoginRequest request) {

        final LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid
            @RequestBody final RegisterTenantRequest request) {

        this.tenantService.registerTenant(request);
        return ResponseEntity.ok().build();
    }
}
