package com.fassarly.academy.services;

import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClasseService {

    private final AppUserRepository appUserRepository;

    public List<AppUser> findUsersByRole(String roleName) {
        return appUserRepository.findByRolesNameContainingAndIsSmsVerified(roleName,true);
    }
    public AuthenticationResponse desactivateUsers() {
        List<AppUser> users = appUserRepository.findAll();
        for (AppUser user : users) {
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> "admin".equalsIgnoreCase(role.getName()));
            if (!isAdmin) {
                user.setAccountActivated(false);
            }
        }
        appUserRepository.saveAll(users);
        return AuthenticationResponse.builder()
                .successmessage("Users deactivated successfully")
                .build();
    }

    public AuthenticationResponse allAccountsActivate() {
        List<AppUser> users = appUserRepository.findAll();
        for (AppUser user : users) {
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> "admin".equalsIgnoreCase(role.getName()));
            if (!isAdmin) {
                user.setAccountActivated(true);
            }
        }
        appUserRepository.saveAll(users);
        return AuthenticationResponse.builder()
                .successmessage("Users activated successfully")
                .build();
    }

}