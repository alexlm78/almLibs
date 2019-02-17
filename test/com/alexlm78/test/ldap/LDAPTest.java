package com.alexlm78.test.ldap;

import java.util.HashSet;
import java.util.Map;

/**
 * Example code for retrieving a Users Primary Group
 * from Microsoft Active Directory via. its LDAP API
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class LDAPTest {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		final String ldapAdServer = "ldap://clarogt.americamovil.ca1";
		final String ldapSearchBase = "DC=clarogt,DC=americamovil,DC=ca1";
		
		final String ldapUsername = "N=eLearning,OU=Cuentas de Servicio,DC=clarogt,DC=americamovil,DC=ca1";
		final String ldapPassword = "Claro+123";
		
		final String ldapAccountToLookup = "jorgea.lopez@claro.com.gt";
		
		ADAuthenticator adAuthenticator = new ADAuthenticator();
		
		// Test bad password
		//System.out.println("Testing bad password...");
		//Map<String,Object> attrs = adAuthenticator.authenticate("SOMEUSER","badpassword");
		
		// Test good password
		System.out.println("Testing good password...");
		//"N=eLearning,OU=Cuentas de Servicio,DC=clarogt,DC=americamovil,DC=ca1"
		Map<String,Object> attrs = adAuthenticator.authenticate("jorgea.lopez","hermione+1.618");
		
		if (attrs != null) {
			for (String attrKey : attrs.keySet()) {
				if (attrs.get(attrKey) instanceof String) {
					System.out.println(attrKey +": "+attrs.get(attrKey));
				} else {
					System.out.println(attrKey +": (Multiple Values)");
					for (Object o : (HashSet)attrs.get(attrKey)) {
						System.out.println("\t value: " +o);
					}
				}
			}
		} else {
			System.out.println("Attributes are null!");
		}
		
	}
}
