package com.jenkins.saas.auth;

import com.jenkins.saas.entity.User;
import com.jenkins.saas.security.JwtService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        final User user = (User) authentication.getPrincipal();
        final String token = this.jwtService.generateAccessToken(user.getTenantId(), user.getId(), user.getRole().name());
        final String tokenType = "Bearer";
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType(tokenType)
                .build();
    }
}
