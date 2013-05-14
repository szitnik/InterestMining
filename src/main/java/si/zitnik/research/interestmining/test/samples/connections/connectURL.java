package si.zitnik.research.interestmining.test.samples.connections;//=====================================================================
//
//  File:    connectURL.java      
//  Summary: This Microsoft JDBC Driver for SQL Server sample application
//	     demonstrates how to connect to a SQL Server database by using
//	     a connection URL. It also demonstrates how to retrieve data 
//	     from a SQL Server database by using an SQL statement.
//
//---------------------------------------------------------------------
//
//  This file is part of the Microsoft JDBC Driver for SQL Server Code Samples.
//  Copyright (C) Microsoft Corporation.  All rights reserved.
//
//  This source code is intended only as a supplement to Microsoft
//  Development Tools and/or on-line documentation.  See these other
//  materials for detailed information regarding Microsoft code samples.
//
//  THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF 
//  ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
//  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//  PARTICULAR PURPOSE.
//
//===================================================================== 

import java.sql.*;

public class connectURL {

	public static void main(String[] args) {
		
		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://192.168.25.131\\SQLEXPRESS:1433;databaseName=interestmining;user=sa;password=xs;";

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
        	try {
        		// Establish the connection.
        		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            		con = DriverManager.getConnection(connectionUrl);

                //INSERT
                String SQL = "INSERT INTO dbo.Users VALUES ('mirko', 'x:0.3;ffff:0.98;fff:0.7')";
                stmt = con.createStatement();
                int updatedNo = stmt.executeUpdate(SQL);
                if (updatedNo != 1) {
                    System.out.println("PROBLEM!!!");
                }

                // SELECT
            		SQL = "SELECT * FROM dbo.Users";
            		stmt = con.createStatement();
            		rs = stmt.executeQuery(SQL);
            		while (rs.next()) {
            			System.out.println(rs.getString(1) + " " + rs.getString(2));
            		}


        	}
        
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null) try { rs.close(); } catch(Exception e) {}
	    		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	    		if (con != null) try { con.close(); } catch(Exception e) {}
		}
	}
}

