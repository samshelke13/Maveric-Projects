package com.maveric.mentee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {
	
	BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	
    public String encryptPassword(String password) {
        return bcrypt.encode(password);
   }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return bcrypt.matches(rawPassword, encodedPassword);
    }
}
