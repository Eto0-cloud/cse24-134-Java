public class SavingsAccount extends Account implements IInterestBearing {
    public SavingsAccount(double initialDeposit) {
        super(initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) balance -= amount;
    }

    @Override
    public double calculateInterest() {
        return balance * 0.03;
    }
}
