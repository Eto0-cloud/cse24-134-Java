import java.util.*;

public class Bank {
    private final String name;
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public Bank(String name) { this.name = name; }

    public String getName() { return name; }

    public Customer registerCustomer(String firstName, String lastName, String address) {
        Customer c = new Customer(firstName, lastName, address);
        customers.put(c.getCustomerId(), c);
        return c;
    }

    public SavingsAccount openSavingsAccount(Customer c, double initialDeposit, String branch) {
        SavingsAccount a = new SavingsAccount(c, initialDeposit, branch);
        c.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    public InvestmentAccount openInvestmentAccount(Customer c, double initialDeposit, String branch) {
        InvestmentAccount a = new InvestmentAccount(c, initialDeposit, branch);
        c.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    public ChequeAccount openChequeAccount(Customer c, double initialDeposit, String branch,
                                          String employerName, String employerAddress, double overdraftLimit) {
        ChequeAccount a = new ChequeAccount(c, initialDeposit, branch, employerName, employerAddress, overdraftLimit);
        c.addAccount(a);
        accounts.put(a.getAccountNumber(), a);
        return a;
    }

    public void applyMonthlyInterests() {
        for (Account a : accounts.values()) {
            if (a instanceof InterestBearing) ((InterestBearing) a).applyMonthlyInterest();
        }
    }

    public Collection<Customer> getAllCustomers() { return customers.values(); }
    public Customer findCustomerById(String id) { return customers.get(id); }
    public Account findAccountByNumber(String accNo) { return accounts.get(accNo); }
}
