package com.supermarket;

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
			ResultSet rsCustomerPoint=ps.executeQuery("select  customerPoints from customerPoint where customerPhoneNo='"+cNumber+"'");
			while(rsCustomerPoint.next()) 
			{  
				 prevPoint=rsCustomerPoint.getInt(1);
			
			}
			if(rsCustomerPoint.absolute(1))	
			{   
				  float customerPoint=prevPoint+newPoint;
				  PreparedStatement updateCustomerPoint=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
				  updateCustomerPoint.setFloat(1,customerPoint);
				  updateCustomerPoint.executeUpdate();
				  if (customerPoint >= 100) 
				  {
						System.out.println("         thank you " + cName+ " for being as a valuable customer \n   we are awarding you a small gift for your constant bonding  with us\n\n");
				       float balancePoint=customerPoint-100;
				       PreparedStatement reupdateCustomerPoint=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
				       reupdateCustomerPoint.setFloat(1,balancePoint);
				       reupdateCustomerPoint.executeUpdate();
				  }
			}
			else
			{    
				  PreparedStatement insertCustomerPoint=con.prepareStatement("insert into customerPoint values(?,?,?);");
				  insertCustomerPoint.setString(1, cName);
				  insertCustomerPoint.setString(2,cNumber);
				  insertCustomerPoint.setFloat(3,newPoint);
				  insertCustomerPoint.executeUpdate();
				  if (newPoint >= 100) 
				  {
						System.out.println("         thank you " + cName+ " for being as a valuable customer \n   we are awarding you a small gift for your constant bonding  with us\n\n");
						float balancePoint=	newPoint-100;
					    PreparedStatement reupdateCustomerPoint=con.prepareStatement("update customerPoint set  customerPoints=? where customerPhoneNo='"+cNumber+"'");
					    reupdateCustomerPoint.setFloat(1,balancePoint);
					    reupdateCustomerPoint.executeUpdate();
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
			if (amountPay == discountPrice)
			{
				System.out.println("                  your payment is successfull");
				System.out.println("         Thank you !We wish you all a great success!!!!\n");
			}
			else 
			{
				System.out.println("wrong amount");
			}
		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Entered value is not a Integer/Float");
		}

	}

}
