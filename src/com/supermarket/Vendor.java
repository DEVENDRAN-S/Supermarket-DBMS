package com.supermarket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.sql.*;



public class Vendor {

	 Scanner sc=new Scanner(System.in);
	public  void displayVendorBill(Connection con,String vName,String vPhoneNo,int vBillNo) 
	{	
		float total=0;
	 System.out.println("\n                          "+"BILLING");
	 DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/YYYY");
	 DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
	   LocalDate dateNow = LocalDate.now();
	   LocalTime timeNow=LocalTime.now();
    System.out.println("\n    BILL NO  :"+vBillNo);
	System.out.println("VENDOR NAME  :"+vName);                    
	System.out.println("                                              Date: "+df.format(dateNow));
	System.out.println("                                              Time: "+tf.format(timeNow));  

	System.out.println("======================================================================");
	System.out.println("Item name"+"\t\t"+"Quantity"+"\t"+"unit cost"+"\t"+"Item Total cost");
	System.out.println("======================================================================");
	java.sql.PreparedStatement ps;
	try {
		 ps = con.prepareStatement("select productName,buyQuantity,productUnitCost,productTotalCost from vendor where vendorBillNo='"+vBillNo+"';");
		ResultSet rs1=ps.executeQuery();
		while(rs1.next()) 
		{  
				System.out.println(rs1.getString(1)+"\t\t"+rs1.getInt(2)+"\t\t"+rs1.getFloat(3)+"\t\t"+rs1.getFloat(4));
				total=total+rs1.getInt(4);
		}
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		
	System.out.println("======================================================================");
   System.out.println("Total Amount   :"+total);
   System.out.println("======================================================================");
   System.out.println("         Thank you! Have a safe and Happy day!!!\n\n\n");
   
	}
	
	public  void buyOrRemove(Connection con,String vName,String vPhoneNo,int vBillNo) 
	{
		
		String choice = "";
		System.out.println("do you want to buy a new  items/remove an item  from the vendor(buy/remove/no)");
			choice=sc.next();
		if(choice.equalsIgnoreCase("buy"))
		{
		
			Groceries g = new Groceries();
			g.buyItem(con,vName,vPhoneNo,vBillNo);
		}
		else if(choice.equalsIgnoreCase("remove"))
		{
			removeProduct(con,vName,vPhoneNo,vBillNo);
		}
		else if(choice.equalsIgnoreCase("no"))
		{
			 displayVendorBill(con,vName,vPhoneNo,vBillNo); 
		}
		else 
		{
			buyOrRemove(con,vName,vPhoneNo,vBillNo);
		}
		
	}

	
	public  void removeProduct(Connection con,String vName,String vPhoneNo,int vBillNo)
	{
		// for canceling items
				String pName="";
				System.out.println("enter the product name to cancel");
				pName=sc.next();
				  try {
						Statement ps=con.createStatement();  
						int vendorBuyQty = 0;
						float vendorUnitCost = 0;
						ResultSet rs1=ps.executeQuery("select buyQuantity,productUnitCost from vendor where productName='"+pName+"' and vendorBillNo='"+vBillNo+"'");
						while(rs1.next()) 
						{  
							 vendorBuyQty=rs1.getInt(1);
							 vendorUnitCost=rs1.getFloat(2);  
							
						}
						if(rs1.absolute(1))	
						{
							System.out.println("enter Quantity");
							int  productQuantity=sc.nextInt();
							int  groceryQty=0;
							 ResultSet rs2=ps.executeQuery("select groceryQuantity from grocery where groceryName='"+pName+"'");
								while(rs2.next()) 
								{  
									 groceryQty=rs2.getInt(1);
								}
							int newGroceryQty=groceryQty-productQuantity;
							if(vendorBuyQty== productQuantity)
							{    
								  PreparedStatement ps1=con.prepareStatement("Delete from  vendor  where  vendorBillNo='"+vBillNo+"' AND  productName='"+pName+"';"); 
								  int x=ps1.executeUpdate();
								  if(x!=0)
								  {
				 	 				  System.out.println("#####  "+pName +" is fully removed from Vendor bill  #####\n");
								  }
								  if(vendorBuyQty==groceryQty) 
								  {
									  PreparedStatement ps2=con.prepareStatement("Delete from  grocery  where  groceryName='"+pName+"';"); 
									  //ps1.setString(1,pName);
									  int y=ps2.executeUpdate();
									  if(y!=0)
									  {
										  System.out.println("#####  "+pName +" is fully removed from Groceries list  #####\n");
										  
									  }
								  }
								  else
								  {
									  PreparedStatement ps3=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
									  ps3.setInt(1,newGroceryQty);
									  ps3.setString(2,pName);
									  int y=ps3.executeUpdate();
									  if(y!=0)
									  {
										  System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQty+"  #####\n");
									  }
								  }
								
								  
							}
							
							else if(vendorBuyQty> productQuantity)
							{
								int newvendorBuyQty=vendorBuyQty-productQuantity;
								float productTotalCost=newvendorBuyQty*vendorUnitCost;
								PreparedStatement ps1=con.prepareStatement("update vendor set buyQuantity=?,productTotalCost=? where  vendorBillNo='"+vBillNo+"' AND  productName='"+pName+"'");
								  ps1.setInt(1,newvendorBuyQty);
								  ps1.setFloat(2,productTotalCost);
								  int x=ps1.executeUpdate();
								PreparedStatement ps2=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
								  ps2.setInt(1,newGroceryQty);
								  ps2.setString(2,pName);
								  ps2.executeUpdate();
								  if(x!=0)
								  {
								      System.out.println("#####  "+productQuantity+" quantity of "+pName +" is removed from  the existing Vendor bill  #####\n");
									  System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQty+"  #####\n");
								  }
							}
							else
							{
								System.out.println("  "+ vName+" has selled  only "+vendorBuyQty+" quantity of "+pName);
							}
						} 
						else
						{
							System.out.println("  "+ vName+" has  not selled  the "+pName +" to us ");
						}
				   }
				   catch(Exception e)
				       {
				    	e.printStackTrace();
				    	System.out.println(" Invalid input");
				       }
				
		
	 	  buyOrRemove(con,vName,vPhoneNo,vBillNo);
	}
}
