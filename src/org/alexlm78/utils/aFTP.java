package org.alexlm78.utils;

//import java.io.File;
//import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * Transferidor de archivos por FTP
 * 
 * @author Alejandro Lopez Monzon (based on fileTransfer by FBE)
 */
public class aFTP 
{
	/**
	 * Variable para el logger de clase (log4j)
	 */
	private Logger log = Logger.getLogger(aFTP.class);
	/**
	 * pivoteFtp
	 */
	public boolean pivoteFtp = false;
	/**
	 *  Estado del Cliente FTP.
	 */
	public String ftpClientEstatus  = "";
	/**
	 * Respuesta del FTP Server.
	 */
	public String ftpResponse = "";
	
	/**
	 * Cliente de FTP para envio.
	 */
	private FTPClient ftpClient;
	//
	private boolean DEBUG;
	
	/**
	 * Constructor
	 * 
	 * @param hostName Nombre del Host a donde conectar.
	 * @param userId Identificador del usuario.
	 * @param password Clave del usuario para conexion.
	 */
	public aFTP(String host, String user, String pass)
	{
		try 
		{
			ftpClient = new FTPClient();
			ftpClient.connect(host);
			log.info("[FTP]: Conectando al servidor: " + host);
			ftpClient.login(user, pass.trim());
			log.info("[FTP]: Autenticacion exitosa a "+ host +" con el usuario "+ user);
			
			if ( ftpClient.isConnected() )
			{
				ftpClientEstatus = ftpClient.getStatus();
				this.setMode( "ascii" );
				pivoteFtp = true;
			}
		}catch (Exception ex) 
		 {
			ftpClientEstatus = ex.getMessage().trim();
			log.error("[FTP]: " + ftpClientEstatus);
			pivoteFtp = false;
		 }
	}
	
	/**
	 * Constructor (con autenticacion anonima)
	 * 
	 * @param host Nombre del Host a donde conectar.
	 */
	public aFTP(String host)
	{
		this(host, "anonymous", "anonymous");
	}
	
	/**
	 * Establece el modo de operacion. Binario o ASCII
	 * 
	 * @param mod Modo de operacion bin (binario) ascii (ASCII)
	 */
	public void setMode( String mod )
	{
		try
		{
			if ( mod.equalsIgnoreCase("bin") )
				ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
			if ( mod.equalsIgnoreCase("ascii") )
				ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.ASCII_FILE_TYPE);
		}catch ( IOException ex )
		 {
			log.error("[FTP]: " + ex.getMessage());
		 }
	}
	
	/**
	 * Desconecta del servidor de FTP.
	 */
	public void logoff() 
	{
		try 
		{
			if (ftpClient.isConnected())
			{
				if(DEBUG) log.debug("[FTP] Desconectando del servidor");
				ftpClient.logout();
			}
		}catch (IOException ex) 
		 {
			log.error("[FTP]: " + ex.getMessage());
		 }
	}
	
	/**
	 * Obtiene un archivo desde el servidor.
	 * 
	 * @param dirFile Directorio donde se encuentra el archivo.
	 * @param hostNameFile Nombre del archivo en el remoto.
	 * @param localNameFile Nombre del archivo en local.
	 */
	public boolean getFile(String dirFile, String remoteFile, String localFile) 
	{
		boolean res;
		try 
		{
			if(DEBUG) log.error("[FTP]: Obteniendo: " + chkTargetDir(dirFile) + remoteFile + " --> " + localFile);
			ftpClient.changeWorkingDirectory(dirFile);
			if ( ftpClient.listFiles(remoteFile).length>0 )
			{
				OutputStream output;
	            output = new FileOutputStream(localFile);
	            res = ftpClient.retrieveFile(remoteFile, output);
	            output.close();
	            res=true;
			}else
			 {
				log.error("Archivo "+remoteFile+" no encontrado");
				res=false;
			 }
		}catch (Exception ex)
		 {
			log.error(ex.getMessage());
			res = false;
		 }
		return res;
	}
	
	/**
	 * Pone un archivo en el servidor de FTP.
	 * 
	 * @param dirFile Directorio donde se pondra el archivo
	 * @param localNameFile Nombre de archivo local.
	 * @param hostNameFile Nombre de archivo remoto.
	 * @return Exito o fracaso.
	 */
	@SuppressWarnings("all")
	public boolean putFile(String dirFile, String localFile, String remoteFile) 
	{
		try 
		{
			if(DEBUG) log.debug("[FTP]: Enviando: " + localFile + " --> " + chkTargetDir(dirFile) + remoteFile);
			ftpClient.changeWorkingDirectory(dirFile);
			InputStream in = new FileInputStream(localFile);
			return ftpClient.storeFile(remoteFile, in); 
		}catch (Exception ex) 
		 {
			log.error(ex.getMessage());
			return false;
		 }
	}
	
	/**
	 * Elimina un archivo remoto.
	 * 
	 * @param arch Nombre del archivo remoto a eliminar.
	 * @return Resultado de la eliminacion.
	 */
	public boolean deleteFile( String arch )
	{
		boolean result = false; 
				
		try 
		{
			result = ftpClient.deleteFile(arch);
		}catch (IOException e) 
		 {
			log.error("[FTP]: " + e.getMessage());
		 }
		return result;
	}
	
	public boolean isConnected()
	{
		return ftpClient.isConnected();
	}
	
	/**
	 * Verifica que en el directorio se puede agregar un subdirectorio, es decir que termine en /.
	 * 
	 * @param sDir Directorio de destino.
	 * @return Directorio de destino ya revisado y actualizado si es necesario.
	 */
	public String chkTargetDir( String sDir )
	{
		char [] cDir;
		cDir = sDir.toCharArray();
		if ( cDir[sDir.length()-1] != '/' )
			sDir += '/';
		return sDir;		
	}
	
	public void activeDebug()
	{
		DEBUG = true;
	}
	
	public void disconnect()
	{
		try 
		{
			ftpClient.disconnect();
		} catch (IOException e) 
		{
			log.error(e.getMessage());
		}
	}
}
