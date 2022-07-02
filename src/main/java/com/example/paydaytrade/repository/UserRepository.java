package com.example.paydaytrade.repository;


import com.example.paydaytrade.model.Deposit;
import com.example.paydaytrade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    User findByBalance(Deposit balance);

    User findByVerificationCode(String code);
}
