package com.fassarly.academy.services;

import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.interfaceServices.IUtilisateurService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    AppUserRepository appUserRepository;

    RoleRepository roleRepository;

    @Override
    public AppUser createUtilisateur(AppUser appUser) {

        Set<UserRole> existingRoles = roleRepository.findUserRoleByNameIn(
                appUser.getRoles().stream()
                        .map(UserRole::getName)
                        .collect(Collectors.toSet())
        );

        appUser.setRoles(existingRoles);

        return appUserRepository.save(appUser);
    }
    @Override
    public List<AppUser> readAllUtilisateur() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser readUtilisateur(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser updateUtilisateur(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        appUserRepository.deleteById(id);
    }
}
