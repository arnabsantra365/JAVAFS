package com.by.assignment_3;

import java.util.HashMap;

public class Bank {
    private HashMap<String, BankAccount> accounts = new HashMap<>();

    public void addAccount(BankAccount account) {
        accounts.put(account.getaccNum(), account);
        System.out.println("Account added successfully: " + account.getaccNum());
    }

    public void removeAccount(String accountNumber) throws AccNotFoundException {
        if (!accounts.containsKey(accountNumber)) {
            throw new AccNotFoundException("Account with number " + accountNumber + " not found.");
        }
        accounts.remove(accountNumber);
        System.out.println("Account removed: " + accountNumber);
    }

    public BankAccount findAccount(String accountNumber) throws AccNotFoundException {
        if (!accounts.containsKey(accountNumber)) {
            throw new AccNotFoundException("Account with number " + accountNumber + " not found.");
        }
        return accounts.get(accountNumber);
    }

    public void transferFunds(String fromAccountNumber, String toAccountNumber, double amount) 
            throws AccNotFoundException, TransactionException {
        BankAccount fromAccount = findAccount(fromAccountNumber);
        BankAccount toAccount = findAccount(toAccountNumber);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        System.out.println("Transferred $" + amount + " from " + fromAccountNumber + " to " + toAccountNumber);
    }
}
