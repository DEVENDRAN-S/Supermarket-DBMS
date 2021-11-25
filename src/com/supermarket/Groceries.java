package com.supermarket;

import java.util.Scanner;
import java.sql.*;
public class Groceries extends Vendor
{    
	  Scanner sc= new Scanner(System.in);
	public   void buyItem(Connection con,String vName,String vPhoneNo,int vBillNo)
	{
		
		System.out.println("enter the product name:");
		String pName=sc.next();
		
		   try {
					Statement ps=con.createStatement();  
					int groceryQty = 0;
					ResultSet rsGrocery=ps.executeQuery("select groceryQuantity from grocery where groceryName='"+pName+"'");
					while(rsGrocery.next()) 
					{  
						 groceryQty=rsGrocery.getInt(1); 
					}
					if(rsGrocery.absolute(1))	
					{ 
						  System.out.println("enter Quantity");
						  int  productQuantity=sc.nextInt();
						  int newGroceryQty=groceryQty+productQuantity;
						  if( groceryAlreadyContain(con,vName,vPhoneNo,pName, productQuantity,vBillNo))
						  {
							  PreparedStatement updateGrocery=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname ='"+pName+"'");
							  updateGrocery.setInt(1,newGroceryQty);
							  int x=updateGrocery.executeUpdate();
							 
							  if(x!=0)
							  {
								  System.out.println("#####  Quantity of "+pName+" in the grocery list is updated to:  "+newGroceryQty+"  #####\n");
							  }		
						  }
						  
					}
					else
					{
						 PreparedStatement checkGroceryLike=con.prepareStatement("select * from grocery where groceryName like'"+pName+"%';");
						 ResultSet rsGroceryLike=checkGroceryLike.executeQuery();
						 while(rsGroceryLike.next()) 
								{  
								        System.out.println("******************************************************************");
										System.out.println("Productname:   " + rsGroceryLike.getString(2)+"\t Quantity: " +rsGroceryLike.getInt(3)+"\t Price: "+rsGroceryLike.getFloat(4));  
								}
		
						 if(rsGroceryLike.absolute(1))	
						 {  
							System.out.println("******************************************************************");
							System.out.println("\n     #####   the above are the similar products in the grocery list #####");
							
							System.out.println("\n    #####  please enter the full name  of product  to  sell products ######\n");
						 }
						 else
						 {
						  System.out.println("enter Quantity");
						  int  productQuantity=sc.nextInt();
						  System.out.println("enter mrp rate");
						  float groceryMrp=sc.nextFloat();
						  System.out.println("enter vendor price");
						  float vendorPrice=sc.nextFloat();
						  int itemNo = 0;
						  PreparedStatement psGetItemId=con.prepareStatement("select groceryId from grocery order by groceryId desc limit 1; ");
						  ResultSet rsGetItemId=psGetItemId.executeQuery();
						  while(rsGetItemId.next()) 
						  {
							 itemNo=rsGetItemId.getInt(1); 
						  }	  
						  itemNo=itemNo+1;		
						  PreparedStatement insertGrocery=con.prepareStatement("insert into grocery values(?,?,?,?);");
						  insertGrocery.setInt(1,itemNo);
						  insertGrocery.setString(2,pName);
						  insertGrocery.setInt(3,productQuantity);
						  insertGrocery.setFloat(4,groceryMrp);
						  int x=insertGrocery.executeUpdate();
						  float productTotalCost= vendorPrice*productQuantity;
						  PreparedStatement insertVendorProduct=con.prepareStatement("insert into vendor values(?,?,?,?,?,?,?);");
						  insertVendorProduct.setInt(1, vBillNo);
						  insertVendorProduct.setString(2,vName);
						  insertVendorProduct.setString(3, vPhoneNo);
						  insertVendorProduct.setString(4,pName);
						  insertVendorProduct.setInt(5,productQuantity);
						  insertVendorProduct.setFloat(6,vendorPrice);
						  insertVendorProduct.setFloat(7, productTotalCost);
						  int y=insertVendorProduct.executeUpdate();
						  if(y!=0 && x!=0)
						  {
							   System.out.println("#####  "+productQuantity+" quantity of "+pName +" is added to Vendor bill  #####\n");
							   currentVendorProducts(con ,vBillNo); 
						   	   System.out.println("#####  New item "+pName+" with quantity "+productQuantity+" is added to  the grocery list  #####\n");
						  }
						 }
					}
	     }
	     catch (Exception e)
	     {
	     e.printStackTrace();
	     System.err.println("Invalid Input");
	     }
		//Main.displayitems(groceries);
	     buyOrRemove(con,vName,vPhoneNo,vBillNo);
		
	}

	public  boolean groceryAlreadyContain(Connection con,String vName,String vPhoneNo,String pName, int  productQuantity,int vBillNo)
	{
		  try {
				Statement ps=con.createStatement();  
				int vendorBuyQty = 0;
				float  productUnitCost=0;
				ResultSet rsVendorProduct=ps.executeQuery("select buyQuantity,productUnitCost from vendor where productName='"+pName+"' and vendorBillNo='"+vBillNo+"'");
				while(rsVendorProduct.next()) 
				{  
					 vendorBuyQty=rsVendorProduct.getInt(1);
					 productUnitCost=rsVendorProduct.getFloat(2);
				}
				if(rsVendorProduct.absolute(1))	
				{
					  int newQty=vendorBuyQty+productQuantity;
					  float productTotalCost=newQty*productUnitCost;
					  PreparedStatement updateVendorProduct=con.prepareStatement("update vendor set buyQuantity=?,productTotalcost=?  where productName='"+pName+"' and vendorBillNo='"+vBillNo+"'");
					  updateVendorProduct.setInt(1,newQty);
					  updateVendorProduct.setFloat(2,productTotalCost);
					  int y=updateVendorProduct.executeUpdate();
					  if(y!=0)
					  {
						  System.out.println("#####  "+productQuantity+" quantity of "+pName +" is updated to existing  to Vendor bill  #####\n");
						  currentVendorProducts(con ,vBillNo); 
					  }
				}
				else
				{   
					  System.out.println("enter vendor price");
				      float vendorPrice=sc.nextFloat();
					  float productTotalCost= vendorPrice*productQuantity;
					  PreparedStatement  insertVendorProduct=con.prepareStatement("insert into vendor values(?,?,?,?,?,?,?);");
					  insertVendorProduct.setInt(1, vBillNo);
					  insertVendorProduct.setString(2,vName);
					  insertVendorProduct.setString(3, vPhoneNo);
					  insertVendorProduct.setString(4,pName);
					  insertVendorProduct.setInt(5,productQuantity);
					  insertVendorProduct.setFloat(6,vendorPrice);
					  insertVendorProduct.setFloat(7, productTotalCost);
					  int y=insertVendorProduct.executeUpdate();
					  if(y!=0)
					  {
						      System.out.println("#####  "+productQuantity+" quantity of "+pName +" is added to Vendor bill  #####\n");
						      currentVendorProducts(con ,vBillNo); 
					  }
				}
				return true;
		    }catch (Exception e)
		     {
		     e.printStackTrace();
		     System.err.println("Invalid Input");
		     return false;
		     } 
	} 
	

}
