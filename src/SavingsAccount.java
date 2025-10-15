package com.bankingsystem;

public class SavingsAccount extends Account implements InterestBearing {
    public SavingsAccount(double initialDeposit) {
        super(initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) balance -= amount;
    }

    @Override
    public double calculateInterest() {
        return balance * 0.03;
    }
}
