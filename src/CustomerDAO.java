import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void createTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS customers (
              customer_id VARCHAR(20) PRIMARY KEY,
              first_name VARCHAR(100),
              last_name VARCHAR(100),
              address VARCHAR(255)
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insert(Connection conn, Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(customer_id, first_name, last_name, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        }
    }

    public Customer findById(Connection conn, String customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return CustomerMapper.fromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Customer> findAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(CustomerMapper.fromResultSet(rs));
            }
        }
        return list;
    }

    public void updateAddress(Connection conn, String customerId, String newAddress) throws SQLException {
        String sql = "UPDATE customers SET address = ? WHERE customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setString(2, customerId);
            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            ps.executeUpdate();
        }
    }
}
