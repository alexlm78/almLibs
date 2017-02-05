package org.alexlm78.pisa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.alexlm78.utils.Configurador;
import org.apache.log4j.Logger;

/**
 * Genera una conexion hacia PISA.
 * 
 * @author Alex
 */
public class DBPISA 
{
	/**
	 * Log de la clase.
	 */
	private static Logger log = Logger.getLogger(DBPISA.class);
	// Datos de conexion a la DB
	private String ipConnection;
	private String usrConn;
	private String pasConn;
	private String Schema;
	private String urlConn;
	// Conexion.
	//private Connection dbConn=null;
	
	/**
	 * Contructor
	 */
	public DBPISA( )
	{
		try 
		{
			// Datos se obtienen del archivo pisa.xml
			Properties props = Configurador.getPropiedades("pisa");
			ipConnection = props.getProperty("IP");
			usrConn = props.getProperty("User");
			pasConn = props.getProperty("Pass");
			Schema = props.getProperty("Schema");
			// Conexion a la DB.
			urlConn = "jdbc:as400://"+ ipConnection + "/;libraries="+Schema+",GUAV1,GUARDBV1,TFSOBMX1,QTEMP;prompt=false;naming=sql;errors=full";
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
									
		}catch (Exception ex) 
		 {
			log.error(ex.getMessage());
		 }
		
	}

	/**
	 * Entrega la conexion generada a PISA
	 * 
	 * @return Objeto con la conexion establecida.
	 */
	public Connection getConexion() 
	{		
		try 
		{
			log.debug("Conexion establecida para: "+usrConn+"@"+ipConnection);
			return DriverManager.getConnection(urlConn, usrConn, pasConn);
		}catch (SQLException e) 
		{
			log.error(e.getMessage());
			return null;
		}
	}	
}
