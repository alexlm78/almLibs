package org.alexlm78.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class NetWorking 
{
	private final static char base64Array [] = 
	{
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
		'w', 'x', 'y', 'z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', '+', '/'
	};
	
	private static String base64Encode (String string) 
	{
		String encodedString = "";
		byte bytes [] = string.getBytes ();
		int i = 0;
		int pad = 0;
		while (i < bytes.length) 
		{
			byte b1 = bytes [i++];
			byte b2;
			byte b3;
			if (i >= bytes.length) 
			{
				b2 = 0;
				b3 = 0;
				pad = 2;
			}else 
			 {
				b2 = bytes [i++];
				if (i >= bytes.length) 
				{
					b3 = 0;
					pad = 1;
	            }else
	            	b3 = bytes [i++];
			 }
			
			byte c1 = (byte)(b1 >> 2);
			byte c2 = (byte)(((b1 & 0x3) << 4) | (b2 >> 4));
			byte c3 = (byte)(((b2 & 0xf) << 2) | (b3 >> 6));
			byte c4 = (byte)(b3 & 0x3f);
			encodedString += base64Array [c1];
			encodedString += base64Array [c2];
			switch (pad) 
			{
				case 0:
					encodedString += base64Array [c3];
					encodedString += base64Array [c4];
					break;
				case 1:
					encodedString += base64Array [c3];
					encodedString += "=";
					break;
				case 2:
					encodedString += "==";
					break;
	       }
		}
		return encodedString;
	}
	
	public String userNamePasswordBase64(String username, String password)
	{
		return "Basic " + base64Encode (username + ":" + password);
	}
	
	@SuppressWarnings("deprecation")
	public void enviarSMS(String origen,String destino,String mensaje)
	{
		try
		{
			System.getProperties().put( "proxySet", "true" );
			System.getProperties().put( "proxyHost", "192.168.2.225" );
			System.getProperties().put( "proxyPort", "8080" );
			URL claro = new URL("http://www.claro.com.gt/mensaje.php");
			URLConnection claroCon = claro.openConnection();
			
			claroCon.setRequestProperty( "Proxy-Authorization", userNamePasswordBase64("ramos.drixdel","Heubdpe1"));
			claroCon.setRequestProperty("Host", "www.claro.com.gt");
			claroCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String aux="nombre="+URLEncoder.encode(origen)+"&telefono="+URLEncoder.encode(destino)+"&msg="+URLEncoder.encode(mensaje)+"&B1="+ URLEncoder.encode("E n v i a r");
			claroCon.setRequestProperty("Content-Length", String.valueOf(aux.length()) );
			claroCon.setDoInput( true );
            claroCon.setDoOutput( true );
			DataOutputStream dos = new DataOutputStream(claroCon.getOutputStream());
			System.out.println(aux);
			dos.writeBytes(aux);
			DataInputStream input = new DataInputStream(claroCon.getInputStream() );
			
			// read in each character until end-of-stream is detected
			for( int c = input.read(); c != -1; c = input.read() )
			{
				System.out.print( (char)c );
			}
			
			input.close();
			dos.close();
			System.out.println("sms enviado");
		}catch (MalformedURLException me) 
		{
			System.out.println("MalformedURLException: " + me.getMessage());
		}catch (IOException ioe) 
		{
			System.out.println("IOException: " + ioe.getMessage());
		}
	}
	
	public boolean enviarMail(String server, String origen,String destinatario,String asunto,String cuerpo)
	{
		try
		{
			Properties props = new Properties();
			props.put("mail.smtp.host",server);
			Session sesion = Session.getInstance(props);
			MimeMessage mensaje = new MimeMessage(sesion);
			mensaje.setFrom(new InternetAddress(origen ));
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			mensaje.addRecipient(Message.RecipientType.BCC,new InternetAddress(origen)); 
			mensaje.setSubject(asunto);
			mensaje.setText(cuerpo); 
			
			try
			{
				Transport mta = sesion.getTransport("smtp");
				mta.connect();
	        
				try
				{
					Transport.send(mensaje);
					System.out.println("El mensaje ha sido enviado");
				}catch(SendFailedException ex)
				 {
					return false;
				 }
				
				mta.close();
			}catch(Exception ex)
			 {
				System.out.println("Cartero: Error al enviar "+ex.toString());
			 }
		}catch(Exception ex)
		 {
			return false;
		 }

		return true;
	}
	
	public boolean enviarMail(String server, String origen,String destinatario,String cc1,String cc2,String asunto,String cuerpo,String ruta)
	{
		try
		{
			Properties props = new Properties();
			props.put("mail.smtp.host",server);
			Session sesion = Session.getInstance(props);
			
			// Tanto el usuario como la clave son de la cuenta de correo que envía el mensaje.
			//sesion.setPasswordAuthentication(new URLName(server), new javax.mail.PasswordAuthentication("ramos.drixdel","drix"));
			//Store buzon=sesion.getStore("pop3");
			//buzon.connect("mail.telgua.com.gt", "ramos.drixdel", "drix");
			//buzon.close();
			MimeMessage mensaje = new MimeMessage(sesion);
			
			try
			{
				mensaje.setFrom(new InternetAddress(origen ));
				mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
				mensaje.addRecipient(Message.RecipientType.CC,new InternetAddress(cc1));
				mensaje.addRecipient(Message.RecipientType.CC,new InternetAddress(cc2));
				mensaje.addRecipient(Message.RecipientType.BCC,new InternetAddress(origen)); 
				mensaje.setSubject(asunto);
				
				MimeMultipart mm = new MimeMultipart();
				MimeBodyPart attach = new MimeBodyPart();
				MimeBodyPart mbp1 = new MimeBodyPart();
				
				FileDataSource fr = new FileDataSource(ruta);
				attach.setDataHandler(new DataHandler(fr));
				attach.setFileName(fr.getName());
				
				mbp1.setContent(cuerpo,"text/plain");
				mm.addBodyPart(attach);
				mm.addBodyPart(mbp1);
				
				mensaje.setContent(mm);
				mensaje.setSentDate(new java.util.Date());
				
			}catch(MessagingException ex)
			 {
				ex.printStackTrace();
			 }
		      
			try
			{
				Transport mta = sesion.getTransport("smtp");
				mta.connect();
				
				try
				{
					Transport.send(mensaje);
					System.out.println("El email ha sido enviado");
				}catch(SendFailedException ex)
				 {
					return false;
				 }
		        
				mta.close();
			}catch(Exception ex)
			 {
				System.out.println("Cartero: Error al enviar "+ex.toString());
			 }
		}catch(Exception ex)
		 {
			return false;
		 }

		return true;	
	}
}
