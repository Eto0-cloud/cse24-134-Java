public class SavingsAccount extends Account implements InterestBearing {

    public SavingsAccount(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed from Savings Account.");
    }

    @Override
    public void applyMonthlyInterest() {
        balance += balance * 0.0005; // 0.05%
    }
}
