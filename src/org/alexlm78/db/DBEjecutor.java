package org.alexlm78.db;

import org.apache.log4j.Logger;

public class DBEjecutor 
{
	private static Logger log = Logger.getLogger(DBEjecutor.class);
	
	public String getDriver( String nombre )
	{
		String Driver = "";
		
		if( Driver.equalsIgnoreCase("DB2") )
			Driver = "com.ibm.as400.access.AS400JDBCDriver";
		
		log.info("");
		return Driver;
	}
	
	public int updateExec( String query )
	{
		return 0;
	}
}
