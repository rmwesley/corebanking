package com.example.corebanking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.corebanking.dto.OperationDTO;
import com.example.corebanking.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/{accountId}/deposit")
  public ResponseEntity<?> deposit(
      @PathVariable Long accountId, @RequestBody OperationDTO operationDTO) {
    try {
      accountService.deposit(accountId, operationDTO.getAmount());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/{accountId}/withdraw")
  public ResponseEntity<?> withdraw(
      @PathVariable Long accountId, @RequestBody OperationDTO operationDTO) {
    try {
      accountService.withdraw(accountId, operationDTO.getAmount());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/{accountId}/withdraw-all")
  public ResponseEntity<?> fullWithdraw(@PathVariable Long accountId) {
    try {
      accountService.withdrawAll(accountId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
