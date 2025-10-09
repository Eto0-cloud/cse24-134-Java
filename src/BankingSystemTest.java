public class BankingSystemTest {
    public static void main(String[] args) {
        // Create a customer
        Customer customer = new Customer("Katlego", "Mokoena", "katlego@example.com");
        System.out.println("Customer created: " + customer.getFirstName() + " " + customer.getLastName());

        // Open a savings account
        Account savings = customer.openAccount("savings", 1000.0);
        System.out.println("Savings Account opened with balance: " + savings.getBalance());

        // Deposit funds
        savings.deposit(500.0);
        System.out.println("After deposit, balance: " + savings.getBalance());

        // Withdraw funds
        savings.withdraw(300.0);
        System.out.println("After withdrawal, balance: " + savings.getBalance());

        // Calculate interest (only if account supports it)
        if (savings instanceof InterestBearing) {
            double interest = ((InterestBearing) savings).calculateInterest();
            System.out.println("Calculated interest: " + interest);
        }

        // Open a cheque account
        Account cheque = customer.openAccount("cheque", 200.0);
        cheque.deposit(100.0);
        cheque.withdraw(50.0);
        System.out.println("Cheque Account final balance: " + cheque.getBalance());

        // Open an investment account
        Account investment = customer.openAccount("investment", 5000.0);
        if (investment instanceof InterestBearing) {
            double interest = ((InterestBearing) investment).calculateInterest();
            System.out.println("Investment Account interest: " + interest);
        }
    }
}
