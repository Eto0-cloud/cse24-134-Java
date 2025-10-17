import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Minimal, easy-to-compile AccountController.
 * Call initData(bank, customer) after loading FXML.
 * Assumes Bank, Customer, Account model methods shown below exist.
 */
public class AccountController {

    @FXML private Label lblCustomerName;
    @FXML private ListView<String> listViewAccounts;
    @FXML private TextField acctNumberField;
    @FXML private TextField amountField;
    @FXML private Button depositBtn;
    @FXML private Button withdrawBtn;
    @FXML private Button backBtn;
    @FXML private TextArea outputArea;

    private Bank bank;
    private Customer customer;

    // Simple initializer to be called by the loader's caller
    public void initData(Bank bank, Customer customer) {
        this.bank = bank;
        this.customer = customer;
        lblCustomerName.setText(customer == null ? "Unknown" : customer.getFullName());
        depositBtn.setOnAction(e -> onDeposit());
        withdrawBtn.setOnAction(e -> onWithdraw());
        backBtn.setOnAction(e -> onBack());
        refreshAccountList();
    }

    private void onDeposit() {
        if (!ready()) return;
        String accNo = safeTrim(acctNumberField.getText());
        double amt = parseDouble(amountField.getText(), -1);
        if (accNo.isEmpty()) { append("Enter account number."); return; }
        if (amt <= 0) { append("Enter positive amount."); return; }
        Account a = customer.findAccountByNumber(accNo);
        if (a == null) { append("Account not found: " + accNo); return; }
        a.deposit(amt, "GUI");
        append(String.format("Deposited %.2f into %s", amt, a.getAccountNumber()));
        refreshAccountList();
    }

    private void onWithdraw() {
        if (!ready()) return;
        String accNo = safeTrim(acctNumberField.getText());
        double amt = parseDouble(amountField.getText(), -1);
        if (accNo.isEmpty()) { append("Enter account number."); return; }
        if (amt <= 0) { append("Enter positive amount."); return; }
        Account a = customer.findAccountByNumber(accNo);
        if (a == null) { append("Account not found: " + accNo); return; }
        boolean ok = a.withdraw(amt);
        append(ok ? String.format("Withdrew %.2f from %s", amt, a.getAccountNumber())
                  : "Withdraw failed (insufficient funds).");
        refreshAccountList();
    }

    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Object ctrl = loader.getController();
            try { ctrl.getClass().getMethod("setBank", Bank.class).invoke(ctrl, bank); } catch (NoSuchMethodException ignored) {}
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            ex.printStackTrace();
            append("Failed to go back: " + ex.getMessage());
        }
    }

    private void refreshAccountList() {
        listViewAccounts.getItems().clear();
        if (customer == null) return;
        for (Account a : customer.getAccounts()) {
            listViewAccounts.getItems().add(a.getAccountNumber() + "  Balance: " + a.getBalance());
        }
    }

    private boolean ready() {
        if (bank == null || customer == null) { append("Controller not initialised."); return false; }
        return true;
    }

    private void append(String text) {
        if (outputArea != null) outputArea.appendText(text + "\n");
        else System.out.println(text);
    }

    private String safeTrim(String s) { return s == null ? "" : s.trim(); }

    private double parseDouble(String s, double def) {
        if (s == null || s.trim().isEmpty()) return def;
        try { return Double.parseDouble(s.trim()); } catch (NumberFormatException e) { return def; }
    }
}
