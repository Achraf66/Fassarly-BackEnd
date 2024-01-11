package com.fassarly.academy.auth;

import com.fassarly.academy.config.JwtService;
import com.fassarly.academy.token.Token;
import com.fassarly.academy.token.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TokenCleanupTask {

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private  TokenRepository tokenRepository;

    public TokenCleanupTask(JwtService jwtService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour, adjust the rate as needed
    public void cleanupExpiredTokens() {
        // Get all tokens from the repository
        List<Token> allTokens = tokenRepository.findAll();

        // Iterate through tokens and delete expired ones
        for (Token token : allTokens) {
            if (token.getExpirationDate().before(new Date())) {
                // Delete the token from the repository
                tokenRepository.delete(token);
            }
        }
    }

}
