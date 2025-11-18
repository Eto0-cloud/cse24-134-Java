import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private static final String DEFAULT_URL = "jdbc:h2:./bank;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE";
    private static final String URL = System.getProperty("app.db.url", DEFAULT_URL);
    private static final String USER = System.getProperty("app.db.user", "sa");
    private static final String PASS = System.getProperty("app.db.pass", "");

    private Db() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
