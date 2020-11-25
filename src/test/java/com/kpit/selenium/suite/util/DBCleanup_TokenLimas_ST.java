package com.kpit.selenium.suite.util;

/**
 * @author milindw3
 */

import  java.sql.Connection;		
import  java.sql.Statement;

import org.junit.Test;

import  java.sql.DriverManager;		
import  java.sql.SQLException;		
public class  DBCleanup_TokenLimas_ST {	
	@Test
    	public void  main() throws  ClassNotFoundException, SQLException {													
				//Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"		
                String dbUrl = "jdbc:mysql://104.199.200.43:3306/limas_token_qa";					

				//Database Username		
				String username = "root";	
                
				//Database Password		
				String password = "PlokQaz@0129";				

				//Query to Execute		
				String query1 = "DELETE FROM group_customer_product_version_feature WHERE product_id > 358;";	
				String query2 = "DELETE from user_customer_product_version_feature where user_id>12; "; 
				String query3 = "DELETE  FROM customer_product_version_feature WHERE product_version_feature_id > 125;"; 
				String query4 = "DELETE  FROM product_version_feature WHERE product_version_feature_id > 125;"; 
				String query5 = "DELETE  FROM product_version WHERE product_version_id > 163;"; 
				String query6 = "DELETE  FROM customer_product_config WHERE product_id > 493;"; 
				String query61 = "DELETE from product_feature_definition WHERE product_feature_id > 122;";
				String query7 = "DELETE  FROM product WHERE id > 493;"; 
//				String query8 = "DELETE from system_user_group_config where group_id > 81;"; 
				String query8 = "DELETE from system_user_group_config where system_user_id > 410;"; 
				String query9 = "DELETE from token_group where id > 73;"; 
				String query10 = "DELETE FROM token_bucket where customer_id > 58;"; 
//				String query11 = "DELETE from token_system_user where customer_id > 58;"; 
				String query11 = "DELETE from token_system_user where id > 410;"; 
				String query12 = "DELETE FROM customer where id > 58;"; 
				String query13 = "DELETE from user_hardware_config  where user_id>12;"; 
				String query14 = "DELETE from gdpr_consent where user_id>12;"; 
				String query15 = "DELETE from group_user_config where user_id>12;"; 
				String query16 = "DELETE from token_user where id>12;";  
				String query17 = "DELETE from transactions;"; 
         	    //Load mysql jdbc driver		
           	    Class.forName("com.mysql.jdbc.Driver");			
           
           		//Create Connection to DB		
            	Connection con = DriverManager.getConnection(dbUrl,username,password);
          
          		//Create Statement Object		
        	   Statement stmt = con.createStatement();					
       
       			// Execute the SQL Query. Store results in ResultSet	
					stmt.executeUpdate(query1);
					stmt.executeUpdate(query2);	
					stmt.executeUpdate(query3);		
					stmt.executeUpdate(query4);
					stmt.executeUpdate(query61);
					stmt.executeUpdate(query5);	
					stmt.executeUpdate(query6);
					stmt.executeUpdate(query7);
					stmt.executeUpdate(query8);	
					stmt.executeUpdate(query9);
					stmt.executeUpdate(query10);
					stmt.executeUpdate(query11);	
					stmt.executeUpdate(query12);
					stmt.executeUpdate(query13);
					stmt.executeUpdate(query14);	
					stmt.executeUpdate(query15);
					stmt.executeUpdate(query16);
					stmt.executeUpdate(query17);
         		// While Loop to iterate through all data and print results		
//				while (rs.next()){
//			        		String myName = rs.getString(1);								        
//                            String myAge = rs.getString(2);					                               
//                            System. out.println(myName+"  "+myAge);		
//                    }		
					System.out.println("SQL token limas DB clean up done");
      			 // closing DB Connection		
      			con.close();			
		}
}