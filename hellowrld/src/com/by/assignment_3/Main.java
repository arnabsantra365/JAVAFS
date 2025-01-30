//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        Bank bank = new Bank();
//
//        while (true) {
//            System.out.println("\n--- Banking System Menu ---");
//            System.out.println("1. Create a New Bank Account");
//            System.out.println("2. Create a New Savings Account");
//            System.out.println("3. Deposit Money");
//            System.out.println("4. Withdraw Money");
//            System.out.println("5. Transfer Funds");
//            System.out.println("6. Display Account Balance");
//            System.out.println("7. Display Transaction History");
//            System.out.println("8. Exit");
//            System.out.print("Enter your choice: ");
//            int choice = scanner.nextInt();
//
//            try {
//                switch (choice) {
//                    case 1 -> {
//                        System.out.print("Enter account number: ");
//                        String accountNumber = scanner.next();
//                        System.out.print("Enter account holder name: ");
//                        String accountHolderName = scanner.next();
//                        System.out.print("Enter initial balance: ");
//                        double balance = scanner.nextDouble();
//                        System.out.println("Type of account");
//                        String accountType = scanner.next();
//                        bank.addAccount(new BankAccount(accountNumber, accountHolderName, balance,accountType));
//                    }
//                    case 2 -> {
//                        System.out.print("Enter account number: ");
//                        String accountNumber = scanner.next();
//                        System.out.print("Enter account holder name: ");
//                        String accountHolderName = scanner.next();
//                        System.out.print("Enter initial balance: ");
//                        double balance = scanner.nextDouble();
//                        System.out.print("Enter interest rate: ");
//                        double interestRate = scanner.nextDouble();
////                        bank.addAccount(new SavingsAccount(accountNumber, accountHolderName, balance));
//                    }
//                    case 3 -> {
//                        System.out.print("Enter account number: ");
//                        String accountNumber = scanner.next();
//                        System.out.print("Enter deposit amount: ");
//                        double amount = scanner.nextDouble();
//                        bank.findAccount(accountNumber).deposit(amount);
//                    }
//                    case 4 -> {
//                        System.out.print("Enter account number: ");
//                        String accountNumber = scanner.next();
//                        System.out.print("Enter withdrawal amount: ");
//                        double amount = scanner.nextDouble();
//                        bank.findAccount(accountNumber).withdraw(amount);
//                    }
//                    case 5 -> {
//                        System.out.print("Enter source account number: ");
//                        String fromAccount = scanner.next();
//                        System.out.print("Enter target account number: ");
//                        String toAccount = scanner.next();
//                        System.out.print("Enter transfer amount: ");
//                        double amount = scanner.nextDouble();
//                        bank.processTransfer(fromAccount, toAccount, amount);
//                    }
//                    case 6 -> {
//                        System.out.print("Enter account number: ");
//                        String accountNumber = scanner.next();
//                        bank.findAccount(accountNumber).displayBalance();
//                    }
////                    case 7 -> {
////                        System.out.print("Enter account number: ");
////                        String accountNumber = scanner.next();
////                        bank.findAccount(accountNumber).displayTransactionHistory();
////                    }
//                    case 8 -> {
//                        System.out.println("Exiting the system.");
//                        scanner.close();
//                        return;
//                    }
//                    default -> System.out.println("Invalid choice. Please try again.");
//                }
//            } catch (Exception e) {
//                System.out.println("Error: " + e.getMessage());
//            }
//        }
//    }
//}
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        TransactionProcessor processor = new TransactionProcessor();

        // Load accounts from the database at the start
        try {
            bank.loadAccountsFromDB();
        } catch (SQLException e) {
            System.err.println("Error loading accounts from database: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Create a New Bank Account");
            System.out.println("2. Create a New Savings Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Funds");
            System.out.println("6. Display Account Balance");
            System.out.println("7. Display Transaction History");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> createBankAccount(bank, scanner);
                case 2 -> createSavingsAccount(bank, scanner);
                case 3 -> depositMoney(bank, processor, scanner);
                case 4 -> withdrawMoney(bank, processor, scanner);
                case 5 -> transferFunds(bank, processor, scanner);
                case 6 -> displayAccountBalance(bank, scanner);
                case 7 -> displayTransactionHistory(processor, scanner);
                case 8 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    processor.shutdown();
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createBankAccount(Bank bank, Scanner scanner) {
        System.out.println("\n--- Create New Bank Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Account Holder's Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Account Type (Saving/Current): ");
        String type = scanner.nextLine();

        try {
            bank.addAccount(new BankAccount(accountNumber, name, balance, type));
            System.out.println("Bank account created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
    }

    private static void createSavingsAccount(Bank bank, Scanner scanner) {
        System.out.println("\n--- Create New Savings Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Account Holder's Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Balance (minimum 1000): ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (balance < 1000) {
            System.out.println("Error: Savings account requires a minimum balance of 1000.");
            return;
        }

        try {
            bank.addAccount(new BankAccount(accountNumber, name, balance, "Saving"));
            System.out.println("Savings account created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating savings account: " + e.getMessage());
        }
    }

    private static void depositMoney(Bank bank, TransactionProcessor processor, Scanner scanner) {
        System.out.println("\n--- Deposit Money ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Deposit Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        processor.processTransaction(() -> {
            try {
                BankAccount account = bank.getAccount(accountNumber);
                if (account != null) {
//                    account.deposit(amount);
                    Transaction transaction = new Transaction("Deposit",  amount,accountNumber);
                    transaction.process(bank);
                    System.out.println("Deposit successful!");
                } else {
                    System.out.println("Account not found.");
                }
            } catch (SQLException | InvalidAccountException | TransactionException e) {
                System.err.println("Error processing deposit: " + e.getMessage());
            }
        });
    }

    private static void withdrawMoney(Bank bank, TransactionProcessor processor, Scanner scanner){
        System.out.println("\n--- Withdraw Money ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Withdrawal Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        processor.processTransaction(() -> {
            try {
                BankAccount account = bank.getAccount(accountNumber);
                if (account != null) {
//                    account.withdraw(amount);
                    Transaction transaction = new Transaction("Withdraw",  amount, accountNumber);
                    transaction.process(bank);
                    System.out.println("Withdrawal successful!");
                } else {
                    System.out.println("Account not found.");
                }
            } catch (SQLException | TransactionException | InvalidAccountException e) {
                System.err.println("Error processing withdrawal: " + e.getMessage());
            }
        });
    }

    private static void transferFunds(Bank bank, TransactionProcessor processor, Scanner scanner) {
        System.out.println("\n--- Transfer Funds ---");
        System.out.print("Enter Source Account Number: ");
        String sourceAccountNumber = scanner.nextLine();
        System.out.print("Enter Target Account Number: ");
        String targetAccountNumber = scanner.nextLine();
        System.out.print("Enter Transfer Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        processor.processTransaction(() -> {
            try {
                BankAccount sourceAccount = bank.getAccount(sourceAccountNumber);
                BankAccount targetAccount = bank.getAccount(targetAccountNumber);

                if (sourceAccount != null && targetAccount != null) {
//                    sourceAccount.transfer(targetAccount, amount);
                    Transaction transaction = new Transaction("Transfer",  amount, sourceAccountNumber,targetAccountNumber);
                    transaction.process(bank);
                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("Invalid source or target account.");
                }
            } catch (SQLException  | TransactionException | InvalidAccountException e) {
                System.err.println("Error processing transfer: " + e.getMessage());
            }
        });
    }

    private static void displayAccountBalance(Bank bank, Scanner scanner) {
        System.out.println("\n--- Display Account Balance ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        try {
            BankAccount account = bank.getAccount(accountNumber);
            if (account != null) {
                System.out.println("Account Balance: " + account.getBalance());
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException | InvalidAccountException e) {
            System.err.println("Error retrieving account balance: " + e.getMessage());
        }
    }

    private static void displayTransactionHistory(TransactionProcessor processor, Scanner scanner) {
        System.out.println("\n--- Display Transaction History ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY timestamp ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("\n--- Transaction History for Account: " + accountNumber + " ---");
            boolean hasTransactions = false;

            while (resultSet.next()) {
                hasTransactions = true;
                String transactionId = resultSet.getString("transaction_id");
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                String timestamp = resultSet.getString("timestamp");

                System.out.printf("ID: %s | Type: %s | Amount: %.2f | Timestamp: %s%n",
                        transactionId, transactionType, amount, timestamp);
            }

            if (!hasTransactions) {
                System.out.println("No transactions found for this account.");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction history: " + e.getMessage());
        }
    }

}


