public abstract class Account {
    private String accountNumber;
    protected double balance;
    private String branch;

    public Account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        }
    }

    public abstract void withdraw(double amount);
    public abstract void applyMonthlyInterest();

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}
