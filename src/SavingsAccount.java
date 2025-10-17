public class SavingsAccount extends Account implements InterestBearing {
    private static final double MONTHLY_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(Customer owner, double initialDeposit, String branch) {
        super(owner, initialDeposit, branch);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.printf("[%s] Withdraw denied: Savings accounts do not allow withdrawals%n", getAccountNumber());
        return false;
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = getBalance() * MONTHLY_RATE;
        if (interest > 0) deposit(interest, "Monthly interest");
    }

    @Override
    public String getAccountType() { return "Savings"; }
}
