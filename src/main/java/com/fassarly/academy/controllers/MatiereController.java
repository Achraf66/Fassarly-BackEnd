package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.services.MatiereServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Validated
@RestController
@RequestMapping("/api/matiere")
@AllArgsConstructor

@CrossOrigin("*")
public class MatiereController {

    MatiereServiceImpl matiereService;



    //-----------------------------------CRUD begins-----------------------------------//

    // Create Matiere with image upload
    @PostMapping(value = "/addMatiere")
    public ResponseEntity<String> createMatiere
    (@RequestParam("nomMatiere") String nomMatiere,@RequestParam("file") MultipartFile file) {
        try {
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Only image files are allowed\"}");
            }
            matiereService.createMatiere(nomMatiere, file);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Matiere créée avec succès\"}");
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Image too large\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Matieres
    @GetMapping("/getAllMatieres")
    public ResponseEntity<?> readAllMatieres() {
        try {
            List<Matiere> allMatieres = matiereService.readAllMatiere();
            return ResponseEntity.ok(allMatieres);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Matiere by ID
    @GetMapping("/getMatiere/{id}")
    public ResponseEntity<?> readMatiere(@PathVariable Long id) {
        try {
            Matiere matiere = matiereService.readMatiere(id);
            if (matiere != null) {
                return ResponseEntity.ok(matiere);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Matiere found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Matiere by ID
    @DeleteMapping("/removeMatiere/{id}")
    public ResponseEntity<String> deleteMatiere(@PathVariable Long id) {
        try {
            matiereService.deleteMatiere(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Matiere supprimée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//

    //------------Rechercher Matiere par nom------------//
    @GetMapping("/getMatiereByNom/{nomMatiere}")
    public ResponseEntity<?> readMatiereByNom(@PathVariable String nomMatiere) {
        try {
            List matieresfounds = new ArrayList();
            Matiere matiere = matiereService.findByNomMatiere(nomMatiere);
            if (matiere != null) {
                matieresfounds.add(matiere);
                return ResponseEntity.ok(matieresfounds);
            } else {
                return ResponseEntity.ok(matieresfounds);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    @GetMapping("/findMatiereByUser/{numtel}")
    public ResponseEntity<List<Matiere>> findMatiereByUser(@PathVariable("numtel") String numtel) {

        return ResponseEntity.ok(matiereService.findMatiereByUser(numtel));

    }


    @PutMapping("/updateMatiere/{matiereId}")
    public ResponseEntity<Map<String, String>> updateMatiere(
            @PathVariable Long matiereId,
            @RequestParam String nomMatiere,
            @RequestParam(required = false) MultipartFile file) {
        try {
            Matiere updatedMatiere = matiereService.updateMatiere(matiereId, nomMatiere, file);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Matiere updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error updating Matiere");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<Matiere>> searchMatiereByNom(@RequestParam("searchTerm") String searchTerm) {
        try {
            List<Matiere> result = matiereService.searchMatiereByNom(searchTerm);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
