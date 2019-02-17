package com.alexlm78.test.ldap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class ADAuthenticator
{
	private String domain;
	private String ldapHost;
	private String searchBase;
	
	public ADAuthenticator() {
		this.domain = "claro.com.gt";
		this.ldapHost = "ldap://clarogt.americamovil.ca1";
		this.searchBase = "OU=Users,DC=clarogt,DC=americamovil,DC=ca1"; // YOUR SEARCH BASE IN LDAP
	}
	
	public ADAuthenticator(String domain, String host, String dn)
	{
		this.domain = domain;
		this.ldapHost = host;
		this.searchBase = dn;
	}
	/*
	public boolean Authenticate(String domain,String dns, String user, String pass) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		try {
			if (pass == null || user == null)
				return false;
			
			if (pass.compareTo("") == 0 || user.compareTo("") == 0)
				return false;
			
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITCTX);
			env.put(Context.PROVIDER_URL, "ldap://" + dns);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, new String(domain + "\\" + user));
			env.put(Context.SECURITY_CREDENTIALS, new String(pass));
			
			new InitialDirContext(env);
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			
			// seguimiento del error

//			System.out.println();
//			System.out.println("Trato de loguearse " + user);
//			System.out.println("con clave " + pass);
//			System.out.println("en el dominio " + domain);
//			System.out.println("en el host " + dns);
//			System.out.println("surgio el problema");
//			System.out.println((e.getMessage() == null) ? "" : e.getMessage());
			
			return false;
		} finally {
			env = null;

//			System.out.println();
//			System.out.println("Trato de loguearse " + user);
//			System.out.println("con clave " + pass);
//			System.out.println("en el dominio " + domain);
//			System.out.println("en el host " + dns);
		}
	}*/
	
	public Map<String, Object> authenticate(String user, String pass) {
		String returnedAtts[] ={ "sn", "givenName", "name", "userPrincipalName", "displayName", "memberOf" };
		String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";
		
		// Create the search controls
		SearchControls searchCtls = new SearchControls();
		searchCtls.setReturningAttributes(returnedAtts);
		
		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapHost);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
		env.put(Context.SECURITY_CREDENTIALS, pass);
		
		LdapContext ctxGC = null;
		
		try {
			
			// This is the actual Authentication piece. Will throw javax.naming.AuthenticationException
			// if the users password is not correct. Other exceptions may include IO (server not found) etc.
			ctxGC = new InitialLdapContext(env, null);
			
			// Now try a simple search and get some attributes as defined in returnedAtts
			NamingEnumeration<SearchResult> answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				Map<String, Object> amap = null;
				if (attrs != null) {
					amap = new HashMap<String,Object>();
					NamingEnumeration<?> ne = attrs.getAll();
					while (ne.hasMore()) {
						Attribute attr = (Attribute) ne.next();
						if (attr.size() == 1) {
							amap.put(attr.getID(), attr.get());
						} else {
							HashSet<String> s = new HashSet<String>();
							NamingEnumeration n =  attr.getAll();
							while (n.hasMoreElements()) {
								s.add((String)n.nextElement());
							}
							amap.put(attr.getID(), s);
						}
					}
					ne.close();
				}
				ctxGC.close();  // Close and clean up
				return amap;
			}
		} catch (NamingException nex) {
			nex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
}