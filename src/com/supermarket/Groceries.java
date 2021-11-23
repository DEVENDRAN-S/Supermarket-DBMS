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
					ResultSet rs=ps.executeQuery("select groceryQuantity from grocery where groceryName='"+pName+"'");
					while(rs.next()) 
					{  
						 groceryQty=rs.getInt(1); 
					}
					if(rs.absolute(1))	
					{ 
						  //System.out.println("old product success");
						  System.out.println("enter Quantity");
						  int  productQuantity=sc.nextInt();
						  int newGroceryQty=groceryQty+productQuantity;
						  PreparedStatement ps1=con.prepareStatement("update grocery set groceryQuantity=?  where groceryname =?;");
						  ps1.setInt(1,newGroceryQty);
						  ps1.setString(2,pName);
						  int x=ps1.executeUpdate();
						  groceryAlreadyContain(con,vName,vPhoneNo,pName, productQuantity,vBillNo);
						  if(x!=0)
						  {
							  System.out.println("#####  Quantity of "+pName+" in the grocery list is updated to:  "+newGroceryQty+"  #####\n");
						  }		
					}
					else
					{
						  //System.out.println("new product success");
						  System.out.println("enter Quantity");
						  int  productQuantity=sc.nextInt();
						  System.out.println("enter mrp rate");
						  float groceryMrp=sc.nextFloat();
						  System.out.println("enter vendor price");
						  float vendorPrice=sc.nextFloat();
						  int itemNo = 0;
						  PreparedStatement ps1=con.prepareStatement("select groceryId from grocery order by groceryId desc limit 1; ");
						  ResultSet rs1=ps1.executeQuery();
						  while(rs1.next()) 
						  {
							 itemNo=rs1.getInt(1); 
						  }	  
						  itemNo=itemNo+1;		
						  PreparedStatement ps2=con.prepareStatement("insert into grocery values(?,?,?,?);");
						  ps2.setInt(1,itemNo);
						  ps2.setString(2,pName);
						  ps2.setInt(3,productQuantity);
						  ps2.setFloat(4,groceryMrp);
						  int x=ps2.executeUpdate();
						  float productTotalCost= vendorPrice*productQuantity;
						  PreparedStatement ps11=con.prepareStatement("insert into vendor values(?,?,?,?,?,?,?);");
						  ps11.setInt(1, vBillNo);
						  ps11.setString(2,vName);
						  ps11.setString(3, vPhoneNo);
						  ps11.setString(4,pName);
						  ps11.setInt(5,productQuantity);
						  ps11.setFloat(6,vendorPrice);
						  ps11.setFloat(7, productTotalCost);
						  int y=ps11.executeUpdate();
						  if(y!=0 && x!=0)
						  {
							   System.out.println("#####  "+productQuantity+" quantity of "+pName +" is added to Vendor bill  #####\n");
						   	   System.out.println("#####  New item "+pName+" with quantity "+productQuantity+" is added to  the grocery list  #####\n");
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

	public  void groceryAlreadyContain(Connection con,String vName,String vPhoneNo,String pName, int  productQuantity,int vBillNo)
	{
		  try {
				Statement ps=con.createStatement();  
				int vendorBuyQty = 0;
				float  productUnitCost=0;
				ResultSet rs=ps.executeQuery("select buyQuantity,productUnitCost from vendor where productName='"+pName+"' and vendorBillNo='"+vBillNo+"'");
				while(rs.next()) 
				{  
					 vendorBuyQty=rs.getInt(1);
					 productUnitCost=rs.getFloat(2);
				}
				if(rs.absolute(1))	
				{
					  int newQty=vendorBuyQty+productQuantity;
					  float productTotalCost=newQty*productUnitCost;
					  PreparedStatement ps1=con.prepareStatement("update vendor set buyQuantity=?,productTotalcost=?  where productName='"+pName+"' and vendorBillNo='"+vBillNo+"'");
					  ps1.setInt(1,newQty);
					  ps1.setFloat(2,productTotalCost);
					  int x=ps1.executeUpdate();
					  if(x!=0)
					  {
						  System.out.println(" item in the vendor bill is updated ");
					  }
				}
				else
				{   
					  System.out.println("enter vendor price");
				      float vendorPrice=sc.nextFloat();
					  float productTotalCost= vendorPrice*productQuantity;
					  PreparedStatement ps11=con.prepareStatement("insert into vendor values(?,?,?,?,?,?,?);");
					  ps11.setInt(1, vBillNo);
					  ps11.setString(2,vName);
					  ps11.setString(3, vPhoneNo);
					  ps11.setString(4,pName);
					  ps11.setInt(5,productQuantity);
					  ps11.setFloat(6,vendorPrice);
					  ps11.setFloat(7, productTotalCost);
					  int y=ps11.executeUpdate();
					  if(y!=0)
					  {
						   System.out.println("#####  "+productQuantity+" quantity of "+pName +" is added to Vendor bill  #####\n");
					  }
				}
		    }catch (Exception e)
		     {
		     e.printStackTrace();
		     System.err.println("Invalid Input");
		     }
		
	} 
	

}
