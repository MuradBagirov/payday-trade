package com.example.paydaytrade.repository;


import com.example.paydaytrade.enums.RoleEnum;
import com.example.paydaytrade.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
