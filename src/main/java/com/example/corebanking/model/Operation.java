package com.example.corebanking.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;

@Entity
public class Operation implements Runnable {

  @Id @GeneratedValue private Long id;

  @ManyToOne private final Account sourceAccount;

  @ManyToOne private final Account targetAccount;

  private final OperationType operationType;

  @CreatedDate private Date created_at;

  private double amount;

  private boolean alreadyExecuted;

  public Operation(Account source, OperationType operationType, double amount) {
    if (operationType == OperationType.TRANSFER) {
      throw new IllegalArgumentException(
          "Target account needs to be provided when making a transference");
    }
    this.sourceAccount = source;
    this.targetAccount = null;
    this.operationType = operationType;
    this.amount = amount;
    this.alreadyExecuted = false;
  }

  public Operation(Account source, Account target, OperationType operationType, double amount) {
    this.sourceAccount = source;
    this.targetAccount = target;
    this.operationType = operationType;
    this.amount = amount;
    this.alreadyExecuted = false;
  }

  @Override
  public void run() {
    switch (operationType) {
      case DEPOSIT:
        sourceAccount.deposit(amount);
        break;
      case WITHDRAW:
        sourceAccount.withdraw(amount);
        break;
      case CASH_OUT:
        sourceAccount.withdrawAll();
        break;
      case TRANSFER:
        if (amount < 0) {
          throw new IllegalArgumentException("Only positive transfer amounts are allowed");
        }
        sourceAccount.withdraw(amount);
        targetAccount.deposit(amount);
    }
    this.alreadyExecuted = true;
  }

  public boolean wasAlreadyExecuted() {
    return alreadyExecuted;
  }

  @Override
  public String toString() {
    String str = "";
    str += "Operation: " + this.operationType + "\n";
    str += "Date: " + this.created_at + "\n";
    if (operationType != OperationType.DEPOSIT) {
      str += "Amount: " + this.amount + "\n";
    }
    return str;
  }
}
