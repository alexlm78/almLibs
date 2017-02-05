package org.alexlm78.utils;

public class DBProps 
{
	/**
	 * Propiedades
	 */
	private String IP;
	private String User;
	private String Pass;
	private String Schema;
	
	/**
	 * Obtiene la IP del servidor de conexion.
	 * 
	 * @return IP de Servidor
	 */
	public String getIP()
	{
		return IP;
	}
	
	/**
	 * Establece la IP del Servidor
	 * 
	 * @param iP IP del Servidor
	 */
	public void setIP(String iP) 
	{
		IP = iP;
	}
	
	/**
	 * Obtiene el usuario de conexion.
	 * 
	 * @return the Usuario
	 */
	public String getUser() 
	{
		return User;
	}
	
	/**
	 * Establece el usuario de conexion.
	 * 
	 * @param user Usuario
	 */
	public void setUser(String user) 
	{
		User = user;
	}
	
	/**
	 * Obtiene la password de conexion.
	 * 
	 * @return the Password
	 */
	public String getPass() 
	{
		return Pass;
	}
	
	/**
	 * Establece la password de conexion
	 * 
	 * @param pass Passwword
	 */
	public void setPass(String pass) 
	{
		Pass = pass;
	}
	
	/**
	 * Obtiene el Schema de conexion.
	 * 
	 * @return the Schema
	 */
	public String getSchema() 
	{
		return Schema;
	}
	
	/**
	 * Establece el Schema de conexion.
	 * 
	 * @param schema Schema
	 */
	public void setSchema(String schema) 
	{
		Schema = schema;
	}
}
