package org.alexlm78.kmail;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class KMail 
{
	// Logger.
	private static Logger log = Logger.getLogger(KMail.class);
	// Debuger
	private boolean DEBUG = false;
	
	// Datos de conexion del server.
	private String Host;
	private String User;
	private String Password;
	private String Port;
	private String Mail;
	// Otras opciones de configuracion
	private Boolean TLS = false;
	
	// Datos de contenido del correo.
	private ArrayList<String> Destinatarios;
	private ArrayList<String> ConCopias;
	private ArrayList<String> ConCopiaOcultas;
	private ArrayList<String> Adjuntos;
	private String Asunto;
	private String Texto;
	Properties props = new Properties();
	
	public KMail( String host, String user, String pass, String mail )
	{
		this(host, user, pass, mail, "25");
	}
	
	public KMail( String host, String user, String pass, String mail, String port)
	{
		setMail(mail);
		setHost(host);
		setUser(user);
		setPassword(pass);
		setPort(port);
		Destinatarios = new ArrayList<String>();
		ConCopias = new ArrayList<String>();
		ConCopiaOcultas = new ArrayList<String>();
		Adjuntos = new ArrayList<String>();
	}
	
	public void enviar()
	{			
		try
		{
			if(isDEBUG()) log.debug(Host+":"+User+":"+Mail+":"+Port);	
			setConfiguracion();
			Authenticator auth = new autentificadorSMTP();
			//Session mailSession = javax.mail.Session.getInstance(System.getProperties(), auth );
			Session mailSession = javax.mail.Session.getDefaultInstance(props, auth);
			MimeMessage message = new MimeMessage(mailSession);
			
			// Agregamos el cuerpo del mensaje.
			message.setText(getTexto());//, "ISO-8859-1"
			if(isDEBUG()) log.debug(getTexto());
			// Agregamos el asunto
			message.setSubject(getAsunto());//, "ISO-8859-1"
			if(isDEBUG()) log.debug(getAsunto());
						
			// Adjunto
			if ( Adjuntos.size() > 0 )
			{
				Multipart mp = new MimeMultipart();
			
				for ( String Adjunto : Adjuntos)
				{					
					MimeBodyPart adj =new MimeBodyPart();
					FileDataSource fds = new FileDataSource(Adjunto);
					adj.setDataHandler(new DataHandler(fds));
					adj.setFileName(fds.getName());
					if(isDEBUG()) log.debug(fds.getName());
					mp.addBodyPart(adj);
				}			
				// Agregamos los Adjuntos
				message.setContent(mp);
			}
			
			// Agregar el originario del mail
			message.setFrom(new InternetAddress(getMail()));
			// Los destinatarios
			for ( String Dest : Destinatarios)
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(Dest));
			
			// Los Copiados
			if ( ConCopias.size() > 0 )
				for ( String CC : ConCopias)
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(CC));
			
			// Los Copiados ocultos.
			if ( ConCopiaOcultas.size() > 0 )
				for ( String CCO : ConCopiaOcultas)
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(CCO));
			
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(Host, User, Password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			//Transport.send(message);
		}catch ( AddressException adex )
		 {
			log.error(adex.getMessage());			
		 }catch ( MessagingException mex )
		 {
			log.error(mex.getMessage());
		 }		
	}
	
	private void setConfiguracion()
	{
		props.put("mail.smtp.user", Mail);
        props.put("mail.smtp.host", Host);
        props.put("mail.smtp.port", Port);
        props.put("mail.smtp.auth", "true");
        if ( TLS ) 
        {
        	props.put("mail.smtp.starttls.enable", TLS.toString());
        	props.put("mail.smtp.socketFactory.port", Port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
        }
	}
	
	public void addDestinatario( String Dest )
	{
		Destinatarios.add(Dest);
	}
	
	public void addConCopia( String ConCopia )
	{
		ConCopias.add(ConCopia);		
	}
	
	public void addConCopiaOculta( String ConCopiaOculta )
	{
		ConCopiaOcultas.add(ConCopiaOculta);
	}
	
	public void addAdjunto(String adjuntos) 
	{
		if ( adjuntos.length() > 0 )
			Adjuntos.add(adjuntos);
	}

	public String getHost() {
		return Host;
	}

	public void setHost(String host) {
		Host = host;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	
	public void setMail(String mail) {
		Mail = mail;
	}

	
	public String getMail() {
		return Mail;
	}

	public String getAsunto() {
		return Asunto;
	}

	public void setAsunto(String asunto)
	{
		if ( asunto.contains("#AYER") )
		{
			Date hoy = Calendar.getInstance().getTime();
			long tiempoActual = hoy.getTime();
			long unDia = 1 * 24 * 60 * 60 * 1000;
			Date ayer = new Date(tiempoActual - unDia);
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Asunto = asunto.replaceAll("#AYER", sdf.format(ayer));
			//log.debug(sdf.format(ayer));
		}else
			Asunto = asunto;
	}

	public String getTexto() {
		return Texto;
	}

	public void setTexto(String texto) {
		Texto = texto;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}
	
	/**
	 * Get the mail configuration.
	 */
	public void getMailConf( String fileName )
	{
		String mailConfFile = (fileName == null || fileName.length()<=0) ? "mail.xml" : fileName;
		
		try
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(new File(mailConfFile));
	        
	        doc.getDocumentElement ().normalize ();
            //System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
            
            NodeList nList = doc.getElementsByTagName("MAIL");
            for (int temp = 0; temp < nList.getLength(); temp++) 
            {
            	Node nNode = nList.item(temp);
            	//System.out.println(nNode.toString());
            	if (nNode.getNodeType() == Node.ELEMENT_NODE) 
            	{
            		Element eElement = (Element) nNode;
            		
            		this.setAsunto(getTagValue("SUBJECT", eElement));
            		this.addAdjunto(getTagValue("ATTACH", eElement));
            		this.setTexto(getTagValue("BODY", eElement));
           		}
            }
            
            NodeList nToList = doc.getElementsByTagName("TO");

            for(int temp = 0 ; temp <nToList.getLength(); temp++)
            {
            	Node nNode = nToList.item(temp);
	            Element eElement = (Element) nNode;
	            NodeList childList = eElement.getChildNodes();
	            
	            for(int i = 0; i < childList.getLength(); i++)
	            {
	            	Node childNode = childList.item(i);
	            	if ( !childNode.getTextContent().trim().isEmpty() )
	            		addDestinatario(childNode.getTextContent());
	            }
            }
            
            NodeList nCcList = doc.getElementsByTagName("CC");

            for(int temp = 0 ; temp <nCcList.getLength(); temp++)
            {
            	Node nNode = nCcList.item(temp);
	            Element eElement = (Element) nNode;
	            NodeList childList = eElement.getChildNodes();
	            
	            for(int i = 0; i < childList.getLength(); i++)
	            {
	            	Node childNode = childList.item(i);
	            	if ( !childNode.getTextContent().trim().isEmpty() )
	            		addConCopia(childNode.getTextContent());
	            }
            }
            
            NodeList nCcoList = doc.getElementsByTagName("CCO");

            for(int temp = 0 ; temp <nCcoList.getLength(); temp++)
            {
            	Node nNode = nCcoList.item(temp);
	            Element eElement = (Element) nNode;
	            NodeList childList = eElement.getChildNodes();
	            
	            for(int i = 0; i < childList.getLength(); i++)
	            {
	            	Node childNode = childList.item(i);
	            	if ( !childNode.getTextContent().trim().isEmpty() )
	            		addConCopiaOculta(childNode.getTextContent());
	            }
            }
          
		}catch (SAXParseException err) 
		{
			log.error("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			log.error(" " + err.getMessage ());
		}catch (SAXException e) 
		{
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
			log.error(e.getMessage());			
		}catch (Throwable t) 
		{
			t.printStackTrace ();
			log.error(t.getMessage());
		}
	}
	
	/**
	 * Get ths specific tag elemento on a XML node.
	 * 
	 * @param sTag	Tag on node
	 * @param eElement Elemento on the node
	 * @return Tag value on the element.
	 */
	private String getTagValue(String sTag, Element eElement) 
	{
	    NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    return nValue.getNodeValue();
	}
	
	/**
	 * Authentication for SMPT
	 * 
	 * @author Alejandro Lopez Monzon
	 */
	private class autentificadorSMTP extends javax.mail.Authenticator 
	{
        public PasswordAuthentication getPasswordAuthentication() 
        {
            return new PasswordAuthentication(User, Password);
        }
    }

	/**
	 * @return the dEBUG 9872
	 */
	public boolean isDEBUG() {
		return DEBUG;
	}

	/**
	 * @param dEBUG the dEBUG to set
	 */
	public void setDEBUG(boolean debug) {
		DEBUG = debug;
	}
}
