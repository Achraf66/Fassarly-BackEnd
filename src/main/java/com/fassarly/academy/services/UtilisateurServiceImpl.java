package com.fassarly.academy.services;

import com.fassarly.academy.DTO.ComptabiliteDTO;
import com.fassarly.academy.config.AzureBlobStorageServiceImpl;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.interfaceServices.IUtilisateurService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    AppUserRepository appUserRepository;

    RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AzureBlobStorageServiceImpl azureBlobStorageService;


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
    @Transactional
    public void deleteUtilisateur(Long id) {

        String blobDirectoryPath = "Users/" + id + "/";
        azureBlobStorageService.deleteFolder(blobDirectoryPath);

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


    public AppUser updateUserById(
            Long userId, String password, String nomPrenom, String numeroTel,
            MultipartFile photoFile, Long roleId
    ) throws IOException {
        Optional<AppUser> appUserOptional = appUserRepository.findById(userId);

        if (appUserOptional.isEmpty()) {
            return null;
        }

        AppUser appUser = appUserOptional.get();
        appUser.setNomPrenom(nomPrenom);
        appUser.setNumeroTel(numeroTel);

        // If roleId is provided, update the user's role
        if (roleId != null) {
            // Find the UserRole based on roleId
            Optional<UserRole> roleOptional = roleRepository.findById(roleId);

            // Check if the role exists
            if (roleOptional.isPresent()) {
                // Create a new HashSet and add the found role
                Set<UserRole> updatedRoles = new HashSet<>();
                updatedRoles.add(roleOptional.get());

                // Set the user's roles to the new set
                appUser.setRoles(updatedRoles);
            } else {
                // Handle the case where the specified role does not exist
                // You can throw an exception, log an error, or handle it based on your requirements
                throw new RuntimeException("Role with ID " + roleId + " not found");
            }
        }

        // Update password if provided
        if (password != null) {
            appUser.setPassword(passwordEncoder.encode(password));
        }

        // Update photo if provided
        if (photoFile != null) {
                // Upload examFile to Azure Blob Storage
                String blobDirectoryPath = "Users/" + appUser.getId() + "/";
                // Upload examFile to Azure Blob Storage
                azureBlobStorageService.uploadBlob(blobDirectoryPath, photoFile);
                appUser.setPhoto(azureBlobStorageService.getBlobUrl(blobDirectoryPath, photoFile.getOriginalFilename()));
        }


        return appUserRepository.save(appUser);
    }



    public List<AppUser> searchUsers(String searchterm) {
        return appUserRepository.findByNomPrenomContainingIgnoreCaseOrNumeroTelContainingIgnoreCaseOrRolesDisplayNameContainingIgnoreCase(searchterm, searchterm, searchterm);
    }



    public AppUser findByNumeroTel (String numTel){
       return appUserRepository.findByNumeroTel(numTel).orElse(null);
    }








}
