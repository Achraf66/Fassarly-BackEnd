package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.services.UtilisateurServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/utilisateur")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class UtilisateurController {

    UtilisateurServiceImpl utilisateurService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Utilisateur
    @PostMapping("/addUtilisateur")
    public ResponseEntity<String> createUtilisateur(@RequestBody AppUser appUser) {
        try {
            AppUser createdAppUser = utilisateurService.createUtilisateur(appUser);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Utilisateur créé avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
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
}

