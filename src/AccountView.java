package com.bankingsystem;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AccountView {
    private final VBox root = new VBox(8);
    private final Label nameLabel = new Label("Customer: -");
    private final Label typeLabel = new Label("Type: -");
    private final Label balanceLabel = new Label("Balance: 0.00");
    private final TextField amountField = new TextField();
    private final Button depositBtn = new Button("Deposit");
    private final Button withdrawBtn = new Button("Withdraw");
    private final Label infoLabel = new Label();
    private Runnable onDeposit;
    private Runnable onWithdraw;

    public AccountView() {
        root.setPadding(new Insets(16));
        amountField.setPromptText("Amount");
        depositBtn.setOnAction(e -> { infoLabel.setText(""); if (onDeposit != null) onDeposit.run(); });
        withdrawBtn.setOnAction(e -> { infoLabel.setText(""); if (onWithdraw != null) onWithdraw.run(); });
        root.getChildren().addAll(nameLabel, typeLabel, balanceLabel, amountField, depositBtn, withdrawBtn, infoLabel);
    }

    public Node getView() { return root; }
    public void setCustomerName(String n) { nameLabel.setText("Customer: " + n); }
    public void setAccountType(String t) { typeLabel.setText("Type: " + t); }
    public void setBalance(double b) { balanceLabel.setText(String.format("Balance: %.2f", b)); }
    public double getEnteredAmount() {
        try { return Double.parseDouble(amountField.getText()); } catch (Exception ex) { return 0.0; }
    }
    public void showMessage(String m) { infoLabel.setText(m); }
    public void setOnDeposit(Runnable r) { this.onDeposit = r; }
    public void setOnWithdraw(Runnable r) { this.onWithdraw = r; }
}
