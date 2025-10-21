import java.util.concurrent.atomic.AtomicInteger;

public abstract class Account {
    private static final AtomicInteger NEXT = new AtomicInteger(1000);
    private final String accountNumber;
    protected double balance;          // protected so subclasses (e.g., Cheque) can adjust for overdraft
    private final String branch;
    private final Customer owner;

    public Account(Customer owner, double initialDeposit, String branch) {
        if (owner == null) throw new IllegalArgumentException("Owner cannot be null");
        this.accountNumber = String.valueOf(NEXT.getAndIncrement());
        this.owner = owner;
        this.branch = branch;
        this.balance = 0.0;
        if (initialDeposit > 0) deposit(initialDeposit);
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
            System.out.printf("[%s] Withdraw failed: insufficient funds (requested %.2f, balance %.2f)%n", accountNumber, amount, balance);
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

    public abstract String getAccountType();
}
