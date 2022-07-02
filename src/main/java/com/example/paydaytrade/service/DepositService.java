package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.balance.ReqDeposit;

public interface DepositService {

    void addDepositForUser(ReqDeposit balanceRequest, Long id);
}
