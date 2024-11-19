package com.maveric.mentee.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityConfigTest {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    public void testEncryptPassword() {
        String rawPassword = "mySecretPassword";
        String encodedPassword = securityConfig.encryptPassword(rawPassword);

        assertTrue(bcrypt.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testMatchPassword() {
        String rawPassword = "mySecretPassword";
        String encodedPassword = securityConfig.encryptPassword(rawPassword);

        assertTrue(securityConfig.matchPassword(rawPassword, encodedPassword));
    }

}
