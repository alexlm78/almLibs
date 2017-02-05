package org.alexlm78.utils;

import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Control y manejo de fechas en DWH.
 * 
 * @author Alejandro Lopez Monzon
 */
public class ManejoFechas 
{
	/**
	 * Variable para el logger de la clase (log4j).
	 */
	static Logger log = Logger.getLogger(ManejoFechas.class);
	
	/**
	 * Obtenemos las particiones en caso necesario.
	 * 
	 * @param inicio Fecha inicial.
	 * @param fin Fecha final.
	 * @return Array de String con el contenido.
	 * @throws Exception Caso de problemas.
	 */
	public static ArrayList<String> getParticiones( int inicio, int fin ) throws Exception
	{
		ArrayList<String> rVal = new ArrayList<String>();
		int difDias=0;
		
		String iAnio = getAnio(inicio);
		String fAnio = getAnio(fin);
		
		// Si la fecha final ocurre antes de la incial.
		if ( Integer.parseInt(fAnio) < Integer.parseInt(iAnio) )
			throw new Exception("La fecha final ocurre antes que la inicial.");
		
		// Involucra dos años distitos, por lo que lo partimos y procesamos recursicamente.
		if ( Integer.parseInt(fAnio) > Integer.parseInt(iAnio) )
		{
			rVal.addAll( getParticiones(inicio, getFinAnio(Integer.parseInt(iAnio))) );
			rVal.addAll( getParticiones(getInicioAnio(Integer.parseInt(fAnio)), fin) );
		}
		
		// Dentro del mismo año.
		if ( getAnio(fin).compareTo(getAnio(inicio)) == 0 )
		{
			difDias = Integer.parseInt(getAnio(inicio)+getMes(inicio));
			
			while(true)
			{
				if ( Integer.parseInt(getAnio(fin)+getMes(fin)) != difDias )
				{
					rVal.add("P" + difDias);
					difDias++;
				}else
				 {
					rVal.add("P" + difDias);
					break;
				 }			
			}
		}
		
		return rVal;		
	}
	
	/**
	 * Crea un array de las fechas para concordar con las particiones.
	 * 
	 * @param inicio Fecha inicial.
	 * @param fin Fecha final.
	 * @return Fechas separadas por meses.
	 * @throws Exception Fechas invalidas.
	 */
	public static ArrayList<ArrayList<Integer>> getFechas( int inicio, int fin ) throws Exception
	{
		ArrayList<ArrayList<Integer>> rVal = new ArrayList<ArrayList<Integer>>();
		
		String iAnio = getAnio(inicio);
		String fAnio = getAnio(fin);
		
		if ( Integer.parseInt(fAnio)-Integer.parseInt(iAnio) >= 2 )
			throw new Exception("El periodo de generacion no puede exceder de 2 años completos.");
		
		if ( Integer.parseInt(fAnio) < Integer.parseInt(iAnio) )
			throw new Exception("La fecha final ocurre antes que la inicial.");
		
		if ( Integer.parseInt(fAnio) > Integer.parseInt(iAnio) )
		{
			rVal.addAll( getFechas(inicio, getFinAnio(Integer.parseInt(iAnio))) );
			rVal.addAll( getFechas(getInicioAnio(Integer.parseInt(fAnio)), fin) );
		}
		
		if ( getAnio(fin).compareTo(getAnio(inicio)) == 0 )
		{
			int meses = Integer.parseInt(getMes(fin))-Integer.parseInt(getMes(inicio)) + 1;
			int iCont = Integer.parseInt(getMes(inicio));
					
			while ( true )
			{
				if( meses == 1 )
				{
					ArrayList<Integer> tVal = new ArrayList<Integer>();
					tVal.add(inicio);
					tVal.add(fin);
					rVal.add(tVal);
					break;
				}else
				 {
					if ( iCont == Integer.parseInt(getMes(inicio)) )	// Primer Mes.
					{
						ArrayList<Integer> tVal = new ArrayList<Integer>();
						tVal.add(inicio);
						tVal.add(Integer.parseInt(getAnio(inicio)+getFinMes(Integer.parseInt(getMes(inicio)))));
						rVal.add(tVal);
					}else if ( iCont == Integer.parseInt(getMes(fin)) )	// Ultimo mes.
					{
						ArrayList<Integer> tVal = new ArrayList<Integer>();
						tVal.add(Integer.parseInt(getAnio(fin)+getMes(fin)+"01"));
						tVal.add(fin);
						rVal.add(tVal);
						break;
					}else
					{
						ArrayList<Integer> tVal = new ArrayList<Integer>();
						tVal.add(Integer.parseInt(getAnio(inicio)+(iCont<10?"0"+iCont:iCont)+"01"));
						tVal.add(Integer.parseInt(getAnio(inicio)+getFinMes(iCont)));
						rVal.add(tVal);
					}
				 }
				
				iCont++;
			}
		}
		
		return rVal;
	}
	
	/**
	 * Cambia el formato numerico de una fecha al formato separado por guiones.
	 * 
	 * @param fech fecha en formato numerido (yyyymmdd)
	 * @return Fecha convertida en formato (dd-mm-yyyy)
	 */
	public static String NumFechToGuionFech( int fech )
	{
		return Integer.toString(fech).substring(6,8) +"-"+ Integer.toString(fech).substring(4,6) + "-"+ Integer.toString(fech).substring(0,4);
	}
	
	/**
	 * Obtiene el mes de una fecha en formato numerico (yyyymmdd).
	 * 
	 * @param fecha Fecha fecha en formato numerico
	 * @return El mes de la fecha enviada
	 */
	public static String getMes( int fecha )
	{
		return  Integer.toString(fecha).substring(4,6);
	}
	
	/**
	 * Obtiene el año de una fecha en formato numerico (yyyymmdd).
	 * 
	 * @param fecha Fecha fecha en formato numerico.
	 * @return El mes de la fecha enviada.
	 */
	public static String getAnio( int fecha )
	{
		return  Integer.toString(fecha).substring(0,4);
	}
	
	/**
	 * Obtiene el dia de una fecha en formato numerico (yyyymmdd).
	 * 
	 * @param fecha Fecha fecha en formato numerico.
	 * @return El mes de la fecha enviada.
	 */
	public static String getDia( int fecha )
	{
		return  Integer.toString(fecha).substring(6,8);
	}
	
	/**
	 * Obtiene la fecha del ultimo dia del año.
	 * 
	 * @param anio Año a considerar.
	 * @return fecha en numerico del fin de año.
	 */
	public static int getFinAnio( int anio )
	{
		return (anio*10000 + 1200 + 31);
	}
	
	/**
	 * Obtiene la fecha del primer dia del año.
	 * 
	 * @param anio Año a considerar.
	 * @return fecha en numerico del fin de año.
	 */
	public static int getInicioAnio( int anio )
	{
		return (anio*10000 + 100 + 1);
	}

	/**
	 * Obtiene el fin de mes de mes.
	 * 
	 * @param mes Mes solicitado.
	 * @return Fecha de fin de mes.
	 */
	public static String getFinMes( int mes )
	{
		int diasMes[] = {31,28,31,30,31,30,31,31,30,31,30,31};
		String sMes = (mes<10 ? "0"+Integer.toString(mes) : Integer.toString(mes) );
		
		return sMes + Integer.toString(diasMes[mes-1]);
	}
	
	/**
	 * Obtiene el objeto LocalDate desde un objecto Date
	 * Ex:
	 * 	java.util.Date: Thu Oct 06 10:59:16 PDT 2016 
	 * 	java.time.LocalDate: 2016-10-06
	 * 
	 * @param date Fecha a convertir
	 * @return Objeto con el formato LocalDate.
	 */
	public static java.time.LocalDate Date2LocalDate( java.util.Date date )
	{
		java.time.Instant instant = java.time.Instant.ofEpochMilli(date.getTime()); 
		java.time.LocalDateTime localDateTime = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault()); 
		java.time.LocalDate localDate = localDateTime.toLocalDate(); 
		
		return localDate;
	}
}
