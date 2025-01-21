package com.by.assignment_3;

public class BankAccount {
	private String accNum;
	private String accName;
	private double balance;
	
	public BankAccount(String num,String name,double bal) {
		accNum = num;
		accName = name;
		balance = bal;
	}
	
	public String getaccNum() {
		return accNum;
	}
	public String getaccName() {
		return accName;
	}
	public double getBalance() {
		return balance;
	}
	
	public void deposit(double amount) throws TransactionException{
		if(amount<=0) {
			throw new TransactionException("Enter a valid deposit");
		}
		else {
			balance+=amount;
			System.out.println("Amount deposited="+amount);
		}
	}
	
	public void withdraw(double amount) throws TransactionException{
		if(amount<=0 || amount>balance) {
			throw new TransactionException("Enter a valid withdrawal");
		}
		else {
			balance-=amount;
			System.out.println("Amount withdrawn="+amount);
		}
	}
	
	public void displayBalance() {
		System.out.println("Current balance="+balance);
	}
	
	
}
