//import java.util.HashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Bank {
//    private HashMap<String, BankAccount> accounts = new HashMap<>();
//    private ExecutorService executorService;
//    public void addAccount(BankAccount account) {
//        accounts.put(account.getaccNum(), account);
//        System.out.println("Account added successfully: " + account.getaccNum());
//    }
//
//    public void removeAccount(String accountNumber) throws AccNotFoundException {
//        if (!accounts.containsKey(accountNumber)) {
//            throw new AccNotFoundException("Account with number " + accountNumber + " not found.");
//        }
//        accounts.remove(accountNumber);
//        System.out.println("Account removed: " + accountNumber);
//    }
//    public void processDeposit(String accountNumber, double amount) {
//        executorService.submit(() -> {
//            try {
//                BankAccount account = findAccount(accountNumber);
//                account.deposit(amount);
//            } catch (Exception e) {
//                System.out.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
//            }
//        });
//    }
//    public void processWithdrawal(String accountNumber, double amount) {
//        executorService.submit(() -> {
//            try {
//                BankAccount account = findAccount(accountNumber);
//                account.withdraw(amount);
//            } catch (Exception e) {
//                System.out.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
//            }
//        });
//    }
//
//
//    public BankAccount findAccount(String accountNumber) throws AccNotFoundException {
//        if (!accounts.containsKey(accountNumber)) {
//            throw new AccNotFoundException("Account with number " + accountNumber + " not found.");
//        }
//        return accounts.get(accountNumber);
//    }
//
////    public void transferFunds(String fromAccountNumber, String toAccountNumber, double amount)
////            throws AccNotFoundException, TransactionException {
////        BankAccount fromAccount = findAccount(fromAccountNumber);
////        BankAccount toAccount = findAccount(toAccountNumber);
////
////        fromAccount.withdraw(amount);
////        toAccount.deposit(amount);
//////        String transactionID = "TXN" + System.currentTimeMillis();
//////        fromAccount.displayTransactionHistory().add(new Transaction(transactionID, "Transfer Out", amount, fromAccountNumber));
//////        toAccount.displayTransactionHistory().add(new Transaction(transactionID, "Transfer In", amount, toAccountNumber));
////
////        System.out.println("Transferred $" + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
////    }
////public void processTransfer(String fromAccountNumber, String toAccountNumber, double amount) {
////    executorService.submit(() -> {
////        try {
////            BankAccount fromAccount = findAccount(fromAccountNumber);
////            BankAccount toAccount = findAccount(toAccountNumber);
////
////            synchronized (fromAccount) {
////                synchronized (toAccount) {
////                    fromAccount.withdraw(amount);
////                    toAccount.deposit(amount);
//////                    BankAccount.setTransaction(amount,fromAccountNumber,toAccountNumber);
//////                            String transactionID = "TXN" + System.currentTimeMillis();
//////                            fromAccount.displayTransactionHistory().add(new Transaction(transactionID, "Transfer Out", amount, fromAccountNumber));
//////                            toAccount.displayTransactionHistory().add(new Transaction(transactionID, "Transfer In", amount, toAccountNumber));
////                }
////            }
////            System.out.println(Thread.currentThread().getName() + " - Transfer successful: $" + amount +
////                    " from " + fromAccountNumber + " to " + toAccountNumber);
////        } catch (Exception e) {
////            System.out.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
////        }
////    });
////}
//    public void shutdown() {
//        executorService.shutdown();
//        System.out.println("Bank system shutting down.");
//    }
//}
import java.sql.*;
import java.util.HashMap;

public class Bank {
    private HashMap<String, BankAccount> accountsMap = new HashMap<>();


    // Load accounts from the database
    public void loadAccountsFromDB() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {
            while (rs.next()) {
                BankAccount account = new BankAccount(
                        rs.getString("account_number"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getString("account_type")

                );
                accountsMap.put(account.getaccNum(), account);
            }
        }
    }

    // Add a new account
    public void addAccount(BankAccount account) throws SQLException {
        accountsMap.put(account.getaccNum(), account);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts (account_number, account_holder_name, account_type, balance) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, account.getaccNum());
            stmt.setString(2, account.getaccName());
            stmt.setString(3, account.getAccType());
            stmt.setDouble(4, account.getBalance());


            stmt.executeUpdate();
        }
    }

    // Get an account by account number
    public BankAccount getAccount(String accountNumber) throws SQLException,InvalidAccountException {
        if (!accountsMap.containsKey(accountNumber)) {
            accountsMap.put(accountNumber, BankAccount.loadFromDB(accountNumber));
        }
        return accountsMap.get(accountNumber);
    }
}
