package com.bankingsystem;

public class ChequeAccount extends Account {
    public ChequeAccount(double initialDeposit) {
        super(initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("withdraw amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("insufficient funds");
        }
        balance -= amount;
    }
}
