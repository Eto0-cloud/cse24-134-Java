package com.bankingsystem;

import bankingsystem.ui.AccountView;
import bankingsystem.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        AccountView accountView = new AccountView();
        ControllerStub.wire(loginView, accountView);
        Scene loginScene = new Scene(loginView.getView(), 320, 240);
        Scene accountScene = new Scene(accountView.getView(), 360, 280);

        // Simple scene switch triggered by successful login message update
        loginView.setOnLogin(() -> {
            String email = loginView.getEmail();
            String pwd = loginView.getPassword();
            if (ControllerStub.authenticate(email, pwd)) {
                accountView.setCustomerName("Katlego Mokoena");
                accountView.setAccountType("Savings");
                accountView.setBalance(ControllerStub.getBalance());
                stage.setScene(accountScene);
            } else {
                loginView.showMessage("Invalid credentials");
            }
        });

        stage.setTitle("Banking UI");
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
