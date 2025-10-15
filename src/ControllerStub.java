package com.bankingsystem;

import bankingsystem.ui.AccountView;
import bankingsystem.ui.LoginView;

/**
 * Business logic lives here. Views call into this class via callbacks.
 * This stub is intentionally tiny to show separation.
 */
public class ControllerStub {
    private static double balance = 1000.0;

    public static boolean authenticate(String email, String password) {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }

    public static void wire(LoginView login, AccountView account) {
        login.setOnLogin(() -> {
            String email = login.getEmail();
            String pwd = login.getPassword();
            if (authenticate(email, pwd)) {
                account.setCustomerName("Katlego Mokoena");
                account.setAccountType("Savings");
                account.setBalance(balance);
                login.showMessage("Login successful");
            } else {
                login.showMessage("Invalid credentials");
            }
        });

        account.setOnDeposit(() -> {
            double amt = account.getEnteredAmount();
            if (amt > 0) { balance += amt; account.setBalance(balance); account.showMessage("Deposit done"); }
            else account.showMessage("Enter positive amount");
        });

        account.setOnWithdraw(() -> {
            double amt = account.getEnteredAmount();
            if (amt > 0 && amt <= balance) { balance -= amt; account.setBalance(balance); account.showMessage("Withdrawal done"); }
            else account.showMessage("Invalid withdrawal");
        });
    }

    public static double getBalance() { return balance; }
}
