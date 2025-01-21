package com.by.assignment_3;

import java.time.LocalDateTime;

public class Transaction {
	private String transactionId;
	private String transactionType;
	private double amount;
	private String accountNumber;
	private LocalDateTime timestamp;
	
	Transaction(String transactionId,String transactionType,double amount,String accountNumber,LocalDateTime timestamp){
		this.transactionId=transactionId;
		this.transactionType=transactionType;
		this.amount=amount;
		this.accountNumber=accountNumber;
		this.timestamp=timestamp;
	}
	public void process() {
		System.out.println("Transaction in progress..");
		System.out.println("Transaction id="+transactionId);
		System.out.println("Transaction type="+transactionType);
		System.out.println("Transaction amount="+amount);
		System.out.println("Timestamp:"+timestamp.format(DateTimeFormatter));
	}
}
