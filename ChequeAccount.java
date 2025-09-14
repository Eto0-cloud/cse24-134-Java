public class ChequeAccount extends Account {

    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double balance, String branch, String employer, String employerAddress) {
        super(accountNumber, balance, branch);
        this.employer = employer;
        this.employerAddress = employerAddress;
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
        // No interest for Cheque Account
    }
}
