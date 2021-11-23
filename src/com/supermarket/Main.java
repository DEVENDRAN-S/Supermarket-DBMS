package com.supermarket;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;



import java.sql.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("                " + "WELCOME TO OUR SUPER MARKET");
		System.out.println("******************************************************************");
		int vendorBillNo = 0;
		int customerBillNo = 0;
		Scanner sc = new Scanner(System.in);
		while (true) {
			Groceries g = new Groceries();
			Customer c = new Customer();
			displayItems();
			System.out.println("1)Vendors Portal \n2)Customers Portal \n3)to exit");
			int option = sc.nextInt();
			switch (option){
			case 1:
				System.out.println("enter the vendor name");
				String vendorName = sc.next();
				try {
					if (isAlpha(vendorName)) {
						System.out.println("enter the  vendor phone number");
						String venderPhoneNo = sc.next();

						if (venderPhoneNo.matches("[0-9]{10}$")) 
						{  
						
						  Connection con=getConnect() ;
						  Statement stmt=con.createStatement();  
						  ResultSet rs2=stmt.executeQuery("SELECT vendorBillNo FROM vendor ORDER BY vendorBillNo DESC LIMIT 1;");  
						  while(rs2.next()) 
								{
							      vendorBillNo=rs2.getInt(1);
								}
						  vendorBillNo = vendorBillNo + 1;
						  g.buyItem(con, vendorName, venderPhoneNo, vendorBillNo);
						  con.close();
						} 
						else

						{
							System.out.println(" phone number format is wrong");

						}

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:
				System.out.println("enter customer name");
				String customerName = sc.next();
				try {
					if (isAlpha(customerName)) {
						System.out.println("enter the  customer phone number");
						String customerPhoneNo = sc.next();

						if (customerPhoneNo.matches("[0-9]{10}$")) 
						{
				            Connection con=getConnect() ;
						    Statement stmt=con.createStatement();  
							ResultSet rs2=stmt.executeQuery("SELECT customerBillNo FROM customer ORDER BY customerBillNo DESC LIMIT 1;");  
							while(rs2.next()) 
								{
								customerBillNo=rs2.getInt(1);
								}
							customerBillNo = customerBillNo + 1;
							c.sellItem(con,customerName, customerPhoneNo,customerBillNo);
							con.close();
						} else

						{
							System.out.println(" phone number format is wrong");

						}

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3:
				System.exit(0);
				break;
			default:
				System.out.println("    please enter a valid input\n");
			}
		}
		

	}
	public static Connection getConnect() 
	{
		
		try {
		     DriverManager.registerDriver(new com.mysql.jdbc.Driver());  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket?characterEncoding=latin1","root","Deva1234@&");
		     return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
		
	}
	public  static void displayItems() {
		// TODO Auto-generated method stub
		try { 
			Connection con=getConnect() ;
			 PreparedStatement ps=con.prepareStatement("select * from grocery order by groceryId ");
			ResultSet rs1=ps.executeQuery();
			System.out.println("S.NO" + "\t\t" + "NAME" + "\t\t\t" + "QUANTITY" + "\t" + "MRP RATE");
			System.out.println("******************************************************************");
			while(rs1.next()) 
			{  
					System.out.println(rs1.getInt(1)+"\t\t"+rs1.getString(2)+"\t\t"+rs1.getInt(3)+"\t\t"+rs1.getFloat(4));  
			}
			System.out.println("******************************************************************");
			  con.close();
			} 
		     catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  	
	}

	public static boolean isAlpha(String name) {
		char[] chars = name.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}
}
