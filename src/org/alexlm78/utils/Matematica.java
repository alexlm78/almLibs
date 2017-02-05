package org.alexlm78.utils;

/**
 * Funciones Matematicas
 * 
 * @author Alejandro Lopez Monzon
 * @version 0.1
 */
public class Matematica 
{
	/**
	 * Factorial de n
	 * 
	 * @param n Numero para el cual se quiere el factorial.
	 * @return factorial de n
	 */
	public static Integer Factorial( Integer n )
	{
		if ( n == 0)
			return 1;
		else
			return n*Factorial(n-1);		
	}
	
	/**
	 * Obtiene un elemento puntual en la sucecion de Fibonacci.
	 * 
	 * @param n Numero de elemento en la sucecion de Fibonacci (0 -> Infinito)
	 * @return Elemento que ocupa la posicion solicitada en la sucesion de Fibonacci.
	 */
	public static Integer Fibonacci( Integer n )
	{
		Integer iRes=0;
		
		if ( n == 0 )
			iRes = 0;
		
		if ( n == 1 )
			iRes = 1;
		
		if ( n > 2 )
			iRes = Fibonacci(n-1)+Fibonacci(n-2);
		
		return iRes;
	}	
}
