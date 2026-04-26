package com.jenkins.saas.auth;

public interface AuthenticationService {

    LoginResponse login(final LoginRequest request);
}
