package com.jenkins.saas.repositories;

import com.jenkins.saas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {


    Optional<User> findByUsername(String username);

    boolean existsByUsername(String adminUsername);
}
