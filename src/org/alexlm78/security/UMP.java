/**
 * Security package.
 */
package org.alexlm78.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.alexlm78.utils.Configurador;
import org.alexlm78.db.DBTask;
import org.apache.log4j.Logger;

/**
 * Users/Modules/Permissions
 * 
 * @author Alejandro López Monzón
 * @version 1.0
 */
public class UMP
{
	private static Logger log = Logger.getLogger(UMP.class);
	
	/**
	 * Authorize by module and user
	 * 
	 * @param user Username with authorization
	 * @param module Module to authorize
	 * @return Has the authorization or not.
	 */
	public static boolean Authorized( String user, String module )
	{
		boolean bRes=false;
		
		try
		{
			Properties props = Configurador.getPropiedades("PISA");
			String ipConnection = props.getProperty("IP");
			String usrConn = props.getProperty("User");
			String pasConn = props.getProperty("Pass");
			String schema = props.getProperty("Schema");
			String query = "SELECT * FROM JL637879.UMP WHERE UMPSTS='' AND UMPUSER='"+user.trim()+"' and UMPMODULE in ('*'"+(module.trim().length()==0 ? "" : ",'"+module.trim()+"'")+")";
			
			// Define the conection class
			String urlConn = "jdbc:as400://"+ ipConnection + "/;libraries="+ schema + ",PASO,GUAV1,GUARDBV1,TFSOBMX1,QTEMP;prompt=false;naming=sql;errors=full";
			DBTask db = new DBTask("com.ibm.as400.access.AS400JDBCDriver");
			db.Conectar(urlConn, usrConn, pasConn);
			Connection con = db.getConexion();
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			
			if( res.next() )
				bRes = true;
			
			res.close();
			st.close();
			con.close();
			
		}catch ( Exception ex )
		 {
			log.error(ex.getMessage());
			bRes = false;
		 }
		
		return bRes;
	}
	
	/**
	 * Global Security for Applicatin and Modules.
	 * 
	 * @return Access granted or denied 
	 */
	public static boolean Security()
	{
		boolean bRes=false;
		
		try
		{
			Properties props = Configurador.getPropiedades("PISA");
			String ipConnection = props.getProperty("IP");
			String usrConn = props.getProperty("User");
			String pasConn = props.getProperty("Pass");
			String schema = props.getProperty("Schema");
			String query = "SELECT * FROM GUAV1.MNUSER WHERE USRID='JL637879' AND USRSTS=''";
			
			// Definimos la forma de conexion de la clase
			String urlConn = "jdbc:as400://"+ ipConnection + "/;libraries="+ schema + ",PASO,GUAV1,GUARDBV1,TFSOBMX1,QTEMP;prompt=false;naming=sql;errors=full";
			DBTask db = new DBTask("com.ibm.as400.access.AS400JDBCDriver");
			db.Conectar(urlConn, usrConn, pasConn);
			Connection con = db.getConexion();
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(query);
			
			if( res.next() )
				bRes = true;
			
			res.close();
			st.close();
			con.close();
			
		}catch ( Exception ex )
		 {
			log.error(ex.getMessage());
			bRes = false;
		 }
		
		return bRes;
	}

}
