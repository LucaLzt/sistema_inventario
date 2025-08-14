package com.pruebas.sistema_inventario.exceptions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
		model.addAttribute("status", "404");
		model.addAttribute("error", "Entity Not Found");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("hour", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return "error/error-personalized";
	}
	
	@ExceptionHandler(PasswordException.class)
	public String handlePasswordException(PasswordException ex, Model model) {
		model.addAttribute("status", "400");
		model.addAttribute("error", "Password Error");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("hour", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return "error/error-personalized";
	}
	
	@ExceptionHandler(EmailException.class)
	public String handleEmailException(EmailException ex, Model model) {
		model.addAttribute("status", "400");
		model.addAttribute("error", "Email Error");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("hour", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return "error/error-personalized";
	}
	
	@ExceptionHandler(ActiveUserException.class)
	public String handleActiveUserException(ActiveUserException ex, Model model) {
		model.addAttribute("status", "400");
		model.addAttribute("error", "Active User Error");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("hour", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return "error/error-personalized";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
		model.addAttribute("status", "400");
		model.addAttribute("error", "Bad Request");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("hour", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		return "error/error-personalized";
	}
	
}
