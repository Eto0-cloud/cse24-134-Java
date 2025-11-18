import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer {
    private static final AtomicInteger NEXT = new AtomicInteger(1);
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private String address;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String firstName, String lastName, String address) {
        this.customerId = String.valueOf(NEXT.getAndIncrement());
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
    
    // Constructor used by DAOs / mappers when loading from the database (preserve id)
    public Customer(String customerId, String firstName, String lastName, String address) {
        if (customerId == null) throw new IllegalArgumentException("customerId cannot be null");
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;

        // If the stored id is numeric, advance NEXT to avoid generating duplicates
        try {
            int idnum = Integer.parseInt(customerId);
            NEXT.updateAndGet(curr -> Math.max(curr, idnum + 1));
        } catch (NumberFormatException ignored) {
            // non-numeric ids are allowed; generated numeric ids will continue unaffected
        }
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public void addAccount(Account a) {
        if (a == null) throw new IllegalArgumentException("Account cannot be null");
        accounts.add(a);
    }

    public List<Account> getAccounts() { return Collections.unmodifiableList(accounts); }

    public Account findAccountByNumber(String accNo) {
        for (Account a : accounts) if (a.getAccountNumber().equals(accNo)) return a;
        return null;
    }
}
