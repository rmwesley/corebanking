package com.example.corebanking.repository;

import com.example.corebanking.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {}
