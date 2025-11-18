import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public final class TransactionMapper {

    public static Transaction fromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String acct = rs.getString("account_number");
        String type = rs.getString("type");
        double amount = rs.getDouble("amount");
        String desc = rs.getString("description");
        Timestamp ts = rs.getTimestamp("created_at");
        Instant created = (ts == null) ? Instant.now() : ts.toInstant();
        return new Transaction(id, acct, type, amount, desc, created);
    }
}
