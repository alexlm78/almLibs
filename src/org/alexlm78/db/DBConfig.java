package org.alexlm78.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class DBConfig 
{
	private static Logger log = Logger.getLogger(DBConfig.class);
	
	public static ArrayList<String> getMailSetting()
	{
		Connection c = null;
	    Statement stmt = null;
	    ArrayList<String> info = new ArrayList<String>();
	    
	    try 
	    {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:Kreaker.db");
	    	c.setAutoCommit(false);
	    	log.info("Opened database successfully");

	    	stmt = c.createStatement();
	    	ResultSet rs = stmt.executeQuery( "SELECT * FROM MAIL;" );

	    	if ( rs.next() ) 
	    	{
	    		info.add(rs.getString("SERVER"));
	    		info.add(rs.getString("USER"));
	    		info.add(rs.getString("PASS"));
	    		info.add(rs.getString("MAIL"));
	    	}
	    	rs.close();
	    	stmt.close();
	    	c.close();
	    }catch ( Exception e ) 
	    {
	    	log.error(e.getClass().getName() + ": " + e.getMessage());
	    	return null;
	    }
	    
	    return info;
	}
}
