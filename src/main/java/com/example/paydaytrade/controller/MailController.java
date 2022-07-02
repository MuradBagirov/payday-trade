package com.example.paydaytrade.controller;

import com.example.paydaytrade.service.AuthService;
import com.example.paydaytrade.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        if (mailService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
