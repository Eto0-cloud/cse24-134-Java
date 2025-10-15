package com.bankingsystem;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {
    private final VBox root = new VBox(10);
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Log in");
    private final Label message = new Label();
    private Runnable onLogin;

    public LoginView() {
        root.setPadding(new Insets(16));
        emailField.setPromptText("Email");
        passwordField.setPromptText("Password");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> { message.setText(""); if (onLogin != null) onLogin.run(); });
        root.getChildren().addAll(new Label("Sign in"), emailField, passwordField, loginButton, message);
    }

    public Node getView() { return root; }
    public String getEmail() { return emailField.getText(); }
    public String getPassword() { return passwordField.getText(); }
    public void showMessage(String msg) { message.setText(msg); }
    public void setOnLogin(Runnable r) { this.onLogin = r; }
}
