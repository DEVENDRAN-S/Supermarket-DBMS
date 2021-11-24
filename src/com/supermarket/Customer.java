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
				ResultSet rsGrocery=ps.executeQuery("select groceryQuantity,groceryMrp from grocery where groceryName='"+pName+"'");
				while(rsGrocery.next()) 
				{  
					 groceryQty=rsGrocery.getInt(1);
					 groceryUnitCost=rsGrocery.getFloat(2);  
				
				}
				if(rsGrocery.absolute(1))	
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
								  PreparedStatement insertCustomerProduct=con.prepareStatement("insert into customer values(?,?,?,?,?,?,?);");
								  insertCustomerProduct.setInt(1, cBillNo);
								  insertCustomerProduct.setString(2,cName);
								  insertCustomerProduct.setString(3, cPhoneNo);
								  insertCustomerProduct.setString(4,pName);
								  insertCustomerProduct.setInt(5,productQuantity);
								  insertCustomerProduct.setFloat(6,groceryUnitCost);
								  insertCustomerProduct.setFloat(7, productTotalCost);
								  int x=insertCustomerProduct.executeUpdate();
								  int newGroceryQuantity=groceryQty-productQuantity;
								  PreparedStatement updateGrocery=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname ='"+pName+"';");
								  updateGrocery.setInt(1,newGroceryQuantity);
								  int y= updateGrocery.executeUpdate();
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
					 PreparedStatement checkGroceryLike=con.prepareStatement("select * from grocery where groceryName like'"+pName+"%';");
					 ResultSet rsGroceryLike=checkGroceryLike.executeQuery();
					 while(rsGroceryLike.next()) 
							{  
									System.out.println("Productname: " + rsGroceryLike.getString(2)+" of quantity " +rsGroceryLike.getInt(3)+" and price is "+rsGroceryLike.getFloat(4));  
							}
	
					 if(rsGroceryLike.absolute(1))	
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
				ResultSet rsCustomerProduct=ps.executeQuery("select purchaseQuantity,productUnitCost from customer where customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';");
				while(rsCustomerProduct.next()) 
				{  
					 customerSellQty=rsCustomerProduct.getInt(1);
					 customerUnitCost=rsCustomerProduct.getFloat(2);  
					  //System.out.println(product_qty+"   "+unit_cost);
				}
				if(rsCustomerProduct.absolute(1))	
				{ 
					  int newCustomerSellQty=customerSellQty+productQuantity;
					  float productTotalCost=newCustomerSellQty*customerUnitCost;
					  PreparedStatement updateCustomerProduct=con.prepareStatement("update customer set purchaseQuantity=?,productTotalCost=? where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"'");
					  updateCustomerProduct.setInt(1,newCustomerSellQty);
					  updateCustomerProduct.setFloat(2,productTotalCost);
					  int x=updateCustomerProduct.executeUpdate();
					  int newGroceryQuantity=groceryQuantity - productQuantity;
					  PreparedStatement updateGrocery=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname ='"+pName+"';");
					  updateGrocery.setInt(1, newGroceryQuantity);
					  int y=updateGrocery.executeUpdate();
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
			ResultSet rsVendorBill=ps.executeQuery();
			while(rsVendorBill.next()) 
			{  
					System.out.println(rsVendorBill.getString(1)+"\t\t"+rsVendorBill.getInt(2)+"\t\t"+rsVendorBill.getFloat(3)+"\t\t"+rsVendorBill.getFloat(4));
					total=total+rsVendorBill.getInt(4);
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
				ResultSet rsCustomerProduct=ps.executeQuery("select purchaseQuantity,productUnitCost from customer where customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';");
				while(rsCustomerProduct.next()) 
				{  
					 customerSellQty=rsCustomerProduct.getInt(1);
					 customerunitcost=rsCustomerProduct.getFloat(2);  
				}
				if(rsCustomerProduct.absolute(1))	
				{
					System.out.println("enter Quantity");
					int  productQuantity=sc.nextInt();
					int  groceryQuantity=0;
					 ResultSet rsGrocery=ps.executeQuery("select groceryQuantity from grocery where groceryName='"+pName+"'");
						while(rsGrocery.next()) 
						{  
							 groceryQuantity=rsGrocery.getInt(1);
						}
					int newGroceryQuantity=groceryQuantity+productQuantity;
					if(customerSellQty== productQuantity)
					{    
						  PreparedStatement deleteCustomerProduct=con.prepareStatement("Delete from  customer  where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"';"); 
						  int x=deleteCustomerProduct.executeUpdate();
						  PreparedStatement updateGrocery=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname ='"+pName+"';");
						  updateGrocery.setInt(1,newGroceryQuantity);
						  int y=updateGrocery.executeUpdate();
						  if(x!=0 && y!=0)
						  {
							  System.out.println("#####  "+pName +" is fully removed from customer bill  #####\n");
							  System.out.println("#####  Remaining Quantity of "+pName+" in the grocery list is:  "+newGroceryQuantity+"  #####\n");

						  }
					}
					
					else if(customerSellQty> productQuantity)
					{
						int newCustomerSellQty=customerSellQty-productQuantity;
						float productTotalCost=newCustomerSellQty*customerunitcost;
						PreparedStatement updateCustomerProduct=con.prepareStatement("update customer set purchaseQuantity=?,productTotalCost=? where  customerBillNo='"+cBillNo+"' AND  productName='"+pName+"'");
						updateCustomerProduct.setInt(1,newCustomerSellQty);
						updateCustomerProduct.setFloat(2,productTotalCost);
						  int x=updateCustomerProduct.executeUpdate();
						PreparedStatement updateGrocery=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
						updateGrocery.setInt(1,newGroceryQuantity);
						updateGrocery.setString(2,pName);
						 int y=updateGrocery.executeUpdate();
						  if(x!=0 && y!=0)
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
					 ResultSet rscheckCustomerProduct=ps.executeQuery("select * from customer where customerBillNo='"+cBillNo+"' AND  productName like '"+pName+"%';");
					 while(rscheckCustomerProduct.next()) 
							{  
									System.out.println("Productname: " + rscheckCustomerProduct.getString(4)+" of quantity " +rscheckCustomerProduct.getInt(5)+" and price is "+rscheckCustomerProduct.getFloat(6));  
							}
	
					 if(rscheckCustomerProduct.absolute(1))	
					 {  
						System.out.println("\n     #####   the above are the similar products in the customer bill #####");
						
						System.out.println("\n    #####  please enter the full name  of product  to  remove ######\n");
					 }
					 else
					 {
						 System.out.println("  "+ cName+" has  not buyed  the "+pName +" from  us ");
					 }
					
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
