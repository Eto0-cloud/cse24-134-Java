import java.sql.ResultSet;
import java.sql.SQLException;

public final class AccountMapper {

    public static Account fromResultSet(ResultSet rs, Customer owner) throws SQLException {
        String acctNo = rs.getString("account_number");
        String type = rs.getString("account_type");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");

        switch (type.toUpperCase()) {
            case "SAVINGS":
            case "SAVINGSACCOUNT":
                // package-private constructor used to set explicit account number & balance
                return new SavingsAccount(acctNo, owner, balance, branch);
            case "INVESTMENT":
            case "INVESTMENTACCOUNT":
                return new InvestmentAccount(acctNo, owner, balance, branch);
            case "CHEQUE":
            case "CHEQUEACCOUNT":
                String empName = rs.getString("employer_name");
                String empAddr = rs.getString("employer_address");
                double overdraft = rs.getDouble("overdraft_limit");
                return new ChequeAccount(acctNo, owner, balance, branch, empName, empAddr, overdraft);
            default:
                throw new SQLException("Unknown account type: " + type);
        }
    }
}
