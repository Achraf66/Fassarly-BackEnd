package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Comptabilite;
import com.fassarly.academy.services.ComptabiliteserviceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptabilite")
@AllArgsConstructor
public class ComptabiliteController {

    ComptabiliteserviceImpl comptabiliteService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Comptabilite
    @PostMapping("/addComptabilite")
    public ResponseEntity<String> createComptabilite(@RequestBody Comptabilite comptabilite) {
        try {
            Comptabilite createdComptabilite = comptabiliteService.createComptabilite(comptabilite);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Comptabilite créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Comptabilites
    @GetMapping("/getAllComptabilites")
    public ResponseEntity<?> readAllComptabilites() {
        try {
            List<Comptabilite> allComptabilites = comptabiliteService.readAllComptabilite();
            return ResponseEntity.ok(allComptabilites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Comptabilite by ID
    @GetMapping("/getComptabilite/{id}")
    public ResponseEntity<?> readComptabilite(@PathVariable Long id) {
        try {
            Comptabilite comptabilite = comptabiliteService.readComptabilite(id);
            if (comptabilite != null) {
                return ResponseEntity.ok(comptabilite);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Comptabilite found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Comptabilite
    @PutMapping("/updateComptabilite")
    public ResponseEntity<String> updateComptabilite(@RequestBody Comptabilite comptabilite) {
        try {
            Comptabilite updatedComptabilite = comptabiliteService.updateComptabilite(comptabilite);
            if (updatedComptabilite != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Comptabilite mise à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Comptabilite found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Comptabilite by ID
    @DeleteMapping("/removeComptabilite/{id}")
    public ResponseEntity<String> deleteComptabilite(@PathVariable Long id) {
        try {
            comptabiliteService.deleteComptabilite(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Comptabilite supprimée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//


    @PostMapping("createAndAffectComtabiliteToUser")
    public ResponseEntity<String> createAndAffectComtabiliteToUser
            (@RequestParam("paye") float paye,@RequestParam("nonPaye") float nonPaye,@RequestParam("idUser")Long idUser ,@RequestParam("idMatiere") Long idMatiere)
    {
        return  ResponseEntity.status(HttpStatus.OK).body(comptabiliteService.createAndAffectComptabiliteToUser(paye,nonPaye,idUser,idMatiere));
    }
}
