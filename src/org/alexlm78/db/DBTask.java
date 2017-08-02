package org.alexlm78.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Administrador de tareas para la DB.
 *  
 * @author Alejandro Lopez Monzon
 * @version 0.2
 * @category DataBases
 */
public class DBTask
{
	/**
	 * Variable para el logger de clase (log4j)
	 */
	public Logger log = Logger.getLogger(DBTask.class);
	/**
	 * Control permanente de la conexcion a la DB.
	 */
	private Connection dbConn=null;
	/**
	 * Estado del registro del JDBC.
	 */
	private boolean registroJDBC=false;
	private boolean DEBUG = false;

	/**
	 * Contructor.
	 */
	public DBTask()
	{
		if ( !registroJDBC )
			log.warn("El estado del registro del puente JDBC esta marcado como " + registroJDBC + " por lo que es necesario registrarlo.");
	}
	
	/**
	 * Contructor con definicion de la clase para la creacion del puente JDBC.
	 * 
	 * @param driver Clase para la creacion del puente JDBC.
	 */
	public DBTask( String driver )
	{
		this.registrarJDBC(driver);
	}
	
	/**
	 * Registra el Puente JDBC para la base de datos.
	 * 
	 * @param driver Cadena de conexion para el driver.
	 * @return Exito o fracaso.
	 */
	public void registrarJDBC(String driver)
	{
		try
		{
			if ( registroJDBC )
				log.warn("Ya existe un puente JDBC cargado, favor intente con una nueva instancia!.");
			else
			{
				Class.forName(driver);
				registroJDBC=true;
				if ( isDEBUG() ) log.debug("Se ha regitrado el puente JDBC: " + driver);
			}
		}catch(ClassNotFoundException ex)
		 {
			log.error(ex);
			registroJDBC=false;
		 }
	}
	
	/**
	 * Habilita una conexion con la base de datos.
	 * 
	 * @param url Direccion de conexion.
	 * @param user Usuario para conectarse.
	 * @param pass Password para el usuario.
	 * @return Instancia de la conexcion establecida.
	 */
	public void Conectar(String url,String user,String pass) //throws SQLException
	{
		try
		{
			if ( registroJDBC )
			{
				dbConn = DriverManager.getConnection(url,user,pass);
				if ( isDEBUG() ) log.debug("Conectado con " + dbConn.toString() + " mediante JDBC ("+url+")");
			}else
				throw new SQLException("No esta definido un puente JDBC!.");
		}catch ( SQLException ex )
		 {
			log.error(ex.getMessage());
		 }
	}
	
	/**
	 * Obtiene el enlace con la conexion a la DB.
	 * 
	 * @return Enlace con la conexion a la DB.
	 */
	public Connection getConexion()
	{		
		try 
		{
			if ( !dbConn.isClosed() )
				return dbConn;
		}catch (SQLException e) 
		 {
			log.error(e.getMessage());
			return null;
		 }
		
		return null;
	}
	
	/**
	 * Cierra una conexion establecida.
	 * 
	 * @param con Conexion para cerrar.
	 */
	public void Cerrar(Connection con)
	{
		try
		{
			con.close();
		}catch(SQLException ex)
		 {
			//ex.printStackTrace();
			log.error(ex);
		 }
	}
	
	/**
	 * Cierra la conexion establecida.
	 * 
	 * @param con Conexion para cerrar.
	 */
	public void Cerrar()
	{
		try 
		{
			if ( !dbConn.isClosed() )
				dbConn.close();
		}catch (SQLException sex) 
		 {
			log.error(sex.getMessage());
		 }
	}

	public boolean isDEBUG() {
		return DEBUG;
	}

	public void setDEBUG(boolean debug) {
		DEBUG = debug;
	}
}
