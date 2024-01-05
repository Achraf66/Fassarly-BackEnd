package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.services.SeanceEnLigneServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seanceEnLigne")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class SeanceEnLigneController {

    SeanceEnLigneServiceImpl seanceEnLigneService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create SeanceEnLigne
    @PostMapping("/addSeanceEnLigne")
    public ResponseEntity<String> createSeanceEnLigne(@RequestBody SeanceEnLigne seanceEnLigne) {
        try {
            SeanceEnLigne createdSeanceEnLigne = seanceEnLigneService.createSeanceEnLigne(seanceEnLigne);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All SeanceEnLignes
    @GetMapping("/getAllSeanceEnLignes")
    public ResponseEntity<?> readAllSeanceEnLignes() {
        try {
            List<SeanceEnLigne> allSeanceEnLignes = seanceEnLigneService.readAllSeanceEnLigne();
            return ResponseEntity.ok(allSeanceEnLignes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read SeanceEnLigne by ID
    @GetMapping("/getSeanceEnLigne/{id}")
    public ResponseEntity<?> readSeanceEnLigne(@PathVariable Long id) {
        try {
            SeanceEnLigne seanceEnLigne = seanceEnLigneService.readSeanceEnLigne(id);
            if (seanceEnLigne != null) {
                return ResponseEntity.ok(seanceEnLigne);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No SeanceEnLigne found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update SeanceEnLigne
    @PutMapping("/updateSeanceEnLigne")
    public ResponseEntity<String> updateSeanceEnLigne(@RequestBody SeanceEnLigne seanceEnLigne) {
        try {
            SeanceEnLigne updatedSeanceEnLigne = seanceEnLigneService.updateSeanceEnLigne(seanceEnLigne);
            if (updatedSeanceEnLigne != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne mise à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No SeanceEnLigne found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete SeanceEnLigne by ID
    @DeleteMapping("/removeSeanceEnLigne/{id}")
    public ResponseEntity<String> deleteSeanceEnLigne(@PathVariable Long id) {
        try {
            seanceEnLigneService.deleteSeanceEnLigne(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne supprimée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//

    @GetMapping("/getSessionLiveByMatiereId/{matiereId}")
    public ResponseEntity<?> getSessionLiveByMatiereId(@PathVariable("matiereId") Long matiereId) {
        try {
            List<SeanceEnLigne> seanceEnLignes = seanceEnLigneService.getSessionLiveByMatiereId(matiereId);
            if (seanceEnLignes != null) {
                return ResponseEntity.ok(seanceEnLignes);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No SeanceEnLigne found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    @PostMapping("/createSeanceEnLigneAndAffectToMatiere/{matiereId}")
        public ResponseEntity<?> createSeanceEnLigneAndAffectToMatiere(@PathVariable("matiereId") Long matiereId, @RequestBody SeanceEnLigne seanceEnLigne) {
            try {
                seanceEnLigneService.createSeanceEnLigneAndAffectToMatiere(matiereId, seanceEnLigne);
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne créée avec succès\"}");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
            }
        }

    @PutMapping("/editSeanceEnLigne/{seanceEnLigneId}")
    public ResponseEntity<?> editSeanceEnLigne(@PathVariable("seanceEnLigneId") Long seanceEnLigneId, @RequestBody SeanceEnLigne seanceEnLigne) {
        try {
            seanceEnLigneService.editSeanceEnLigne(seanceEnLigneId, seanceEnLigne);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }



}