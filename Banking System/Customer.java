public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

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
}
