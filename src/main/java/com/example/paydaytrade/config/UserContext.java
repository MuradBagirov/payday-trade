package com.example.paydaytrade.config;

import com.example.paydaytrade.exception.AuthenticationException;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private static UserRepository userRepository;
    public UserContext(UserRepository userRepository) {
        UserContext.userRepository = userRepository;
    }
    public static User getUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(name)
                .orElseThrow(() -> new AuthenticationException(String.format("User not found, username: %s", name)));
    }
}
