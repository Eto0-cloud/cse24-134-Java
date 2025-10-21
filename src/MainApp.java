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
        Object ctrl = loader.getController();
        if (ctrl instanceof LoginController) {
            ((LoginController) ctrl).setBank(BANK);
        }
        primaryStage.setTitle("Bank - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showBankingSystem(Customer loggedInCustomer) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("BankingSystem.fxml"));
        Parent root = loader.load();
        Object ctrl = loader.getController();
        if (ctrl instanceof BankingSystemController) {
            BankingSystemController bctrl = (BankingSystemController) ctrl;
            bctrl.setBank(BANK);
            if (loggedInCustomer != null) bctrl.setLoggedInCustomer(loggedInCustomer);
        }
        primaryStage.setTitle("Bank - Dashboard");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
