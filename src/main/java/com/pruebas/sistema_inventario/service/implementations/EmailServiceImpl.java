package com.pruebas.sistema_inventario.service.implementations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {
	
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	public EmailServiceImpl(JavaMailSender mailSender , SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}
	
	public void sendRegistrationEmail(String to, String name) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of("name", name, "dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
		String htmlContent = templateEngine.process("email/registration", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("¡Register successful! - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendAcceptUserEmail(String to, String name) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of("name", name, "dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
		String htmlContent = templateEngine.process("email/registration-accepted", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Your account has been accepted! - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendRejectUserEmail(String to, String name) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of("name", name, "dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
		String htmlContent = templateEngine.process("email/registration-rejected", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("¡We're sorry! - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendPasswordChangeEmail(String to, String name) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of("name", name, "dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
		String htmlContent = templateEngine.process("email/password-changed", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Password Change Notification - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendEmailChangeEmail(String to, String name, String oldEmail, String newEmail) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of(
				"name", name, 
				"dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 
				"oldEmail", oldEmail,
				"newEmail", newEmail
		));
		String htmlContent = templateEngine.process("email/email-changed", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Email Change Notification - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendWorkplaceChangedEmail(String to, String name, String oldWorkplace, String newWorkplace) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of(
				"name", name, 
				"dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 
				"oldWorkplace", oldWorkplace,
				"newWorkplace", newWorkplace
		));
		String htmlContent = templateEngine.process("email/workplace-changed", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Workplace Change Notification - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
	public void sendPrivilegedUpdatedEmail(String to, String name, String oldPrivileges, String newPrivileges) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of(
				"name", name, 
				"dateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), 
				"oldPrivileges", oldPrivileges,
				"newPrivileges", newPrivileges
		));
		String htmlContent = templateEngine.process("email/privileges-changed", context);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Privileges Updated Notification - Inventory System");
		helper.setText(htmlContent, true);
		
		mailSender.send(message);
	}
	
}
