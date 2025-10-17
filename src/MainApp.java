package com.bankingsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        AccountView accountView = new AccountView();

        // Wire views to controller stub (keeps business logic out of views)
        ControllerStub.wire(loginView, accountView);

        Scene loginScene = new Scene(loginView.getView(), 320, 240);
        Scene accountScene = new Scene(accountView.getView(), 360, 280);

        // Switch scene when controller updates the account view after successful login
        // ControllerStub will set a success message; we detect it here by using the login callback
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
