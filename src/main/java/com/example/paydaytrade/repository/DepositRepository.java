package com.example.paydaytrade.repository;

import com.example.paydaytrade.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit,Long> {
}
