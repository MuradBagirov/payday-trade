package com.example.paydaytrade.controller;


import com.example.paydaytrade.config.UserContext;
import com.example.paydaytrade.dto.balance.ReqDeposit;
import com.example.paydaytrade.security.userDetails.UserDetailsImpl;
import com.example.paydaytrade.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PutMapping
    public ResponseEntity<Void> userAddBalance(@RequestBody ReqDeposit reqDeposit) {
        depositService.addDepositForUser(reqDeposit, UserContext.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private UserDetailsImpl getLoggedUser(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
