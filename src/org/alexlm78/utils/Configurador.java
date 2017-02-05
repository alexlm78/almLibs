package org.alexlm78.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Obtiene datos desde un archivo de propiedades (.properties)
 * 
 * @author Alejandro Lopez Monzon
 * @version 0.2
 * @category Utilidades
 */
public class Configurador
{
	/**
	 * Log para la clase!
	 */
	static Logger log = Logger.getLogger(Configurador.class);
	
	/**
	 * Obtiene las propiedades contenidas en el archivo con un objeto Properties.
	 * 
	 * @param Ident Nombre de identificaion del archivo, este es el nombre del archivo sin la extension .xml
	 * @return Objeto Properties con las propiedades definidas en el archivo.
	 * @throws Exception I/O en caso de no existir el archivo enviado de configuracion.
	 */
	public static Properties getPropiedades( String Ident ) throws Exception 
	{
		Properties properties = new Properties();
		
		// Se lee el archivo de texto con la definicion de propiedades.
		File file = new File(Ident.toLowerCase()+".xml");
		if ( file.exists() )
			properties.loadFromXML(new FileInputStream(file));
		else
			throw new IOException("El archivo con la configuracion para " + Ident + " no existe");
		
		// Se pasan por la validacion cada uno de los valores de llave por aquello de los parametros.
		/*Enumeration<Object> P = properties.elements();
		Object O = null;
		String S1,S2 = "";
		
		while ( P.hasMoreElements() )
		{
			O = P.nextElement();
			S1 = String.valueOf(O);
			System.out.print(S1+" - ");
			S2 = Configurador.validaPropiedad(String.valueOf(O));
			System.out.println(S2);
		}
		*/
		
		return properties;
	}
	
	/**
	 * Agrega una llave y valor a un archivo de configuracion.
	 * 
	 * @param Ident Identifcacion del archivo de configuracion.
	 * @param Llave Identificador para la propiedad.
	 * @param Valor Contenido de la propiedad.
	 * @throws Exception I/O en caso de no existir el archivo de configuracion.
	 */
	public static void setPropiedad( String Ident, String Llave, String Valor ) throws Exception
	{
		Properties proper = new Properties();
		File file= null;
		
		// Obtengo el contenido actual del archivo
		file = new File(Ident.toLowerCase()+".xml");
		if ( file.exists() )
			proper.loadFromXML(new FileInputStream(file));
		
		// Agrego la nueva configuracion y guardo nuevamente en el archivo.
		proper.setProperty(Llave.toUpperCase(), Valor);
		file = new File(Ident.toLowerCase()+".xml");
		proper.storeToXML(new FileOutputStream(file), "UTF-8");
	}
	
	/**
	 * Valida patrones de conversion del Configurador
	 * 
	 * @param proper Propiedad a Validar.
	 * @return Valor con los patrones cambiados.
	 */
	public static String validaPropiedad( String proper ) throws Exception
	{
		// :DD Dia 2 digitos :MM Mes 2 digitos; :YY A�o 2 digitos; :YYYY A�o 4 digitos.
		String[] tmp;
		Date hoy = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now =  sdf.format(hoy);
		String dia = now.substring(6, 8);
		String mes = now.substring(4, 6);
		String ano = now.substring(0, 4);
		
		tmp=proper.split(":");
		String tmp2="";
		
		for ( String tm : tmp )
		{
				 if ( tm.equals("DD") )
				tmp2 += dia;
			else if ( tm.equals("MM") )
				tmp2 +=mes;
			else if ( tm.equals("YY") )
				tmp2 += ano.substring(2,4);
			else if ( tm.equals("YYYY") )
				tmp2 += ano;
			else
				tmp2 += tm;
		}
		
		return tmp2;
	}
}
