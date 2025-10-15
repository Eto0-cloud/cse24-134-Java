package com.bankingsystem;

public class InvestmentAccount extends Account implements InterestBearing {
    public InvestmentAccount(double initialDeposit) {
        super(initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) balance -= amount;
    }

    @Override
    public double calculateInterest() {
        return balance * 0.05;
    }
}
