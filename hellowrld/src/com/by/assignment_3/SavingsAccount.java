package com.by.assignment_3;

public class SavingsAccount extends BankAccount {
	public double interestRate=0.05;
	
	public SavingsAccount(String num,String name,double bal) {
		super(num,name,bal);
	}
	
	public void withdraw(double amount) throws TransactionException{
		double fee = 5.0;
		if(amount<=50) {
			amount+=fee;
			System.out.println("You are charged $5");
		}
		if(amount+fee>=getBalance()) {
			throw new TransactionException("Insufficient funds");
		}
		super.withdraw(amount);
		
	}
	
	public void calculateInterest() {
		double value=getBalance()*interestRate;
		System.out.println("The total interest="+value);
	}
}
