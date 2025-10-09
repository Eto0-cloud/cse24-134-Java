public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    // Constructor
    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Business Logic
    public void register() {
        System.out.println("Customer registered: " + firstName + " " + lastName);
    }

    public Account openAccount(String type, double initialDeposit) {
        if (type.equalsIgnoreCase("savings")) {
            return new SavingsAccount(initialDeposit);
        } else if (type.equalsIgnoreCase("investment")) {
            return new InvestmentAccount(initialDeposit);
        } else {
            return new ChequeAccount(initialDeposit);
        }
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
