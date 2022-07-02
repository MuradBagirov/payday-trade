package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.LoginDto;
import com.example.paydaytrade.dto.RegisterDto;
import com.example.paydaytrade.model.User;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface AuthService {
    User register(RegisterDto registerDto, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    ResponseEntity<?> login(LoginDto loginDto);

}
