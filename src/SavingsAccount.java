package com.bankingsystem;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double RATE = 0.03;

    public SavingsAccount(double initialDeposit) {
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

    @Override
    public double calculateInterest() {
        return balance * RATE;
    }
}
