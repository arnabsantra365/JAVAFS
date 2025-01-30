//
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class Transaction {
//    private String transactionId;
//    private String transactionType;
//    private double amount;
//    private String accountNumber;
//    private LocalDateTime timestamp;
//
//    Transaction(String transactionId,String transactionType,double amount,String accountNumber){
//        this.transactionId=transactionId;
//        this.transactionType=transactionType;
//        this.amount=amount;
//        this.accountNumber=accountNumber;
//        this.timestamp=LocalDateTime.now();
//    }
//    public void process() {
//        System.out.println("Transaction in progress..");
//        System.out.println("Transaction id="+transactionId);
//        System.out.println("Transaction type="+transactionType);
//        System.out.println("Transaction amount="+amount);
//        System.out.println("Timestamp:"+timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//    }
//}
import java.sql.*;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private String transactionType; // "Deposit", "Withdraw", or "Transfer"
    private double amount;
    private String accountNumber;
    private String targetAccountNumber; // Only for transfers
    private LocalDateTime timestamp;



    // Constructor for deposit/withdraw transactions
    public Transaction(String transactionType, double amount, String accountNumber) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for transfer transactions
    public Transaction(String transactionType, double amount, String accountNumber, String targetAccountNumber) {
        this(transactionType, amount, accountNumber);
        this.targetAccountNumber = targetAccountNumber;
    }

    // Process the transaction
    public void process(Bank bank) throws SQLException,TransactionException,InvalidAccountException{
        switch (transactionType) {
            case "Deposit":
                processDeposit(bank);
                break;
            case "Withdraw":
                processWithdraw(bank);
                break;
            case "Transfer":
                processTransfer(bank);
                break;
            default:
                throw new TransactionException("Invalid transaction type: " + transactionType);
        }
        logTransaction(); // Log the transaction after successful processing
    }

    // Handle deposit
    private void processDeposit(Bank bank) throws SQLException,InvalidAccountException,TransactionException {
        BankAccount account = bank.getAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            System.out.println("Deposit of " + amount + " to account " + accountNumber + " successful.");
        } else {
            throw new InvalidAccountException("Account not found: " + accountNumber);
        }
    }

    // Handle withdrawal
    private void processWithdraw(Bank bank) throws SQLException,InvalidAccountException,TransactionException{
        BankAccount account = bank.getAccount(accountNumber);
        if (account != null) {
            account.withdraw(amount);
            System.out.println("Withdrawal of " + amount + " from account " + accountNumber + " successful.");
        } else {
            throw new InvalidAccountException("Account not found: " + accountNumber);
        }
    }

    // Handle transfer
    private void processTransfer(Bank bank) throws SQLException,TransactionException,InvalidAccountException {
        if (targetAccountNumber == null || targetAccountNumber.isEmpty()) {
            throw new TransactionException("Target account number is required for transfer.");
        }

        BankAccount sourceAccount = bank.getAccount(accountNumber);
        BankAccount targetAccount = bank.getAccount(targetAccountNumber);

        if (sourceAccount == null) {
            throw new InvalidAccountException("Source account not found: " + accountNumber);
        }

        if (targetAccount == null) {
            throw new InvalidAccountException("Target account not found: " + targetAccountNumber);
        }

        sourceAccount.transfer(targetAccount, amount);
        System.out.println("Transfer of " + amount + " from account " + accountNumber + " to account " + targetAccountNumber + " successful.");
    }

    // Log the transaction in the database
    private void logTransaction() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO transactions (transaction_type, amount, account_number, timestamp) VALUES (?, ?, ?, ?)"
             )) {
            stmt.setString(1, transactionType);
            stmt.setDouble(2, amount);
            stmt.setString(3, accountNumber);
            stmt.setTimestamp(4, Timestamp.valueOf(timestamp));
            stmt.executeUpdate();

            // Log target account for transfers
            if ("Transfer".equals(transactionType)) {
                try (PreparedStatement targetStmt = conn.prepareStatement(
                        "INSERT INTO transactions (transaction_type, amount, account_number, timestamp) VALUES (?, ?, ?, ?)"
                )) {
                    targetStmt.setString(1, transactionType + " (Received)");
                    targetStmt.setDouble(2, amount);
                    targetStmt.setString(3, targetAccountNumber);
                    targetStmt.setTimestamp(4, Timestamp.valueOf(timestamp));
                    targetStmt.executeUpdate();
                }
            }
        }
    }
}
