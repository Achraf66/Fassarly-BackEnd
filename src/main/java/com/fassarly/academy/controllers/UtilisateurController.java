package com.fassarly.academy.controllers;

import com.fassarly.academy.DTO.ComptabiliteDTO;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.services.UtilisateurServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
@AllArgsConstructor
@CrossOrigin("*")
public class UtilisateurController {

    UtilisateurServiceImpl utilisateurService;


    //-----------------------------------CRUD begins-----------------------------------//

    // Create Utilisateur
    @PostMapping("/addUtilisateur")
    public ResponseEntity<String> createUtilisateur(@RequestBody AppUser appUser) {
        try {
             utilisateurService.createUtilisateur(appUser);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Utilisateur créé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Utilisateurs
    @GetMapping("/getAllUtilisateurs")
    public ResponseEntity<?> readAllUtilisateurs() {
        try {
            List<AppUser> allAppUsers = utilisateurService.readAllUtilisateur();
            return ResponseEntity.ok(allAppUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Utilisateur by ID
    @GetMapping("/getUtilisateur/{id}")
    public ResponseEntity<?> readUtilisateur(@PathVariable Long id) {
        try {
            AppUser appUser = utilisateurService.readUtilisateur(id);
            if (appUser != null) {
                return ResponseEntity.ok(appUser);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Utilisateur found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Utilisateur
    @PutMapping("/updateUtilisateur")
    public ResponseEntity<String> updateUtilisateur(@RequestBody AppUser appUser) {
        try {
            AppUser updatedAppUser = utilisateurService.updateUtilisateur(appUser);
            if (updatedAppUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Utilisateur mis à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Utilisateur found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Utilisateur by ID
    @DeleteMapping("/removeUtilisateur/{id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Utilisateur supprimé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//


    @GetMapping("/getComptabilitesByUserId/{userId}")
    public ResponseEntity<List<ComptabiliteDTO>> getComptabilitesByUserId(@PathVariable Long userId) {
        List<ComptabiliteDTO> comptabilites = utilisateurService.getComptabilitesByUserId(userId);

        if (!comptabilites.isEmpty()) {
            return ResponseEntity.ok(comptabilites);
        } else {
            return ResponseEntity.notFound().build();
        }
    }






    @PutMapping("/updateUserById/{userId}")
    public ResponseEntity<AppUser> updateUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String password,
            @RequestParam String nomPrenom,
            @RequestParam String numeroTel,
            @RequestParam(required = false) MultipartFile photoFile,
            @RequestParam(required = false) Long roleId){
        try {
            AppUser updatedUser = utilisateurService.updateUserById(userId, password, nomPrenom, numeroTel, photoFile,roleId);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppUser>> searchUsers(@RequestParam("searchterm") String searchterm) {
        List<AppUser> searchResults = utilisateurService.searchUsers(searchterm);

        if (searchResults.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @GetMapping("/findByNumeroTel/{numTel}")
    public AppUser findByNumeroTel(@PathVariable("numTel") String numTel){
        return utilisateurService.findByNumeroTel(numTel);
    }






}


