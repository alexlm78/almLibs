package org.alexlm78.utils;

/**
 * Funciones de Cadenas
 * 
 * @author Alejandro Lopez Monzon
 * @version 1.0
 * @category Utilitarios.
 */
public class Cadenas 
{
	/**
	 * Invierte la escritura de una cadena.
	 * 
	 * @param str Cadena a invertir
	 * @return Cadena invertida resultante.
	 */
	public static String Invertir ( String str )
	{
		String sInvertida = "";
		
		if ( str.length() > 0 )
			for (int x=str.length()-1; x>=0; x--)
				sInvertida = sInvertida + str.charAt(x);
		else
			sInvertida = str;
		
		return sInvertida;
	}
	
	/**
	 * Valua si el contenido de una cadena es Entero.
	 * 
	 * @param str Cadena a comprobar.
	 * @return True si es entero o false si no lo es.
	 */
	public static boolean esInteger(String str) 
	{
	    return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
	}

}
