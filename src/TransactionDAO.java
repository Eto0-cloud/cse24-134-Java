import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO {

    // Adjust DDL to match your RDBMS. This uses AUTO_INCREMENT (MySQL). For SQLite use "INTEGER PRIMARY KEY AUTOINCREMENT".
    private final String createSql = "CREATE TABLE IF NOT EXISTS transactions ("
            + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
            + "account_number VARCHAR(20) NOT NULL,"
            + "type VARCHAR(30) NOT NULL,"
            + "amount DOUBLE NOT NULL,"
            + "description VARCHAR(255),"
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE"
            + ")";

    public void createTable() throws SQLException {
        try (Connection c = Db.getConnection();
             Statement s = c.createStatement()) {
            s.execute(createSql);
        }
    }

    // Insert and return generated id (0 if not returned)
    public long insert(Transaction t) throws SQLException {
        try (Connection c = Db.getConnection()) {
            return insert(c, t);
        }
    }

    // Transactional insert using existing connection
    public long insert(Connection conn, Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions(account_number, type, amount, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getAccountNumber());
            ps.setString(2, t.getType());
            ps.setDouble(3, t.getAmount());
            ps.setString(4, t.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0L;
    }

    public Optional<Transaction> findById(long id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(TransactionMapper.fromResultSet(rs));
            }
        }
        return Optional.empty();
    }

    public List<Transaction> findByAccount(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY created_at DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(TransactionMapper.fromResultSet(rs));
                }
            }
        }
        return list;
    }

    // Simple delete by id
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
