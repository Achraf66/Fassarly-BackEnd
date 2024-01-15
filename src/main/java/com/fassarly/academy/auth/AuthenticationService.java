package com.fassarly.academy.auth;

import com.fassarly.academy.DTO.AuthenticationRequest;
import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.DTO.RegisterRequest;
import com.fassarly.academy.SmsToken.SmsRequest;
import com.fassarly.academy.config.JwtService;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.RoleRepository;
import com.fassarly.academy.services.OrangeSmsService;
import com.fassarly.academy.token.Token;
import com.fassarly.academy.token.TokenRepository;
import com.fassarly.academy.token.TokenType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final OrangeSmsService OrangeSmsService;
    private static final String BAD_CREDENTIALS_MESSAGE = "Bad Credentials";
    private static final String USERNAME_NOT_FOUND_MESSAGE = "Username Not Found";

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;



    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        try {
            Set<UserRole> existingRoles = getExistingRoles(request.getRoles().stream()
                    .map(UserRole::getName)
                    .collect(Collectors.toSet()));

            Set<String> requestedRoleNames = request.getRoles().stream()
                    .map(UserRole::getName)
                    .collect(Collectors.toSet());

            if (!existingRoles.stream().map(UserRole::getName).toList().containsAll(requestedRoleNames)) {
                return AuthenticationResponse.builder()
                        .errormessage("Some roles are not valid.")
                        .build();
            }

            var user = buildUser(request, existingRoles);
            String verificationCode = generateRandomCode();
            user.setVerificationCode(verificationCode);
            user.setSmsVerified(false);

            sendVerificationSms(user.getNumeroTel(), verificationCode);

            repository.save(user);

            var jwtToken = jwtService.GenerateToken(user.getNumeroTel());
            var refreshToken = jwtService.GenerateToken(user.getNumeroTel());
            // saveUserToken(savedUser, jwtToken);

            return AuthenticationResponse.builder()
                    .successmessage("Register Success")
                    .build();
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            return AuthenticationResponse.builder()
                    .errormessage("An error occurred during registration.")
                    .build();
        }
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getNumtel(),
                            request.getPassword()
                    )
            );

            var user = repository.findByNumeroTel(request.getNumtel())
                    .orElse(null);

            // Check if user already has an active token
            List<Token> userTokens = tokenRepository.findTokenByUser(user);
            if (!userTokens.isEmpty()) {
                return AuthenticationResponse.builder()
                        .errormessage("User Already logged in on another device")
                        .build();
            }


            if (!Boolean.TRUE.equals(user.isSmsVerified())) {
                return AuthenticationResponse.builder()
                        .errormessage("This User is not sms verified yet")
                        .build();
            }

            var accessToken = jwtService.GenerateToken(request.getNumtel());
            saveUserToken(user, accessToken);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .successmessage("Sucess login")
//                .refreshToken(refreshToken)
                    .build();

        } catch (BadCredentialsException e) {
            return AuthenticationResponse.builder()
                    .errormessage(BAD_CREDENTIALS_MESSAGE)
                    .build();
        } catch (UsernameNotFoundException e) {
            return AuthenticationResponse.builder()
                    .errormessage(USERNAME_NOT_FOUND_MESSAGE)
                    .build();
        }
    }


    public AuthenticationResponse logout(String username) {
        try {
            AppUser user = repository.findByNumeroTel(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<Token> userTokens = tokenRepository.findTokenByUser(user);

            if (userTokens.isEmpty()) {
                return AuthenticationResponse.builder()
                        .errormessage("User Already logged out")
                        .build();
            }
            // Revoke all tokens for the user (this is just an example, you may have a more sophisticated token management)
            jwtService.deleteAllUserTokens(user);

            return AuthenticationResponse.builder()
                    .successmessage("User logged Successfully")
                    .build();
        } catch (UsernameNotFoundException e) {
            return AuthenticationResponse.builder()
                    .successmessage("User not found")
                    .build();
        }
    }

    private Set<UserRole> getExistingRoles(Set<String> roleNames) {
        return roleRepository.findUserRoleByNameIn(roleNames);
    }

    private AppUser buildUser(RegisterRequest request, Set<UserRole> existingRoles) {
        return AppUser.builder()
                .nomPrenom(request.getFirstname() + " " + request.getLastname())
                .numeroTel(request.getNumTel())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(existingRoles)
                .build();
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        // Set the token expiration duration in milliseconds (e.g., 1 hour)
        long expirationDuration = jwtExpiration; // 1 hour in milliseconds

        // Calculate the expiration date
        long expirationTime = System.currentTimeMillis() + expirationDuration;

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expirationDate(new Date(expirationTime))
                .build();

        tokenRepository.save(token);
    }

    public AuthenticationResponse verifyCode(String phoneNumber, String enteredCode) {
        try {
            Optional<AppUser> optionalUser = repository.findByNumeroTel(phoneNumber);

            if (optionalUser.isPresent()) {
                AppUser user = optionalUser.get();

                if (enteredCode.equals(user.getVerificationCode())) {
                    // Verification code matches, update smsVerified to true
                    user.setSmsVerified(true);
                    repository.save(user);

                    return AuthenticationResponse.builder()
                            .successmessage("Verification successful. SMS is now verified.")
                            .build();
                } else {
                    return AuthenticationResponse.builder()
                            .errormessage("Incorrect verification code.")
                            .build();
                }
            } else {
                return AuthenticationResponse.builder()
                        .errormessage("User not found for phone number")
                        .build();
            }
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            return AuthenticationResponse.builder()
                    .errormessage("An error occurred during code verification.")
                    .build();
        }
    }

















    public static String generateRandomCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // Random number in the range [10000, 99999]
        return String.valueOf(code);
    }

    private void sendVerificationSms(String phoneNumber, String verificationCode) {
        SmsRequest smsRequest = SmsRequest.builder().number(phoneNumber).message(verificationCode).build();
        OrangeSmsService.sendSms(smsRequest);
    }

}
