import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstName;
    private String surname;
    private String address;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String firstName, String surname, String address) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
    }

    public void openAccount(Account account) {
        accounts.add(account);
    }
    
    public List<Account> getAccounts() {
       return accounts;
    }

    public void showAccounts() {
        for (Account acc : accounts) {
            System.out.println(acc.getAccountNumber() + " - Balance: " + acc.getBalance());
        }
    }

    public void applyInterestToAll() {
        for (Account acc : accounts) {
            acc.applyMonthlyInterest();
        }
    }
}
