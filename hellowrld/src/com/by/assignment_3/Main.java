package com.by.assignment_3;
import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Bank bank = new Bank();
	        Scanner scanner = new Scanner(System.in);

	        while (true) {
	            System.out.println("\n--- Banking System Menu ---");
	            System.out.println("1. Create a New Bank Account");
	            System.out.println("2. Create a New Savings Account");
	            System.out.println("3. Deposit Money");
	            System.out.println("4. Withdraw Money");
	            System.out.println("5. Transfer Funds");
	            System.out.println("6. Display Account Balance");
	            System.out.println("7. Calculate Interest (Savings Account)");
	            System.out.println("8. Exit");
	            System.out.print("Enter your choice: ");

	            int choice = scanner.nextInt();
	            try {
	                switch (choice) {
	                    case 1:
	                        System.out.print("Enter Account Number: ");
	                        String accountNumber = scanner.next();
	                        System.out.print("Enter Account Holder Name: ");
	                        String accountHolder = scanner.next();
	                        System.out.print("Enter Initial Balance: ");
	                        double balance = scanner.nextDouble();
	                        bank.addAccount(new BankAccount(accountNumber, accountHolder, balance));
	                        break;

	                    case 2:
	                        System.out.print("Enter Account Number: ");
	                        accountNumber = scanner.next();
	                        System.out.print("Enter Account Holder Name: ");
	                        accountHolder = scanner.next();
	                        System.out.print("Enter Initial Balance: ");
	                        balance = scanner.nextDouble();
	                        System.out.print("Enter Interest Rate: ");
//	                        double interestRate = scanner.nextDouble();
	                        bank.addAccount(new SavingsAccount(accountNumber, accountHolder, balance));
	                        break;

	                    case 3:
	                        System.out.print("Enter Account Number: ");
	                        accountNumber = scanner.next();
	                        System.out.print("Enter Deposit Amount: ");
	                        double depositAmount = scanner.nextDouble();
	                        bank.findAccount(accountNumber).deposit(depositAmount);
	                        break;

	                    case 4:
	                        System.out.print("Enter Account Number: ");
	                        accountNumber = scanner.next();
	                        System.out.print("Enter Withdrawal Amount: ");
	                        double withdrawAmount = scanner.nextDouble();
	                        bank.findAccount(accountNumber).withdraw(withdrawAmount);
	                        break;

	                    case 5:
	                        System.out.print("Enter Source Account Number: ");
	                        String fromAccount = scanner.next();
	                        System.out.print("Enter Target Account Number: ");
	                        String toAccount = scanner.next();
	                        System.out.print("Enter Transfer Amount: ");
	                        double transferAmount = scanner.nextDouble();
	                        bank.transferFunds(fromAccount, toAccount, transferAmount);
	                        break;

	                    case 6:
	                        System.out.print("Enter Account Number: ");
	                        accountNumber = scanner.next();
	                        bank.findAccount(accountNumber).displayBalance();
	                        break;

	                    case 7:
	                        System.out.print("Enter Account Number: ");
	                        accountNumber = scanner.next();
	                        BankAccount account = bank.findAccount(accountNumber);
	                        if (account instanceof SavingsAccount) {
	                            ((SavingsAccount) account).calculateInterest();
	                        } else {
	                            System.out.println("This is not a savings account.");
	                        }
	                        break;

	                    case 8:
	                        System.out.println("Exiting the system. Goodbye!");
	                        scanner.close();
	                        System.exit(0);

	                    default:
	                        System.out.println("Invalid option. Please try again.");
	                }
	            } catch (AccNotFoundException | TransactionException e) {
	                System.out.println("Error: " + e.getMessage());
	            }
	        }
	    

	}

}
