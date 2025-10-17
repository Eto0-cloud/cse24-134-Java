package com.bankingsystem;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) {
        if (firstName == null || lastName == null || email == null) {
            throw new IllegalArgumentException("firstName, lastName and email must not be null");
        }
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName, lastName and email must not be empty");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim();
    }

    public void register() {
        System.out.println("Customer registered: " + firstName + " " + lastName);
    }

    public Account openAccount(String type, double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("initialDeposit must not be negative");
        }
        String t = (type == null) ? "" : type.trim().toLowerCase();
        if (t.equals("savings")) {
            return new SavingsAccount(initialDeposit);
        } else if (t.equals("investment")) {
            return new InvestmentAccount(initialDeposit);
        } else {
            return new ChequeAccount(initialDeposit);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName must not be null or empty");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("lastName must not be null or empty");
        }
        this.lastName = lastName.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email must not be null or empty");
        }
        this.email = email.trim();
    }
}
