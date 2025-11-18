import java.util.*;

public class Bank {
    private final String name;
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public Bank(String name) { this.name = Objects.requireNonNull(name); }

    public String getName() { return name; }

    // Register a new customer (generates id using Customer's auto-id ctor)
    public Customer registerCustomer(String firstName, String lastName, String address) {
        Customer c = new Customer(firstName, lastName, address);
        customers.put(c.getCustomerId(), c);
        return c;
    }

    // Register an existing customer loaded from DB (preserve id)
    public Customer registerCustomer(Customer existing) {
        customers.put(existing.getCustomerId(), existing);
        return existing;
    }

    // Open accounts — use convenience ctors on Account subclasses
    public SavingsAccount openSavingsAccount(String accountNumber, Customer owner, double initialDeposit, String branch) {
        SavingsAccount a = new SavingsAccount(accountNumber, owner, initialDeposit, branch);
        owner.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    public InvestmentAccount openInvestmentAccount(String accountNumber, Customer owner, double initialDeposit, String branch) {
        InvestmentAccount a = new InvestmentAccount(accountNumber, owner, initialDeposit, branch);
        owner.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    public ChequeAccount openChequeAccount(String accountNumber, Customer owner, double initialDeposit, String branch,
                                           String employerName, String employerAddress, double overdraftLimit) {
        ChequeAccount a = new ChequeAccount(accountNumber, owner, initialDeposit, branch, employerName, employerAddress, overdraftLimit);
        owner.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    // Business operations
    public boolean deposit(String accountNumber, double amount, String description) {
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        a.deposit(amount, description);
        return true;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        return a.withdraw(amount);
    }

    public void applyMonthlyInterests() {
        for (Account a : accounts.values()) {
            if (a instanceof InterestBearing) ((InterestBearing) a).applyMonthlyInterest();
        }
    }

    // Queries
    public Collection<Customer> getAllCustomers() { return Collections.unmodifiableCollection(customers.values()); }
    public Customer findCustomerById(String id) { return customers.get(id); }
    public Account findAccountByNumber(String accNo) { return accounts.get(accNo); }

    // Synchronize with persistence layer (simple helpers)
    public void loadCustomerAndAccounts(Customer customer, Collection<Account> acctList) {
        // register the customer and add accounts that came from DB
        registerCustomer(customer);
        for (Account a : acctList) {
            accounts.put(a.getAccountNumber(), a);
            customer.addAccount(a);
        }
    }
}
