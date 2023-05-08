package com.example.corebanking.model;

import java.sql.Date;
// import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;

@Entity
public class Operation implements Runnable {

  @Id @GeneratedValue private Long id;

  // @JoinColumn(name = "account_id")
  @ManyToOne private final Account account;

  private final OperationType operationType;

  @CreatedDate private Date created_at;

  private double amount;

  public Operation(Account account, OperationType operationType, double amount) {
    this.account = account;
    this.operationType = operationType;
    this.amount = amount;
  }

  @Override
  public void run() {
    switch (operationType) {
      case DEPOSIT:
        account.deposit(amount);
        break;
      case WITHDRAW:
        account.withdraw(amount);
        break;
      case CASH_OUT:
        account.withdrawAll();
        break;
    }
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
