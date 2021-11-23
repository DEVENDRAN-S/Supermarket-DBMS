package com.supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Customer  extends Totalpoint  {
	Scanner sc = new Scanner(System.in);
	public  void sellItem(Connection con,String cName,String cPhoneNo,int cBillNo){
		  
		   System.out.println("enter product name");
		   String pName=sc.next();
		   try {
				Statement ps=con.createStatement();  
				int groceryQty = 0;
				float groceryUnitCost = 0;
				ResultSet rs1=ps.executeQuery("select * from grocery where groceryName='"+pName+"'");
				while(rs1.next()) 
				{  
					 groceryQty=rs1.getInt(3);
					 groceryUnitCost=rs1.getFloat(4);  
				
				}
				if(rs1.absolute(1))	
				{ 
				
					System.out.println("enter Quantity");
					int  productQuantity=sc.nextInt();
					if(groceryQty>= productQuantity)
					{   
						if(customerAlreadyContains(con,cBillNo,pName,productQuantity,groceryQty))
						{
							  System.out.println("data updated in the customer bill");
						}
						else
						{
								  float productTotalCost=productQuantity*groceryUnitCost;
								  PreparedStatement ps1=con.prepareStatement("insert into customer values(?,?,?,?,?,?,?);");
								  ps1.setInt(1, cBillNo);
								  ps1.setString(2,cName);
								  ps1.setString(3, cPhoneNo);
								  ps1.setString(4,pName);
								  ps1.setInt(5,productQuantity);
								  ps1.setFloat(6,groceryUnitCost);
								  ps1.setFloat(7, productTotalCost);
								  int x=ps1.executeUpdate();
								  int newGroceryQuantity=groceryQty-productQuantity;
								  PreparedStatement ps3=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
								  ps3.setInt(1,newGroceryQuantity);
								  ps3.setString(2,pName);
								  int y= ps3.executeUpdate();
								  if(x!=0  && y!=0)
								  {
									  //  System.out.println("datas inserted into customer bill");
									  System.out.println("#####  "+productQuantity+" quantity of "+pName +" is added to customer bill  #####\n");
									 // System.out.println("datas updated into grocery list");
									  System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQuantity+"  #####\n");
								  }
							
						}
					}
					else
					{
						  System.out.println("available quantity of "+pName+" in the grocery list is "+groceryQty);
					}
					
				}
				else
				{    
					 PreparedStatement ps1=con.prepareStatement("select * from grocery where groceryName like'"+pName+"%';");
					 ResultSet rs2=ps1.executeQuery();
					 while(rs2.next()) 
							{  
									System.out.println("Productname: " + rs2.getString(2)+" of quantity " +rs2.getString(3)+" and price is "+rs2.getInt(4));  
							}
	
					 if(rs2.absolute(1))	
					 {  
						System.out.println("\n     #####   the above are the similar products in the grocery list #####");
						
						System.out.println("\n    #####  please enter the full name  of product  to  sell products ######\n");
					 }
					 else
					 {
						 System.out.println("products you are willing to sell is not in grocery list ");
					 }
				
				}
			
			} catch (Exception e) 
		     {
				// TODO Auto-generated catch block
				e.printStackTrace();
		     } 
		   sellOrRemove(con,cName,cPhoneNo,cBillNo);  
	}
	public  boolean customerAlreadyContains(Connection con,int cBillNo,String pName,int productQuantity,int groceryQuantity ) 
	{
		  try {
				Statement ps=con.createStatement();  
				int customerSellQty = 0;
				float customerUnitCost = 0;
				ResultSet rs1=ps.executeQuery("select purchaseQuantity,productUnitCost from customer where customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';");
				while(rs1.next()) 
				{  
					 customerSellQty=rs1.getInt(1);
					 customerUnitCost=rs1.getFloat(2);  
					  //System.out.println(product_qty+"   "+unit_cost);
				}
				if(rs1.absolute(1))	
				{ 
					  int newCustomerSellQty=customerSellQty+productQuantity;
					  float productTotalCost=newCustomerSellQty*customerUnitCost;
					  PreparedStatement ps1=con.prepareStatement("update customer set purchaseQuantity=?,productTotalCost=? where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"'");
					  ps1.setInt(1,newCustomerSellQty);
					  ps1.setFloat(2,productTotalCost);
					  int x=ps1.executeUpdate();
					  int newGroceryQuantity=groceryQuantity - productQuantity;
					  PreparedStatement ps3=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
					  ps3.setInt(1, newGroceryQuantity);
					  ps3.setString(2,pName);
					  int y=ps3.executeUpdate();
					  if(y!=0 && x!=0)
					  {
			             System.out.println("#####  "+productQuantity+" quantity of "+pName +" is updated in the existing customer bill  #####\n");
				    	 System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQuantity+"  #####\n");
					  }
					return true;
				}
				else
				{
					return false;
				}
			} catch (Exception e) 
		     {
				// TODO Auto-generated catch block
				e.printStackTrace();
		     } 
				
		      
				
		return false;
	}
	public  void displayCustomerBill(Connection con,String cName,String cPhoneNo,int cBillNo)//to display customer bill
	{
		float total=0;
		System.out.println("\n                          "+"BILLING");
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
	    LocalDate dateNow = LocalDate.now();
		LocalTime timeNow=LocalTime.now();
		System.out.println("\n      BILL NO  :"+cBillNo);   
		System.out.println("CUSTOMER NAME  :"+cName);
		System.out.println("                                                Date: "+df.format(dateNow));//for date and time
		System.out.println("                                                Time: "+tf.format(timeNow));  
		System.out.println("======================================================================");
		System.out.println("Item name"+"\t\t"+"Quantity"+"\t"+"unit cost"+"\t"+"Item Total cost");
		System.out.println("======================================================================");
		 PreparedStatement ps;
		try {
			ps = con.prepareStatement("select productName,purchaseQuantity,productUnitCost,productTotalCost from customer where customerBillNo='"+cBillNo+"';");
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
	     float discountPrice=0;
	     float savePrice=0;
	     if(total>=500)// discount above 500
	     {
	    	 savePrice=(total*10)/100;
	    	  discountPrice=total-savePrice;
	    		 System.out.println("Discount price  :"+ discountPrice);
	    		 System.out.println("======================================================================");
	    		 System.out.println("         YOUR TODAYS SAVING IS   :"+savePrice);
	    		 System.out.println("======================================================================");
	     }
	     else if(total>=300)// discount above 300
	     {
	    	 savePrice=(total*5)/100;
	    	  discountPrice=total-savePrice;
	    		 System.out.println("Discount price  :"+ discountPrice);
	    		 System.out.println("===================================================================");
	    		 System.out.println("         YOUR TODAYS SAVING IS   :"+savePrice);
	    		 System.out.println("===================================================================");
	     }
	     else 
	     {
	    	 discountPrice= total;
	     }
	   
		
	     System.out.println("         Thank you! Have a safe and Happy day!!!\n\n");
	     
	     System.out.println("do you want to make any changes in the bill (yes/no)   ");//conforming bill
	   try
	    { 
		   String billChange=sc.next();
	     if(billChange.equalsIgnoreCase("yes")) 
	     {
	    	 sellOrRemove(con,cName,cPhoneNo,cBillNo);
	     }
	     else if(billChange.equalsIgnoreCase("no")) 
	     {
	    	System.out.println("           Your bill is confirmed!thank you\n\n\n");
	    	customerdetails(con ,cName, cPhoneNo,  discountPrice);//adding points to customers
	     }
	    }
	   catch(Exception e)
	       {
	    	e.printStackTrace();
	    	System.out.println(" Invalid input");
	       }
	     
	     
	    //payment(discountPrice); 
	}
	public  void removeItem(Connection con,String cName,String cPhoneNo,int cBillNo) {
		// for canceling items
		String pName="";
		System.out.println("enter the product name to cancel");
		pName=sc.next();
		  try {
				Statement ps=con.createStatement();  
				int customerSellQty = 0;
				float customerunitcost = 0;
				ResultSet rs1=ps.executeQuery("select purchaseQuantity,productUnitCost from customer where customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';");
				while(rs1.next()) 
				{  
					 customerSellQty=rs1.getInt(1);
					 customerunitcost=rs1.getFloat(2);  
				}
				if(rs1.absolute(1))	
				{
					System.out.println("enter Quantity");
					int  productQuantity=sc.nextInt();
					int  groceryQuantity=0;
					 ResultSet rs2=ps.executeQuery("select groceryQuantity from grocery where groceryName='"+pName+"'");
						while(rs2.next()) 
						{  
							 groceryQuantity=rs2.getInt(1);
						}
					int newGroceryQuantity=groceryQuantity+productQuantity;
					if(customerSellQty== productQuantity)
					{    
						  PreparedStatement ps1=con.prepareStatement("Delete from  customer  where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';"); 
						  //ps1.setString(1,pName);
						  int x=ps1.executeUpdate();
						  PreparedStatement ps2=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
						  ps2.setInt(1,newGroceryQuantity);
						  ps2.setString(2,pName);
						  ps2.executeUpdate();
						  if(x!=0)
						  {
							  System.out.println("#####  "+pName +" is fully removed from customer bill  #####\n");
								 System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQuantity+"  #####\n");

						  }
					}
					
					else if(customerSellQty> productQuantity)
					{
						int newCustomerSellQty=customerSellQty-productQuantity;
						float productTotalCost=newCustomerSellQty*customerunitcost;
						PreparedStatement ps1=con.prepareStatement("update customer set purchaseQuantity=?,productTotalCost=? where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"'");
						  ps1.setInt(1,newCustomerSellQty);
						  ps1.setFloat(2,productTotalCost);
						  int x=ps1.executeUpdate();
						PreparedStatement ps2=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
						  ps2.setInt(1,newGroceryQuantity);
						  ps2.setString(2,pName);
						  ps2.executeUpdate();
						  if(x!=0)
						  {
							  
							  
							  System.out.println("#####  "+productQuantity+" quantity of "+pName +" is removed from  the existing customer bill  #####\n");
								 System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQuantity+"  #####\n");
						  }
					}
					else
					{
						System.out.println("  "+ cName+" has bought only "+customerSellQty+" quantity of "+pName);
					}
				} 
				else
				{
					System.out.println("  "+ cName+" has  not buyed  the "+pName +" to us ");
				}
		   }
		   catch(Exception e)
		       {
		    	e.printStackTrace();
		    	System.out.println(" Invalid input");
		       }
		
	 	sellOrRemove(con,cName,cPhoneNo,cBillNo);
	}
	public  void sellOrRemove( Connection con,String cName,String cPhoneNo,int cBillNo) 
	{
		String choice = "";
		System.out.println("do you want to sell a new  items/remove an item (sell/remove/no)");
	     choice=sc.next();
		if(choice.equalsIgnoreCase("sell"))
		{
			sellItem(con,cName,cPhoneNo,cBillNo);
		}
		else if(choice.equalsIgnoreCase("remove"))
		{
			removeItem(con,cName,cPhoneNo,cBillNo);
		}
		else if(choice.equalsIgnoreCase("no"))
		{
			displayCustomerBill(con,cName,cPhoneNo,cBillNo);
		}
		else 
		{
			sellOrRemove(con,cName,cPhoneNo,cBillNo);
		}
	}


}
