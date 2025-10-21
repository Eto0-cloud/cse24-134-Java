import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BankingSystemController {

    // Buttons
    @FXML public Button registerBtn;
    @FXML public Button openAccountBtn;
    @FXML public Button depositBtn;
    @FXML public Button withdrawBtn;
    @FXML public Button interestBtn;
    @FXML public Button listBtn;
    @FXML public Button exitBtn;

    // Register fields
    @FXML public TextField firstNameField;
    @FXML public TextField lastNameField;
    @FXML public TextField addressField;

    // Open account fields
    @FXML public TextField custIdField;
    @FXML public TextField acctTypeField;
    @FXML public TextField initDepositField;
    @FXML public TextField empNameField;
    @FXML public TextField empAddrField;
    @FXML public TextField odField;

    // Account operations
    @FXML public TextField acctNumberField;
    @FXML public TextField amountField;

    // Output
    @FXML public TextArea outputArea;
    @FXML public ListView<String> listView;
    @FXML public Label statusLabel;

    private Bank bank;
    private final ObservableList<String> listItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (listView != null) listView.setItems(listItems);

        if (registerBtn != null) registerBtn.setOnAction(e -> handleRegister());
        if (openAccountBtn != null) openAccountBtn.setOnAction(e -> handleOpenAccount());
        if (depositBtn != null) depositBtn.setOnAction(e -> handleDeposit());
        if (withdrawBtn != null) withdrawBtn.setOnAction(e -> handleWithdraw());
        if (interestBtn != null) interestBtn.setOnAction(e -> handleApplyInterest());
        if (listBtn != null) listBtn.setOnAction(e -> handleListCustomers());
        if (exitBtn != null) exitBtn.setOnAction(e -> handleExit());

        if (statusLabel != null) statusLabel.setText("Status: ready");
    }

    // Typed injection by MainApp
    public void setBank(Bank bank) {
        this.bank = bank;
        if (statusLabel != null) statusLabel.setText("Status: connected to " + (bank == null ? "null" : bank.getName()));
    }

    // Optional: called by MainApp after successful login
    public void setLoggedInCustomer(Customer c) {
        if (c == null) return;
        if (custIdField != null) custIdField.setText(c.getCustomerId());
        appendOutput("Logged in: " + c.getFullName());
    }

    // ---- Event handlers (controllers validate only, model enforces business rules) ----

    public void handleRegister() {
        if (!ensureBank()) return;
        String f = safeTrim(firstNameField == null ? null : firstNameField.getText());
        String l = safeTrim(lastNameField == null ? null : lastNameField.getText());
        String addr = safeTrim(addressField == null ? null : addressField.getText());
        if (f.isEmpty() || l.isEmpty()) { appendOutput("First and last name required."); return; }
        try {
            Customer c = bank.registerCustomer(f, l, addr);
            appendOutput("Registered: " + c.getCustomerId() + " - " + c.getFullName());
            if (custIdField != null) custIdField.setText(c.getCustomerId());
            clearFields(firstNameField, lastNameField, addressField);
        } catch (Exception ex) {
            appendOutput("Error registering: " + ex.getMessage());
        }
    }

    public void handleOpenAccount() {
        if (!ensureBank()) return;
        String id = safeTrim(custIdField == null ? null : custIdField.getText());
        if (id.isEmpty()) { appendOutput("Customer id required."); return; }
        Customer cust = bank.findCustomerById(id);
        if (cust == null) { appendOutput("Customer not found: " + id); return; }

        String t = safeTrim(acctTypeField == null ? null : acctTypeField.getText()).toLowerCase();
        double init = parseDouble(initDepositField == null ? null : initDepositField.getText(), Double.NaN);
        if (Double.isNaN(init)) { appendOutput("Enter a valid initial deposit."); return; }

        try {
            Account created = null;
            if (t.startsWith("a") || t.contains("savings")) {
                created = bank.openSavingsAccount(cust, init, "Main");
                appendOutput("Opened Savings for " + cust.getCustomerId());
            } else if (t.startsWith("b") || t.contains("investment")) {
                // Business rule lives in model (InvestmentAccount will enforce minimum)
                created = bank.openInvestmentAccount(cust, init, "Main");
                appendOutput("Opened Investment for " + cust.getCustomerId());
            } else if (t.startsWith("c") || t.contains("cheque")) {
                String emp = safeTrim(empNameField == null ? null : empNameField.getText());
                String empAddr = safeTrim(empAddrField == null ? null : empAddrField.getText());
                double od = parseDouble(odField == null ? null : odField.getText(), 0);
                created = bank.openChequeAccount(cust, init, "Main", emp, empAddr, od);
                appendOutput("Opened Cheque for " + cust.getCustomerId());
            } else {
                appendOutput("Invalid account type. Use savings/investment/cheque.");
                return;
            }

            if (created != null) {
                // Refresh and show new account number for immediate deposit/withdraw
                handleListCustomers();
                if (acctNumberField != null) acctNumberField.setText(created.getAccountNumber());
                appendOutput("Account number: " + created.getAccountNumber());
            }
        } catch (IllegalArgumentException iae) {
            appendOutput("Failed to open account: " + iae.getMessage());
        } catch (Exception ex) {
            appendOutput("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void handleDeposit() {
        if (!ensureBank()) return;
        String custId = safeTrim(custIdField == null ? null : custIdField.getText());
        Customer cust = bank.findCustomerById(custId);
        if (cust == null) { appendOutput("Customer not found: " + custId); return; }
        String accNo = safeTrim(acctNumberField == null ? null : acctNumberField.getText());
        if (accNo.isEmpty()) { appendOutput("Enter account number."); return; }
        Account a = cust.findAccountByNumber(accNo);
        if (a == null) { appendOutput("Account not found: " + accNo); return; }
        double amt = parseDouble(amountField == null ? null : amountField.getText(), -1);
        if (amt <= 0) { appendOutput("Enter positive amount."); return; }
        try {
            a.deposit(amt, "GUI deposit");
            appendOutput(String.format("Deposited %.2f into %s. New balance: %.2f", amt, a.getAccountNumber(), a.getBalance()));
        } catch (Exception ex) {
            appendOutput("Deposit failed: " + ex.getMessage());
        }
    }

    public void handleWithdraw() {
        if (!ensureBank()) return;
        String custId = safeTrim(custIdField == null ? null : custIdField.getText());
        Customer cust = bank.findCustomerById(custId);
        if (cust == null) { appendOutput("Customer not found: " + custId); return; }
        String accNo = safeTrim(acctNumberField == null ? null : acctNumberField.getText());
        if (accNo.isEmpty()) { appendOutput("Enter account number."); return; }
        Account a = cust.findAccountByNumber(accNo);
        if (a == null) { appendOutput("Account not found: " + accNo); return; }
        double amt = parseDouble(amountField == null ? null : amountField.getText(), -1);
        if (amt <= 0) { appendOutput("Enter positive amount."); return; }
        try {
            boolean ok = a.withdraw(amt);
            appendOutput(ok ? String.format("Withdrew %.2f from %s. New balance: %.2f", amt, a.getAccountNumber(), a.getBalance())
                            : "Withdraw failed (insufficient funds or rules).");
        } catch (Exception ex) {
            appendOutput("Withdraw failed: " + ex.getMessage());
        }
    }

    public void handleApplyInterest() {
        if (!ensureBank()) return;
        bank.applyMonthlyInterests();
        appendOutput("Monthly interest applied.");
    }

    public void handleListCustomers() {
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

    public void handleExit() {
        appendOutput("Exit requested. Close application window to terminate.");
    }

    // ---- Helpers ----

    private boolean ensureBank() {
        if (bank == null) {
            appendOutput("Model not connected. Call setBank(bank) before using this UI.");
            return false;
        }
        return true;
    }

    private void appendOutput(String text) {
        if (outputArea != null) outputArea.appendText(text + "\n");
        else System.out.println(text);
        if (statusLabel != null) statusLabel.setText("Status: updated");
    }

    private String safeTrim(String s) { return s == null ? "" : s.trim(); }
    private void clearFields(TextField... fields) { for (TextField f : fields) if (f != null) f.clear(); }
    private double parseDouble(String s, double def) { if (s == null || s.trim().isEmpty()) return def; try { return Double.parseDouble(s.trim()); } catch (NumberFormatException e) { return def; } }
}
