import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static final Bank BANK = new Bank("Demo Bank");
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("Login.fxml"));
        Parent root = loader.load();

        // inject Bank into login controller if it exposes setBank(Bank)
        Object ctrl = loader.getController();
        try {
            ctrl.getClass().getMethod("setBank", Bank.class).invoke(ctrl, BANK);
        } catch (NoSuchMethodException ignored) {
            // controller doesn't have setBank — that's fine
        }

        primaryStage.setTitle("Bank - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Show the main banking UI after a successful login.
     * Uses reflection to inject Bank and optionally the logged-in Customer to avoid compile-time coupling.
     */
    public static void showBankingSystem(Customer loggedInCustomer) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("BankingSystem.fxml"));
        Parent root = loader.load();

        Object ctrl = loader.getController();

        // try to inject Bank if the controller provides setBank(Bank)
        try {
            ctrl.getClass().getMethod("setBank", Bank.class).invoke(ctrl, BANK);
        } catch (NoSuchMethodException ignored) {
        }

        // try to inject the logged-in customer if the controller provides setLoggedInCustomer(Customer)
        if (loggedInCustomer != null) {
            try {
                ctrl.getClass().getMethod("setLoggedInCustomer", Customer.class).invoke(ctrl, loggedInCustomer);
            } catch (NoSuchMethodException ignored) {
            }
        }

        primaryStage.setTitle("Bank - Dashboard");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
