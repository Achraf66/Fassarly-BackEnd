package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Abonnement;
import com.fassarly.academy.services.AbonnementServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonnement")
@AllArgsConstructor
public class AbonnementController {

    AbonnementServiceImpl abonnementService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Abonnement
    @PostMapping("/addAbonnement")
    public ResponseEntity<String> createAbonnement(@RequestBody Abonnement abonnement) {
        try {
            Abonnement createdAbonnement = abonnementService.createAbonnement(abonnement);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Abonnement créé avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Abonnements
    @GetMapping("/getAllAbonnements")
    public ResponseEntity<?> readAllAbonnements() {
        try {
            List<Abonnement> allAbonnements = abonnementService.readAllAbonnement();
            return ResponseEntity.ok(allAbonnements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Abonnement by ID
    @GetMapping("/getAbonnement/{id}")
    public ResponseEntity<?> readAbonnement(@PathVariable Long id) {
        try {
            Abonnement abonnement = abonnementService.readAbonnement(id);
            if (abonnement != null) {
                return ResponseEntity.ok(abonnement);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Abonnement found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Abonnement
    @PutMapping("/updateAbonnement")
    public ResponseEntity<String> updateAbonnement(@RequestBody Abonnement abonnement) {
        try {
            Abonnement updatedAbonnement = abonnementService.updateAbonnement(abonnement);
            if (updatedAbonnement != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Abonnement mis à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Abonnement found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Abonnement by ID
    @DeleteMapping("/removeAbonnement/{id}")
    public ResponseEntity<String> deleteAbonnement(@PathVariable Long id) {
        try {
            abonnementService.deleteAbonnement(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Abonnement supprimé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//
}

