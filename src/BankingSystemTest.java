import java.util.Scanner;

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank("Demo Bank");
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1 Register customer");
            System.out.println("2 Open account");
            System.out.println("3 Deposit");
            System.out.println("4 Withdraw");
            System.out.println("5 Apply monthly interest");
            System.out.println("6 List customers and accounts");
            System.out.println("7 Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("First name: "); String f = sc.nextLine().trim();
                        System.out.print("Last name: ");  String l = sc.nextLine().trim();
                        System.out.print("Address: ");    String addr = sc.nextLine().trim();
                        bank.registerCustomer(f, l, addr);
                    }
                    case "2" -> {
                        System.out.print("Customer id or 'list': ");
                        String id = sc.nextLine().trim();
                        if (id.equalsIgnoreCase("list")) {
                            for (Customer c : bank.getAllCustomers()) System.out.printf("%s : %s%n", c.getCustomerId(), c.getFullName());
                            System.out.print("Customer id: ");
                            id = sc.nextLine().trim();
                        }
                        Customer cust = bank.findCustomerById(id);
                        if (cust == null) { System.out.println("Customer not found"); break; }
                        System.out.println("a) Savings b) Investment c) Cheque");
                        System.out.print("Type: "); String t = sc.nextLine().trim();
                        System.out.print("Initial deposit: "); double init = Double.parseDouble(sc.nextLine().trim());
                        if (t.equalsIgnoreCase("a")) bank.openSavingsAccount(cust, init, "Main");
                        else if (t.equalsIgnoreCase("b")) bank.openInvestmentAccount(cust, init, "Main");
                        else if (t.equalsIgnoreCase("c")) {
                            System.out.print("Employer name: "); String en = sc.nextLine().trim();
                            System.out.print("Employer address: "); String ea = sc.nextLine().trim();
                            System.out.print("Overdraft limit: "); double od = Double.parseDouble(sc.nextLine().trim());
                            bank.openChequeAccount(cust, init, "Main", en, ea, od);
                        } else System.out.println("Invalid type");
                    }
                    case "3" -> {
                        System.out.print("Customer id: "); String id = sc.nextLine().trim();
                        Customer cust = bank.findCustomerById(id);
                        if (cust == null) { System.out.println("Customer not found"); break; }
                        if (cust.getAccounts().isEmpty()) { System.out.println("No accounts"); break; }
                        for (Account a : cust.getAccounts()) System.out.printf("%s %s %.2f%n", a.getAccountNumber(), a.getAccountType(), a.getBalance());
                        System.out.print("Account number: "); String acc = sc.nextLine().trim();
                        Account a = cust.findAccountByNumber(acc);
                        if (a == null) { System.out.println("Account not found"); break; }
                        System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine().trim());
                        a.deposit(amt, "User deposit");
                    }
                    case "4" -> {
                        System.out.print("Customer id: "); String id = sc.nextLine().trim();
                        Customer cust = bank.findCustomerById(id);
                        if (cust == null) { System.out.println("Customer not found"); break; }
                        if (cust.getAccounts().isEmpty()) { System.out.println("No accounts"); break; }
                        for (Account a : cust.getAccounts()) System.out.printf("%s %s %.2f%n", a.getAccountNumber(), a.getAccountType(), a.getBalance());
                        System.out.print("Account number: "); String acc = sc.nextLine().trim();
                        Account a = cust.findAccountByNumber(acc);
                        if (a == null) { System.out.println("Account not found"); break; }
                        System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine().trim());
                        boolean ok = a.withdraw(amt);
                        System.out.println(ok ? "Withdraw successful" : "Withdraw failed");
                    }
                    case "5" -> {
                        bank.applyMonthlyInterests();
                        System.out.println("Monthly interest applied.");
                    }
                    case "6" -> {
                        for (Customer c : bank.getAllCustomers()) {
                            System.out.printf("%s - %s (%s)%n", c.getCustomerId(), c.getFullName(), c.getAddress());
                            if (c.getAccounts().isEmpty()) System.out.println("  (no accounts)");
                            else for (Account acc : c.getAccounts()) {
                                String extra = acc instanceof ChequeAccount ? " Employer:" + ((ChequeAccount)acc).getEmployerName() + " OD:" + ((ChequeAccount)acc).getOverdraftLimit() : "";
                                System.out.printf("  - %s %s : %.2f%s%n", acc.getAccountType(), acc.getAccountNumber(), acc.getBalance(), extra);
                            }
                        }
                    }
                    case "7" -> { running = false; System.out.println("Goodbye."); }
                    default -> System.out.println("Invalid option");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number entered.");
            } catch (IllegalArgumentException iae) {
                System.out.println("Error: " + iae.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
        sc.close();
    }
}
