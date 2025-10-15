package com.bankingsystem;

public class ChequeAccount extends Account {
    public ChequeAccount(double initialDeposit) {
        super(initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) balance -= amount;
    }
}
