import java.util.concurrent.atomic.AtomicInteger;

public abstract class Account {
    private static final AtomicInteger NEXT = new AtomicInteger(1000);
  
protected static String generateAccountNumber() {
    return String.valueOf(NEXT.getAndIncrement());
}

    private final String accountNumber;
    protected double balance;          // protected so subclasses can adjust for overdraft
    private final String branch;
    private final Customer owner;

    /**
     * Public constructor used when creating a new account in code.
     * Generates a new account number automatically.
     */
    public Account(Customer owner, double initialDeposit, String branch) {
        this(String.valueOf(NEXT.getAndIncrement()), owner, initialDeposit, branch);
    }

    Account(String accountNumber, Customer owner, double initialDeposit, String branch) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("accountNumber cannot be null or blank");
        }
        if (owner == null) throw new IllegalArgumentException("Owner cannot be null");
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.branch = branch;
        this.balance = 0.0;
        if (initialDeposit > 0) deposit(initialDeposit);
    }

    // package-private setter for DAOs if direct balance manipulation is required
    void setBalanceInternal(double newBalance) {
        this.balance = newBalance;
    }

    // Overloaded deposit methods
    public void deposit(double amount) { deposit(amount, "Deposit"); }
    public void deposit(double amount, String description) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        balance += amount;
        System.out.printf("[%s] %s +%.2f -> balance %.2f%n", accountNumber, description, amount, balance);
    }

    // Default withdraw (subclasses may override)
    public boolean withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw must be positive");
        if (amount > balance) {
            System.out.printf("[%s] Withdraw failed: insufficient funds (requested %.2f, balance %.2f)%n",
                    accountNumber, amount, balance);
            return false;
        }
        balance -= amount;
        System.out.printf("[%s] Withdraw -%.2f -> balance %.2f%n", accountNumber, amount, balance);
        return true;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }

    @Override
    public String toString() {
        return String.format("%s[%s] owner=%s balance=%.2f branch=%s",
                getAccountType(), accountNumber, owner.getCustomerId(), balance, branch);
    }

    public abstract String getAccountType();
}
