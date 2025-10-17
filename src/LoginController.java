import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField loginIdField;
    @FXML private Button registerBtn;
    @FXML private Button loginBtn;
    @FXML private Label infoLabel;

    private Bank bank; // injected by MainApp

    @FXML
    private void initialize() {
        registerBtn.setOnAction(e -> onRegister());
        loginBtn.setOnAction(e -> onLogin());
    }

    // called reflectively by MainApp.showLogin or by FXMLLoader caller
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    private void onRegister() {
        String first = safeTrim(firstNameField.getText());
        String last = safeTrim(lastNameField.getText());
        String addr = safeTrim(addressField.getText());
        if (first.isEmpty() || last.isEmpty()) {
            infoLabel.setText("First and last name required.");
            return;
        }
        try {
            Customer c = getBank().registerCustomer(first, last, addr);
            infoLabel.setText("Registered: " + c.getCustomerId());
            loginIdField.setText(c.getCustomerId()); // allow immediate login
            firstNameField.clear();
            lastNameField.clear();
            addressField.clear();
        } catch (Exception ex) {
            infoLabel.setText("Register failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void onLogin() {
        String id = safeTrim(loginIdField.getText());
        if (id.isEmpty()) {
            infoLabel.setText("Enter customer id or register first.");
            return;
        }
        Customer c = getBank().findCustomerById(id);
        if (c == null) {
            infoLabel.setText("Customer not found.");
            return;
        }
        try {
            MainApp.showBankingSystem(c);
        } catch (Exception ex) {
            infoLabel.setText("Failed to open dashboard: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Bank getBank() {
        return bank == null ? MainApp.BANK : bank;
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
