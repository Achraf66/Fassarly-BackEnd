package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.services.MatiereServiceImpl;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Validated
@RestController
@RequestMapping("/api/matiere")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class MatiereController {

    MatiereServiceImpl matiereService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Matiere with image upload
    @PostMapping(value = "/addMatiere")
    public ResponseEntity<String> createMatiere(@RequestPart("matiere") Matiere matiere,@RequestPart("file") @Size(max = 3 * 1024 * 1024, message = "File size must not exceed 5MB") MultipartFile file
    ) {
        try {


            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Only image files are allowed\"}");
            }

            Matiere createdMatiere = matiereService.createMatiere(matiere, file);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Matiere créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Une erreur s'est produite\"}");
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

    // Update Matiere
    @PutMapping("/updateMatiere")
    public ResponseEntity<String> updateMatiere(@RequestBody Matiere matiere) {
        try {
            Matiere updatedMatiere = matiereService.updateMatiere(matiere);
            if (updatedMatiere != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Matiere mise à jour avec succès\"}");
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



    }
