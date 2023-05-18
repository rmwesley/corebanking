package com.example.corebanking.model;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client implements IHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  String name;

  @OneToMany Collection<Account> accounts;

  public Client() {}

  public Client(String name) {
    this.name = name;
  }

  public void addAccount(Account acc) {
    accounts.add(acc);
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return this.id;
  }

  public Collection<Account> getAccounts() {
    return this.accounts;
  }

  public double calculateTotalBalance() {
    return this.accounts.stream().mapToDouble(Account::getBalance).sum();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    Client client = (Client) obj;
    return client.id == this.id && this.name.equals(client.name);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + accounts.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Client " + this.id + ": " + this.name;
  }

  @Override
  public String history() {
    return accounts.stream().map(Object::toString).collect(Collectors.joining("\n\n"));
  }
}
