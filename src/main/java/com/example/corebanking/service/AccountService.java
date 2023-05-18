package com.example.corebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.corebanking.dto.OperationDTO;
import com.example.corebanking.model.Account;
import com.example.corebanking.model.Client;
import com.example.corebanking.model.Operation;
import com.example.corebanking.model.OperationType;
import com.example.corebanking.repository.AccountRepository;
import com.example.corebanking.repository.ClientRepository;

@Service
public class AccountService {

  @Autowired private AccountRepository accountRepository;
  @Autowired private ClientRepository clientRepository;

  public Account createNewAccountFor(Client client) {
    Account account = new Account(client);
    client.addAccount(account);
    accountRepository.saveAndFlush(account);
    clientRepository.flush();
    return account;
  }

  public void execute(Operation operation) {
    operation.run();
  }

  @Transactional
  public void transfer(Long sourceAccountId, Long targetAccountId, double amount) {
    Account sourceAccount =
        accountRepository
            .findById(sourceAccountId)
            .orElseThrow(() -> new RuntimeException("Source account not found"));
    Account targetAccount =
        accountRepository
            .findById(targetAccountId)
            .orElseThrow(() -> new RuntimeException("Source account not found"));
    sourceAccount.withdraw(amount);
    targetAccount.deposit(amount);
  }

  public void transfer(OperationDTO operationDTO) {
    Account sourceAccount = accountRepository.getReferenceById(operationDTO.getSourceId());
    Account targetAccount = accountRepository.getReferenceById(operationDTO.getTargetId());

    Operation transferOperation =
        new Operation(
            sourceAccount, targetAccount, OperationType.TRANSFER, operationDTO.getAmount());
    transferOperation.run();
  }
}
