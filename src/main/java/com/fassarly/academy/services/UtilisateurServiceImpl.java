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
        return appUserRepository.findByIsSmsVerifiedTrue();
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
            MultipartFile photoFile, Long roleId,Boolean smsVerified,
            Boolean accountActivated
    ) throws IOException {
        Optional<AppUser> appUserOptional = appUserRepository.findById(userId);

        if (appUserOptional.isEmpty()) {
            return null;
        }
        AppUser appUser = appUserOptional.get();
        appUser.setNomPrenom(nomPrenom);
        appUser.setNumeroTel(numeroTel);
        appUser.setSmsVerified(smsVerified);
        appUser.setAccountActivated(accountActivated);
        if (roleId != null) {
            Optional<UserRole> roleOptional = roleRepository.findById(roleId);
            if (roleOptional.isPresent()) {
                Set<UserRole> updatedRoles = new HashSet<>();
                updatedRoles.add(roleOptional.get());
                appUser.setRoles(updatedRoles);
            } else {
                throw new RuntimeException("Role with ID " + roleId + " not found");
            }
        }
        if (password != null) {
            appUser.setPassword(passwordEncoder.encode(password));
        }
        if (photoFile != null) {
                String blobDirectoryPath = "Users/" + appUser.getId() + "/";
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
