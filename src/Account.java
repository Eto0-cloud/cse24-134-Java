package com.bankingsystem;

public abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(double initialDeposit) {
        this.balance = initialDeposit;
        this.accountNumber = generateAccountNumber();
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public abstract void withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    private String generateAccountNumber() {
        return "ACCT" + Math.round(Math.random() * 100000);
    }
}
