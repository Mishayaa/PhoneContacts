package com.example.PhoneContacts.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JwtConfig {
    public static final long TOKEN_EXPIRATION_MS = 86400000; // 24 hours
    public static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    public static final String SECRET_KEY = base64EncodedSecretKey;
}