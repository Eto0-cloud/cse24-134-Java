public class InvestmentAccount extends Account implements InterestBearing {

    public InvestmentAccount(String accountNumber, double initialDeposit, String branch) {
        super(accountNumber, initialDeposit, branch);
        if (initialDeposit < 500) {
            throw new IllegalArgumentException("Minimum initial deposit is BWP500");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        }
    }

    @Override
    public void applyMonthlyInterest() {
        balance += balance * 0.05; // 5%
    }
}
