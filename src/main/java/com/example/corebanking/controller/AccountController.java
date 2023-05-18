package com.example.corebanking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.corebanking.dto.OperationDTO;
import com.example.corebanking.model.Account;
import com.example.corebanking.repository.AccountRepository;
import com.example.corebanking.repository.ClientRepository;
import com.example.corebanking.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts API")
public class AccountController {

  @Autowired private AccountService accountService;
  @Autowired private AccountRepository accountRepository;
  @Autowired private ClientRepository clientRepository;

  @Operation(summary = "Get account by id")
  @GetMapping("/{accountId}")
  public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
    Account account = accountRepository.getReferenceById(accountId);
    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @Operation(summary = "Deposit an amount to account")
  @PostMapping("/{accountId}/deposit")
  public ResponseEntity<Account> deposit(
      @PathVariable Long accountId, @RequestBody OperationDTO operationDTO) {
    Account account = accountRepository.getReferenceById(accountId);

    account.deposit(operationDTO.getAmount());
    accountRepository.flush();
    clientRepository.flush();
    // return ResponseEntity.ok().build();
    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @Operation(summary = "Withdraw an amount from account")
  @PostMapping("/{accountId}/withdraw")
  public ResponseEntity<Account> withdraw(
      @PathVariable Long accountId, @RequestBody OperationDTO operationDTO) {
    Account account = accountRepository.getReferenceById(accountId);

    account.withdraw(operationDTO.getAmount());
    accountRepository.flush();
    clientRepository.flush();
    // return ResponseEntity.ok().build();
    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @Operation(summary = "Withdraw everything from account")
  @PostMapping("/{accountId}/withdraw-all")
  public ResponseEntity<Account> fullWithdraw(@PathVariable Long accountId) {
    Account account = accountRepository.getReferenceById(accountId);

    account.withdrawAll();
    accountRepository.flush();
    clientRepository.flush();
    // return ResponseEntity.ok().build();
    return new ResponseEntity<>(account, HttpStatus.OK);
  }

  @Operation(summary = "Make a transference to a target account")
  @PostMapping("/{accountId}/transfer")
  public ResponseEntity<?> transfer(
      @PathVariable Long accountId, @RequestBody OperationDTO operationDTO) {
    if (operationDTO.getSourceId() != accountId) {
      throw new IllegalArgumentException("Source account and current account differ");
    }
    accountService.transfer(operationDTO);
    accountRepository.flush();
    clientRepository.flush();
    // return ResponseEntity.ok().build();
    return new ResponseEntity<>("Transfrence concluded", HttpStatus.OK);
  }

  @Operation(summary = "Delete account by id")
  @DeleteMapping("/{accountId}")
  public ResponseEntity<Long> deleteAccount(@PathVariable Long accountId) {
    Account account = accountRepository.getReferenceById(accountId);
    if (account.getBalance() != 0) {
      throw new UnsupportedOperationException("Cannot delete account if balance is not 0");
    }
    accountRepository.delete(account);
    accountRepository.flush();
    clientRepository.flush();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
