package com.bankingsystem;

public abstract class Account {
    protected String accountNumber;
    protected double balance;

    private static long acctCounter = 100000L;

    public Account(double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("initialDeposit must not be negative");
        }
        this.balance = initialDeposit;
        this.accountNumber = generateAccountNumber();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("deposit amount must be positive");
        }
        balance += amount;
    }

    public abstract void withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return accountNumber + " : " + balance;
    }

    private static synchronized String generateAccountNumber() {
        acctCounter++;
        return "ACCT" + acctCounter;
    }
}
