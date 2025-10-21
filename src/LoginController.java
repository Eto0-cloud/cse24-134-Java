import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField loginIdField;
    @FXML private Button registerBtn;
    @FXML private Button loginBtn;
    @FXML private Label infoLabel;

    private Bank bank;

    @FXML
    private void initialize() {
        if (registerBtn != null) registerBtn.setOnAction(e -> onRegister());
        if (loginBtn != null) loginBtn.setOnAction(e -> onLogin());
    }

    // Typed injection by MainApp
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    public void onRegister() {
        String first = safeTrim(firstNameField == null ? null : firstNameField.getText());
        String last = safeTrim(lastNameField == null ? null : lastNameField.getText());
        String addr = safeTrim(addressField == null ? null : addressField.getText());

        if (first.isEmpty() || last.isEmpty()) {
            setInfo("First and last name required.");
            return;
        }

        try {
            Customer c = getBank().registerCustomer(first, last, addr);
            setInfo("Registered: " + c.getCustomerId());
            if (loginIdField != null) loginIdField.setText(c.getCustomerId());
            clearFields(firstNameField, lastNameField, addressField);
        } catch (Exception ex) {
            setInfo("Register failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void onLogin() {
        String id = safeTrim(loginIdField == null ? null : loginIdField.getText());
        if (id.isEmpty()) {
            setInfo("Enter customer id or register first.");
            return;
        }

        Customer c = getBank().findCustomerById(id);
        if (c == null) {
            setInfo("Customer not found.");
            return;
        }

        try {
            MainApp.showBankingSystem(c);
        } catch (Exception ex) {
            setInfo("Failed to open dashboard: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Helpers
    private Bank getBank() { return bank == null ? MainApp.BANK : bank; }

    private void setInfo(String text) { if (infoLabel != null) infoLabel.setText(text); else System.out.println(text); }

    private void clearFields(TextField... fields) { for (TextField f : fields) if (f != null) f.clear(); }

    private String safeTrim(String s) { return s == null ? "" : s.trim(); }
}
