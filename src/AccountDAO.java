import java.sql.*;
import java.util.*;

public class AccountDAO {

    private final String createSql = "CREATE TABLE IF NOT EXISTS accounts ("
            + "account_number VARCHAR(20) PRIMARY KEY,"
            + "owner_id VARCHAR(20) NOT NULL,"
            + "account_type VARCHAR(20) NOT NULL,"
            + "balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,"
            + "branch VARCHAR(100),"
            + "employer_name VARCHAR(200),"
            + "employer_address VARCHAR(255),"
            + "overdraft_limit DOUBLE DEFAULT 0.0,"
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY (owner_id) REFERENCES customers(customer_id) ON DELETE CASCADE"
            + ") ENGINE=InnoDB";

    public void createTable() throws SQLException {
        try (Connection c = Db.getConnection();
             Statement s = c.createStatement()) {
            s.execute(createSql);
        }
    }

    public void insert(Account account) throws SQLException {
        try (Connection c = Db.getConnection()) {
            insert(c, account);
        }
    }

    public void insert(Connection conn, Account account) throws SQLException {
        String sql = "INSERT INTO accounts "
                + "(account_number, owner_id, account_type, balance, branch, employer_name, employer_address, overdraft_limit) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getOwner().getCustomerId());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getBalance());
            ps.setString(5, account.getBranch());

            if (account instanceof ChequeAccount) {
                ChequeAccount ca = (ChequeAccount) account;
                ps.setString(6, ca.getEmployerName());
                ps.setString(7, ca.getEmployerAddress());
                ps.setDouble(8, ca.getOverdraftLimit());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setDouble(8, 0.0);
            }

            ps.executeUpdate();
        }
    }

    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String ownerId = rs.getString("owner_id");
                    Customer owner = new CustomerDAO().findById(c, ownerId);
                    if (owner == null) throw new SQLException("Owner not found: " + ownerId);
                    return Optional.of(AccountMapper.fromResultSet(rs, owner));
                }
                return Optional.empty();
            }
        }
    }

    public List<Account> findByOwner(String ownerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE owner_id = ?";
        List<Account> list = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                Customer owner = new CustomerDAO().findById(c, ownerId);
                if (owner == null)throw new SQLException("Owner not found: " + ownerId);
                while (rs.next()) {
                    list.add(AccountMapper.fromResultSet(rs, owner));
                }
            }
        }
        return list;
    }

    public boolean updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            return ps.executeUpdate() == 1;
        }
    }

    // Helper: safely read a nullable double column (returns defaultValue if NULL)
    static double getDoubleOrDefault(ResultSet rs, String col, double defaultValue) throws SQLException {
        double v = rs.getDouble(col);
        return rs.wasNull() ? defaultValue : v;
    }
}
