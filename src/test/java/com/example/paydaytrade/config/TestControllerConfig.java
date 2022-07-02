package com.example.paydaytrade.config;


import com.example.paydaytrade.security.AuthEntryPointJwt;
import com.example.paydaytrade.security.JwtUtils;
import com.example.paydaytrade.security.userDetails.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestControllerConfig {
    @Bean
    public UserDetailsImpl controllerService() {
        return new UserDetailsImpl(null, null, null, null);
    }

    @Bean
    public AuthEntryPointJwt bean() {
        return new AuthEntryPointJwt();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
