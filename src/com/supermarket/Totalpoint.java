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
   public void currentCustomerProducts(Connection con ,int cBillNo) 
   {
	      System.out.println("======================================================================");
		  System.out.println("Item name"+"\t\t"+"Quantity"+"\t"+"unit cost"+"\t"+"Item Total cost");
		  System.out.println("======================================================================");
		  
		try {
			Statement ps = con.createStatement();
			ResultSet rsCustomerBill=ps.executeQuery ("select productName,purchaseQuantity,productUnitCost,productTotalCost from customer where customerBillNo='"+cBillNo+"';");
			while(rsCustomerBill.next()) 
			{  
					System.out.println(rsCustomerBill.getString(1)+"\t\t"+rsCustomerBill.getInt(2)+"\t\t"+rsCustomerBill.getFloat(3)+"\t\t"+rsCustomerBill.getFloat(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
			
		  
		  System.out.println("======================================================================\n");
   }

}
