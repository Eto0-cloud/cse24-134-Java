public class InvestmentAccount extends Account implements InterestBearing {
    private static final double MONTHLY_RATE = 0.05; // 5% monthly
    public static final double MIN_INITIAL = 500.0;

    public InvestmentAccount(String accountNumber, Customer owner, double initialDeposit, String branch) {
        super(accountNumber, owner, 0.0, branch);
        if (initialDeposit < MIN_INITIAL) throw new IllegalArgumentException("Investment requires minimum initial deposit of BWP500.00");
        deposit(initialDeposit, "Initial deposit (Investment)");
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = getBalance() * MONTHLY_RATE;
        if (interest > 0) deposit(interest, "Monthly interest");
    }

    @Override
    public String getAccountType() { return "Investment"; }
}
