public class ChequeAccount extends Account {
    private final String employerName;
    private final String employerAddress;
    private final double overdraftLimit;

    public ChequeAccount(String accountNumber, Customer owner, double initialDeposit, String branch,
                         String employerName, String employerAddress, double overdraftLimit) {
        super(accountNumber, owner, initialDeposit, branch);
        if (employerName == null || employerName.isBlank()) throw new IllegalArgumentException("Employer name required");
        if (employerAddress == null || employerAddress.isBlank()) throw new IllegalArgumentException("Employer address required");
        this.employerName = employerName;
        this.employerAddress = employerAddress;
        this.overdraftLimit = Math.max(0.0, overdraftLimit);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw must be positive");
        double available = balance + overdraftLimit;
        if (amount > available) {
            System.out.printf("[%s] Withdraw failed: exceeds available (requested %.2f, available %.2f)%n", getAccountNumber(), amount, available);
            return false;
        }
        balance -= amount; // may go negative up to overdraft limit
        System.out.printf("[%s] Withdraw -%.2f -> balance %.2f%n", getAccountNumber(), amount, balance);
        return true;
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
    public double getOverdraftLimit() { return overdraftLimit; }

    @Override
    public String getAccountType() { return "Cheque"; }
}
