package com.blueyonder.hellowrld;
import java.util.Scanner;

public class Banking {
	static double balance = 0.0;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int choice;
		while(true) {
		System.out.println("Select from the menu below:-");
		System.out.println("1.Deposit:-");
		System.out.println("2.Withdrawal:-");
		System.out.println("3.Check Balance:-");
		System.out.println("4.Exit");
		choice = sc.nextInt();
		
		switch(choice) {
		case 1:
			deposit(sc);
			break;
		case 2:
			withdrawal(sc);
			break;
		case 3:
			checkBalance(sc);
			break;
		case 4:
			System.out.println("Thank you");
			sc.close();
			return;
		default:
			System.out.println("Enter the correct number");
		}
		}
	}
	public static void deposit(Scanner sc) {
		System.out.print("Enter the amount:-");
		double dep = sc.nextDouble();
		if(dep>0) {
			balance+=dep;
			System.out.println("Amount credited.Your new balance="+balance);
		}
		else
			System.out.println("Enter a positive number");
	}
	public static void withdrawal(Scanner sc) {
		System.out.print("Enter the amount to debit:-");
		double wth = sc.nextDouble();
		if(wth>0 && wth<=balance) {
			balance-=wth;
			System.out.println("Amount debited.Your new balance="+balance);
		}
		else if(wth>balance)
			System.out.println("Insufficient funds");
		else
			System.out.println("Enter correct amount");
	}
	public static void checkBalance(Scanner sc) {
		System.out.println("Your current balance is"+balance);
	}
	
}
