package com.bankingsystem;

public class BankingSystemTest {
    public static void main(String[] args) {
        Customer customer = new Customer("Katlego", "Mokoena", "katlego@example.com");
        System.out.println("Customer created: " + customer.getFirstName() + " " + customer.getLastName());

        Account savings = customer.openAccount("savings", 1000.0);
        System.out.println(savings.getAccountNumber() + " opened with balance: " + savings.getBalance());

        savings.deposit(500.0);
        System.out.println("After deposit, balance: " + savings.getBalance());

        savings.withdraw(300.0);
        System.out.println("After withdrawal, balance: " + savings.getBalance());

        if (savings instanceof InterestBearing) {
            double interest = ((InterestBearing) savings).calculateInterest();
            System.out.println("Calculated interest: " + interest);
        }

        Account cheque = customer.openAccount("cheque", 200.0);
        System.out.println(cheque.getAccountNumber() + " opened with balance: " + cheque.getBalance());
        cheque.deposit(100.0);
        cheque.withdraw(50.0);
        System.out.println("Cheque Account final balance: " + cheque.getBalance());

        Account investment = customer.openAccount("investment", 5000.0);
        System.out.println(investment.getAccountNumber() + " opened with balance: " + investment.getBalance());
        if (investment instanceof InterestBearing) {
            double interest = ((InterestBearing) investment).calculateInterest();
            System.out.println("Investment Account interest: " + interest);
        }
    }
}
