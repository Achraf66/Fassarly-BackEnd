package com.fassarly.academy.auth;


import com.fassarly.academy.DTO.AuthenticationRequest;
import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.DTO.RegisterRequest;
import com.fassarly.academy.config.JwtService;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.RoleRepository;
import com.fassarly.academy.token.Token;
import com.fassarly.academy.token.TokenRepository;
import com.fassarly.academy.token.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.postgresql.util.PSQLException;

import java.util.Set;
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


    public AuthenticationResponse register(RegisterRequest request) throws PSQLException {

        try {
            //Get all the roles in database
            Set<UserRole> existingRoles = roleRepository.findUserRoleByNameIn(request.getRoles().stream()
                    .map(UserRole::getName)
                    .collect(Collectors.toSet()));


            // Check if all requested roles are found in existing roles
            Set<String> requestedRoleNames = request.getRoles().stream()
                    .map(UserRole::getName)
                    .collect(Collectors.toSet());


            if (!existingRoles.stream().map(UserRole::getName).toList().containsAll(requestedRoleNames)) {
                return AuthenticationResponse.builder()
                        .errormessage("Some roles are not valid.")
                        .build();
            }
            var user = AppUser.builder()
                    .nomPrenom(request.getFirstname() + " " + request.getLastname())
                    .numeroTel(request.getNumTel())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(existingRoles)
                    .build();
            var savedUser = repository.save(user);
            var jwtToken = jwtService.GenerateToken(user.getNumeroTel());
            var refreshToken = jwtService.GenerateToken(user.getNumeroTel());
            saveUserToken(savedUser, jwtToken);

            return AuthenticationResponse.builder()
                    .successmessage("Register Success")
                    .build();
        }catch (Exception e){
            return AuthenticationResponse.builder()
                    .successmessage("User Already Exisits")
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
                   .orElseThrow();
           var jwtToken = jwtService.GenerateToken(request.getNumtel());
//        var refreshToken = jwtService.gene(user);
//        revokeAllUserTokens(user);
           saveUserToken(user, jwtToken);
           return AuthenticationResponse.builder()
                   .accessToken(jwtToken)
                   .successmessage("Sucess login")
//                .refreshToken(refreshToken)
                   .build();
       }catch (BadCredentialsException e){

           return AuthenticationResponse.builder()
                   .errormessage("BadCredentials")
                   .build();
       }

       catch (UsernameNotFoundException e){
           return AuthenticationResponse.builder()
                   .errormessage("Username Not found")
                   .build();
       }
    }


    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


}
