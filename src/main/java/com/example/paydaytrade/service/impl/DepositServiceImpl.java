package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.balance.ReqDeposit;
import com.example.paydaytrade.exception.NotFoundException;
import com.example.paydaytrade.model.Deposit;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.DepositRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository balanceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addDepositForUser(ReqDeposit balanceRequest, Long id) {

        Deposit deposit = new Deposit();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getBalance() == null) {
            user.setBalance(deposit);
            userRepository.save(user);
        }

        if (user.getBalance().getCurrentBalance() == null) {
            deposit.setCurrentBalance(BigDecimal.ZERO);
            balanceRepository.save(deposit);
        }

        BigDecimal balanceUser = user.getBalance().getCurrentBalance().add(balanceRequest.getAddBalance());
        user.getBalance().setCurrentBalance(balanceUser);
        userRepository.save(user);
    }
}
