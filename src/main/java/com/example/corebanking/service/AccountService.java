package com.example.corebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.corebanking.model.Account;
import com.example.corebanking.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Transactional
    public void deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.deposit(amount);
        accountRepository.save(account);
    }
    @Transactional
    public void withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.withdraw(amount);
        accountRepository.save(account);
    }
    @Transactional
    public void withdrawAll(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.withdrawAll();
        accountRepository.save(account);
    }
    
}