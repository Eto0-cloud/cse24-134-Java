import java.util.Scanner;
import java.util.List;

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create a customer
        System.out.print("Enter your first name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter your surname: ");
        String surname = sc.nextLine();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        Customer customer = new Customer(firstName, surname, address);

        // Menu loop
        boolean running = true;
        while (running) {
            System.out.println("\n==== BANK MENU ====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit into a Chosen Account");
            System.out.println("3. Show Accounts");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.println("\nChoose Account Type:");
                    System.out.println("1. Savings Account");
                    System.out.println("2. Investment Account");
                    System.out.println("3. Cheque Account");
                    int type = sc.nextInt();
                    sc.nextLine(); // Clear buffer

                    System.out.print("Enter Account Number: ");
                    String accNum = sc.nextLine();

                    System.out.print("Enter Initial Balance: ");
                    double balance = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter Branch: ");
                    String branch = sc.nextLine();

                    Account account = null;
                    if (type == 1) {
                        account = new SavingsAccount(accNum, balance, branch);
                    } else if (type == 2) {
                        try {
                            account = new InvestmentAccount(accNum, balance, branch);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    } else if (type == 3) {
                        System.out.print("Enter Employer: ");
                        String emp = sc.nextLine();
                        System.out.print("Enter Employer Address: ");
                        String empAddr = sc.nextLine();
                        account = new ChequeAccount(accNum, balance, branch, emp, empAddr);
                    } else {
                        System.out.println("Invalid account type.");
                        break;
                    }

                    if (account != null) {
                        customer.openAccount(account);
                        System.out.println("Account opened successfully!");
                    }
                    break;

                case 2:
                    List<Account> accounts = customer.getAccounts();
                    if (accounts.isEmpty()) {
                        System.out.println("You have no accounts to deposit into.");
                        break;
                    }

                    System.out.println("\nYour Accounts:");
                    for (int i = 0; i < accounts.size(); i++) {
                        Account acc = accounts.get(i);
                        System.out.println((i + 1) + ". " + acc.getAccountNumber() +
                                " (" + acc.getClass().getSimpleName() + ") - Balance: " + acc.getBalance());
                    }

                    System.out.print("Enter the number of the account to deposit into: ");
                    int accChoice = sc.nextInt();
                    if (accChoice < 1 || accChoice > accounts.size()) {
                        System.out.println("Invalid choice.");
                        break;
                    }

                    System.out.print("Enter deposit amount: ");
                    double depAmount = sc.nextDouble();
                    sc.nextLine();

                    Account selectedAccount = accounts.get(accChoice - 1);
                    selectedAccount.deposit(depAmount);
                    System.out.println("Deposit successful! New balance: " + selectedAccount.getBalance());
                    break;

                case 3:
                    customer.showAccounts();
                    break;

                case 4:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid menu option.");
            }
        }

        sc.close();
        System.out.println("Thank you for banking with us!");
    }
}
