package com.supermarket;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;


public class Totalpoint {

	Scanner sc = new Scanner(System.in);

	public void customerdetails(Connection con,String cName, String cNumber, float total) 
	{
	 try {
		    float newPoint=total/15;
		    float  prevPoint=0;
			Statement ps=con.createStatement();  
			ResultSet rs=ps.executeQuery("select  customerPoints from customerPoint where customerPhoneNo='"+cNumber+"'");
			while(rs.next()) 
			{  
				 prevPoint=rs.getInt(1);
			
			}
			if(rs.absolute(1))	
			{   
				  float customerPoint=prevPoint+newPoint;
				  PreparedStatement ps1=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
				  ps1.setFloat(1,customerPoint);
				  ps1.executeUpdate();
				  if (customerPoint >= 100) 
				  {
						System.out.println("         thank you " + cName+ " for being as a valuable customer \n   we are awarding you a small gift for your constant bonding  with us\n\n");
				       float balancePoint=customerPoint-100;
				       PreparedStatement ps2=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
					   ps2.setFloat(1,balancePoint);
					   ps2.executeUpdate();
				  }
			}
			else
			{    
				  PreparedStatement ps1=con.prepareStatement("insert into customerPoint values(?,?,?);");
				  ps1.setString(1, cName);
				  ps1.setString(2,cNumber);
				  ps1.setFloat(3,newPoint);
				  ps1.executeUpdate();
				  if (newPoint >= 100) 
				  {
						System.out.println("         thank you " + cName+ " for being as a valuable customer \n   we are awarding you a small gift for your constant bonding  with us\n\n");
						float balancePoint=	newPoint-100;
					    PreparedStatement ps2=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
						ps2.setFloat(1,balancePoint);
						ps2.executeUpdate();
				  }
			}
	
	    }catch (Exception e)
         {
           e.printStackTrace();
          System.err.println("Invalid Input");
         }
    }

	
	
	public void payment(float discountPrice) {
		System.out.println("do you want to make  online payment  (yes/no)");
		String paymentMode = sc.next();
		if (paymentMode.equalsIgnoreCase("yes")) {
			System.out.println("please select your bank to pay");
			System.out.println("1)axis bank");
			System.out.println("2)SBI");
			System.out.println("3)Indian bank");
			System.out.println("4)ICICI BANK");
			selectBank(discountPrice);

		} else {
			System.out.println("         Thank you !We wish you all a great success!!!!\n");
		}
	}

	public void selectBank(float discountPrice) {
		try {
			int opt = sc.nextInt();
			switch (opt) {
			case 1:
				System.out.println("Welcome to AXIS bank");
				break;
			case 2:
				System.out.println("Welcome to SBI");
				break;
			case 3:
				System.out.println("Welcome to Indian bank");
				break;
			case 4:
				System.out.println("Welcome to ICICI BANK");
				break;
			default:
				System.out.println("Please select Banks only in the list");
				selectBank(discountPrice);

			}
			amountCheck(discountPrice);
		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Invalid input");
		}

	}

	public void amountCheck(float discountPrice) {

		try {
			System.out.println("enter  the amount to pay");
			float amountPay = sc.nextFloat();
			if (amountPay == discountPrice) {
				System.out.println("                  your payment is successfull");
				System.out.println("         Thank you !We wish you all a great success!!!!\n");
			} else {
				System.out.println("wrong amount");
				amountCheck(discountPrice);
			}
		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Entered value is not a Integer/Float");
		}

	}

}
