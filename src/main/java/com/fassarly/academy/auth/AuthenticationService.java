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
import java.text.SimpleDateFormat;

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


    @Value("${Sender}")
    private  String SENDER;

    @Value("${SmsApiKey}")
    private String SMSAPIKEY;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        try {

            // Check if the phone number already exists
            if (repository.existsByNumeroTel(request.getNumTel())) {
                return AuthenticationResponse.builder()
                        .errormessage("Phone number is already registered.")
                        .build();
            }

            // Fetch existing roles based on requested roles
            Set<String> requestedRoleNames = request.getRoles().stream()
                    .map(UserRole::getName)
                    .collect(Collectors.toSet());

            Set<UserRole> existingRoles = getExistingRoles(requestedRoleNames);

            // Validate roles
            if (!existingRoles.stream().map(UserRole::getName).toList().containsAll(requestedRoleNames)) {
                return AuthenticationResponse.builder()
                        .errormessage("Some roles are not valid.")
                        .build();
            }

            // Create the User
            var user = buildUser(request, existingRoles);
            String verificationCode = generateRandomCode();
            user.setVerificationCode(verificationCode);
            user.setSmsVerified(false);

            // Prepare SMS request
            SmsRequest smsRequest = new SmsRequest();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            smsRequest.setDate(dateFormat.format(currentDate));
            smsRequest.setHeure(timeFormat.format(currentDate));
            smsRequest.setSender(SENDER);
            smsRequest.setApiKey(SMSAPIKEY);
            smsRequest.setMobileNumber("216" + user.getNumeroTel());
            smsRequest.setSmsContent("Merci d'avoir souscrit à Fassarly. Voici votre code :\n" + verificationCode);

            // Uncomment to send SMS
            // OrangeSmsService.sendSms(smsRequest);

            // Save user
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateWithTime = formatter.format(new Date());
            user.setDateCreation(dateWithTime);
            user.setAccountActivated(true);

            repository.save(user);

            return AuthenticationResponse.builder()
                    .successmessage("Register Success")
                    .build();
        } catch (Exception e) {
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
//            List<Token> userTokens = tokenRepository.findTokenByUser(user);
//            if (!userTokens.isEmpty()) {
//                return AuthenticationResponse.builder()
//                        .errormessage("User Already logged in on another device")
//                        .build();
//            }


            if (!Boolean.TRUE.equals(user.isSmsVerified())) {
                return AuthenticationResponse.builder()
                        .errormessage("This User is not sms verified yet")
                        .build();
            }

            if (!Boolean.TRUE.equals(user.isAccountActivated())) {
                return AuthenticationResponse.builder()
                        .errormessage("This User disabled by Admin")
                        .build();
            }

            jwtService.deleteAllUserTokens(user);

            var accessToken = jwtService.GenerateToken(request.getNumtel());

            saveUserToken(user, accessToken);

            return

                    AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .successmessage("Sucess login")
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


    @Transactional
    public AuthenticationResponse reSendVerificationCode(String numTel) {
        try {
            // Check if the user exists
            Optional<AppUser> userOptional = repository.findByNumeroTel(numTel);
            if (userOptional.isEmpty()) {
                return AuthenticationResponse.builder()
                        .errormessage("This user does not exist.")
                        .build();
            }

            AppUser user = userOptional.get();

            // Check if user is already verified
            if (user.isSmsVerified()) {
                return AuthenticationResponse.builder()
                        .errormessage("This user is already verified.")
                        .build();
            }

            // Generate new verification code
            String verificationCode = generateRandomCode();
            user.setVerificationCode(verificationCode);
            user.setSmsVerified(false);

            // Prepare and send SMS
            SmsRequest smsRequest = new SmsRequest();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            smsRequest.setDate(dateFormat.format(currentDate));
            smsRequest.setHeure(timeFormat.format(currentDate));
            smsRequest.setSender(SENDER);
            smsRequest.setApiKey(SMSAPIKEY);
            smsRequest.setMobileNumber("216" + user.getNumeroTel());
            smsRequest.setSmsContent("Merci d'avoir souscrit à Fassarly. Voici votre code :\n" + verificationCode);

            // Uncomment to send SMS
//             OrangeSmsService.sendSms(smsRequest);

            // Save user
            repository.save(user);

            return AuthenticationResponse.builder()
                    .successmessage("Verification code resent successfully.")
                    .build();
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
            return AuthenticationResponse.builder()
                    .errormessage("An error occurred while resending verification code.")
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

                if (user.isSmsVerified()) {
                    // User is already verified
                    return AuthenticationResponse.builder()
                            .errormessage("User is already verified.")
                            .build();
                }

                if (enteredCode.equals(user.getVerificationCode())) {
                    // Verification code matches, update smsVerified to true
                    user.setSmsVerified(true);
                    repository.save(user);
                    var accessToken = jwtService.GenerateToken(user.getNumeroTel());
                    saveUserToken(user, accessToken);

                    return AuthenticationResponse.builder()
                            .successmessage("Verification successful. SMS is now verified.")
                            .accessToken(accessToken)
                            .build();
                } else {
                    return AuthenticationResponse.builder()
                            .errormessage("Incorrect verification code.")
                            .build();
                }
            } else {
                return AuthenticationResponse.builder()
                        .errormessage("User not found for phone number.")
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


}
