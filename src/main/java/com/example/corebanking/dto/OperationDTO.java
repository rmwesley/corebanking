package com.example.corebanking.dto;

public class OperationDTO {
  private Long sourceAccountId;
  private Long targetAccountId;
  private double amount;

  public Long getSourceId() {
    return sourceAccountId;
  }

  public Long getTargetId() {
    return targetAccountId;
  }

  public double getAmount() {
    return amount;
  }
}
