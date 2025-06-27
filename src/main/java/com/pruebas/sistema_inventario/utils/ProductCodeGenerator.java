package com.pruebas.sistema_inventario.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.pruebas.sistema_inventario.repository.ProductRepository;

import lombok.Builder;

@Component @Builder
public class ProductCodeGenerator {
	
	private ProductRepository productRepository;
	
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    
    // --- Method For Generating Unique Product Codes --- ///
    public String generateUserCode(String username) {
    	// Extract the first 3 characters of the username
    	String userPrefix = username.substring(0, Math.min(3, username.length())).toUpperCase();
    	String code;
    	do {
    		// UUID short
        	String uuid = generateRandomAlphanumeric(8);
        	code = userPrefix + '-' + uuid;
    	} while (productRepository.existsByCode(code));
    	return code;
    }
	
	// --- Auxiliary Method For Generating Unique Product Codes --- ///
	private String generateRandomAlphanumeric(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < 8; i++) {
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}
	
}
