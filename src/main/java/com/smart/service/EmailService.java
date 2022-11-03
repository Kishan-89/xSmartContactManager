package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	
	public boolean sendEmail( String content,String subject,String to, String from) {
		
		boolean f =false;
		
// variable for gmail host
		String host ="smtp.gmail.com";
		
		// fetching system properties
		
		Properties properties = System.getProperties();
		System.out.println(properties);
		
		// setting inmportnt information to properties
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		// to get the session object
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("smart.contact.manager89@gmail.com", "Smart@12345");
			}
			
			
			
		});
		
		
		
		// compose the message
		
		session.setDebug(true);
		
		MimeMessage mimeMessage = new MimeMessage(session);
		
		try {
			mimeMessage.setFrom(from);
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			mimeMessage.setText(content);
			
			Transport.send(mimeMessage);
			
			f=true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		
		
		
		return f;
		
	}
	
}
