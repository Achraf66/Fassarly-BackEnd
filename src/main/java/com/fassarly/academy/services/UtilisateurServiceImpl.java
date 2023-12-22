package com.fassarly.academy.services;

import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.DTO.ComptabiliteDTO;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.Comptabilite;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.interfaceServices.IUtilisateurService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    AppUserRepository appUserRepository;

    RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;


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

    public List<ComptabiliteDTO> getComptabilitesByUserId(Long userId) {
        return appUserRepository.findById(userId)
                .map(AppUser::getComptabilites)
                .orElse(Collections.emptyList())
                .stream()
                .map(comptabilite -> modelMapper.map(comptabilite, ComptabiliteDTO.class))
                .collect(Collectors.toList());
    }

}
