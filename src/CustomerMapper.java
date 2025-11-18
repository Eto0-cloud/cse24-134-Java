import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {
    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("customer_id");
        String first = rs.getString("first_name");
        String last = rs.getString("last_name");
        String address = rs.getString("address");

        // Use a special constructor to preserve the original customerId
        return new Customer(id, first, last, address);
    }
}
