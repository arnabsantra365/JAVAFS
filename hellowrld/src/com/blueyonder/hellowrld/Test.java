package com.blueyonder.hellowrld;

public class Test {
	public static String sname="Arnab";
	public static String sid="1041220";
	static class Student{
		public static void getDetails() {
			System.out.println("Student name="+sname);
			System.out.println("Student id="+sid);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Student sc = new Student();
		sc.getDetails();

	}

}
