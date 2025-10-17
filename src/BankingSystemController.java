import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import java.util.List;

public class BankingSystemController {

    @FXML public Button registerBtn;
    @FXML public Button openAccountBtn;
    @FXML public Button depositBtn;
    @FXML public Button withdrawBtn;
    @FXML public Button interestBtn;
    @FXML public Button listBtn;
    @FXML public Button exitBtn;

    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField addressField;

    @FXML public TextField custIdField;
    @FXML public TextField acctTypeField;
    @FXML public TextField initDepositField;
    @FXML public TextField empNameField;
    @FXML public TextField empAddrField;
    @FXML public TextField odField;

    @FXML public TextField acctNumberField;
    @FXML public TextField amountField;

    @FXML public TextArea outputArea;
    @FXML public ListView<String> listView;
    @FXML public Label statusLabel;

    private Bank bank; // model instance used at runtime
    private final ObservableList<String> listItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // UI-only initialisation so Scene Builder can preview safely
        listView.setItems(listItems);
        // wire top-level menu buttons to context actions that will check bank at runtime
        registerBtn.setOnAction(e -> handleRegister());
        openAccountBtn.setOnAction(e -> handleOpenAccount());
        depositBtn.setOnAction(e -> handleDeposit());
        withdrawBtn.setOnAction(e -> handleWithdraw());
        interestBtn.setOnAction(e -> handleApplyInterest());
        listBtn.setOnAction(e -> handleListCustomers());
        exitBtn.setOnAction(e -> handleExit());
        // set default status
        statusLabel.setText("Status: ready");
    }

    // Call this at runtime to attach your Bank instance (e.g., MainApp.BANK or new Bank(...))
    public void setBank(Bank bank) {
        this.bank = bank;
        statusLabel.setText("Status: connected to bank: " + (bank == null ? "null" : bank.getClass().getSimpleName()));
    }

    // ---------- Action handlers (safe if bank is null will show message) ----------
    private void handleRegister() {
        if (!ensureBank()) return;
        String f = safeTrim(firstNameField.getText());
        String l = safeTrim(lastNameField.getText());
        String addr = safeTrim(addressField.getText());
        if (f.isEmpty() || l.isEmpty()) {
            appendOutput("First and last name required.");
            return;
        }
        try {
            Customer c = bank.registerCustomer(f, l, addr);
            appendOutput("Registered: " + c.getCustomerId() + " - " + c.getFullName());
            custIdField.setText(c.getCustomerId());
            clearFields(firstNameField, lastNameField, addressField);
        } catch (Exception ex) {
            appendOutput("Error registering: " + ex.getMessage());
        }
    }

    private void handleOpenAccount() {
        if (!ensureBank()) return;
        String id = safeTrim(custIdField.getText());
        if (id.equalsIgnoreCase("list")) {
            handleListCustomers();
            return;
        }
        Customer cust = bank.findCustomerById(id);
        if (cust == null) { appendOutput("Customer not found: " + id); return; }
        String t = safeTrim(acctTypeField.getText()).toLowerCase();
        double init = parseDouble(initDepositField.getText(), 0);
        try {
            if (t.startsWith("a") || t.contains("savings")) {
                bank.openSavingsAccount(cust, init, "Main");
                appendOutput("Opened Savings for " + cust.getCustomerId());
            } else if (t.startsWith("b") || t.contains("investment")) {
                bank.openInvestmentAccount(cust, init, "Main");
                appendOutput("Opened Investment for " + cust.getCustomerId());
            } else if (t.startsWith("c") || t.contains("cheque")) {
                String emp = safeTrim(empNameField.getText());
                String empAddr = safeTrim(empAddrField.getText());
                double od = parseDouble(odField.getText(), 0);
                bank.openChequeAccount(cust, init, "Main", emp, empAddr, od);
                appendOutput("Opened Cheque for " + cust.getCustomerId());
            } else {
                appendOutput("Invalid account type. Use a/b/c or savings/investment/cheque.");
            }
        } catch (IllegalArgumentException iae) {
            appendOutput("Failed to open account: " + iae.getMessage());
        } catch (Exception ex) {
            appendOutput("Unexpected error: " + ex.getMessage());
        }
    }

    private void handleDeposit() {
        if (!ensureBank()) return;
        String custId = safeTrim(custIdField.getText());
        Customer cust = bank.findCustomerById(custId);
        if (cust == null) { appendOutput("Customer not found: " + custId); return; }
        String accNo = safeTrim(acctNumberField.getText());
        Account a = cust.findAccountByNumber(accNo);
        if (a == null) { appendOutput("Account not found: " + accNo); return; }
        double amt = parseDouble(amountField.getText(), -1);
        if (amt <= 0) { appendOutput("Enter positive amount."); return; }
        try {
            a.deposit(amt, "GUI deposit");
            appendOutput(String.format("Deposited %.2f into %s. New balance: %.2f", amt, a.getAccountNumber(), a.getBalance()));
        } catch (Exception ex) {
            appendOutput("Deposit failed: " + ex.getMessage());
        }
    }

    private void handleWithdraw() {
        if (!ensureBank()) return;
        String custId = safeTrim(custIdField.getText());
        Customer cust = bank.findCustomerById(custId);
        if (cust == null) { appendOutput("Customer not found: " + custId); return; }
        String accNo = safeTrim(acctNumberField.getText());
        Account a = cust.findAccountByNumber(accNo);
        if (a == null) { appendOutput("Account not found: " + accNo); return; }
        double amt = parseDouble(amountField.getText(), -1);
        if (amt <= 0) { appendOutput("Enter positive amount."); return; }
        try {
            boolean ok = a.withdraw(amt);
            appendOutput(ok ? String.format("Withdrew %.2f from %s. New balance: %.2f", amt, a.getAccountNumber(), a.getBalance())
                            : "Withdraw failed (insufficient funds or rules).");
        } catch (Exception ex) {
            appendOutput("Withdraw failed: " + ex.getMessage());
        }
    }

    private void handleApplyInterest() {
        if (!ensureBank()) return;
        bank.applyMonthlyInterests();
        appendOutput("Monthly interest applied.");
    }

    private void handleListCustomers() {
        if (!ensureBank()) return;
        listItems.clear();
        StringBuilder sb = new StringBuilder();
        for (Customer c : bank.getAllCustomers()) {
            String line = String.format("%s - %s (%s)", c.getCustomerId(), c.getFullName(), c.getAddress());
            listItems.add(line);
            sb.append(line).append("\n");
            for (Account acc : c.getAccounts()) {
                String extra = acc instanceof ChequeAccount ? " Employer:" + ((ChequeAccount)acc).getEmployerName() + " OD:" + ((ChequeAccount)acc).getOverdraftLimit() : "";
                sb.append(String.format("   - %s %s : %.2f%s%n", acc.getAccountType(), acc.getAccountNumber(), acc.getBalance(), extra));
            }
        }
        appendOutput(sb.length() == 0 ? "No customers registered." : sb.toString());
    }

    private void handleExit() {
        // If running inside JavaFX application, close via dialog; for simplicity just clear UI
        appendOutput("Exit requested. Close application window to terminate.");
    }

    // ---------- Helpers ----------
    private boolean ensureBank() {
        if (bank == null) {
            appendOutput("Model not connected. Call setBank(bank) before using this UI.");
            return false;
        }
        return true;
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
        statusLabel.setText("Status: updated");
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) if (f != null) f.clear();
    }

    private double parseDouble(String s, double def) {
        if (s == null || s.trim().isEmpty()) return def;
        try { return Double.parseDouble(s.trim()); } catch (NumberFormatException e) { return def; }
    }
}
