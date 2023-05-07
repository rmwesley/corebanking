package com.example.corebanking.model;

public enum OperationType {
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    CASH_OUT("Cash Out");

    private final String displayName;

    OperationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
