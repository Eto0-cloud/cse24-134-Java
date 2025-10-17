package com.bankingsystem;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double RATE = 0.05;

    public InvestmentAccount(double initialDeposit) {
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
