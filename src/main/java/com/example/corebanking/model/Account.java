package com.example.corebanking.model;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GeneratedValue;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Account implements IHistory {

  @Id @GeneratedValue private Long id;

  @ManyToOne private Client client;

  private double balance = 0;

  @OneToMany(mappedBy = "sourceAccount")
  private Collection<Operation> operations;
  // private List<Operation> operations = new ArrayList<Operation>();

  public Account() {}

  public Account(Client client) {
    this.client = client;
  }

  public Account(Client client, double initialBalance) {
    this.client = client;
    balance = initialBalance;
  }

  public Long getId() {
    return id;
  }

  public double getBalance() {
    return balance;
  }

  public Collection<Operation> getOperations() {
    return operations;
  }

  public void deposit(double amount) {
    balance += amount;
  }

  public void withdraw(double amount) {
    balance -= amount;
  }

  public void withdrawAll() {
    operations.add(new Operation(this, OperationType.CASH_OUT, -balance));
    this.balance = 0;
  }

  @Override
  public String toString() {
    return this.id + ":" + this.balance;
  }

  public String history() {
    return operations.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
  }
}
