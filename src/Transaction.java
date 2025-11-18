import java.time.Instant;

public class Transaction {
    private final long id;
    private final String accountNumber;
    private final String type;
    private final double amount;
    private final String description;
    private final Instant createdAt;

    public Transaction(long id, String accountNumber, String type, double amount, String description, Instant createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Transaction(String accountNumber, String type, double amount, String description) {
        this(0L, accountNumber, type, amount, description, Instant.now());
    }

    public long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
