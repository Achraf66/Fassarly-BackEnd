package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.services.SeanceEnLigneServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seanceEnLigne")
@AllArgsConstructor
@CrossOrigin("*")
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
    public ResponseEntity<?> createSeanceEnLigneAndAffectToMatiere(
            @PathVariable("matiereId") Long matiereId,
            @RequestParam("date") LocalDateTime date,
            @RequestParam("heureDebut") String heureDebut,
            @RequestParam("heureFin") String heureFin,
            @RequestParam("titre") String titre,
            @RequestParam("lienZoom") String lienZoom,
            @RequestParam(value = "homeWorkFile", required = false) MultipartFile homeWorkFile
    ) {
        try {
            SeanceEnLigne seanceEnLigne = new SeanceEnLigne();
            seanceEnLigne.setDate(date);
            seanceEnLigne.setHeureDebut(heureDebut);
            seanceEnLigne.setHeureFin(heureFin);
            seanceEnLigne.setTitre(titre);
            seanceEnLigne.setLienZoom(lienZoom);

            SeanceEnLigne createdSeance = seanceEnLigneService.createSeanceEnLigneAndAffectToMatiere(matiereId, seanceEnLigne, homeWorkFile);

            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    @PutMapping("/editSeanceEnLigne/{seanceEnLigneId}")
    public ResponseEntity<?> editSeanceEnLigne(
            @PathVariable("seanceEnLigneId") Long seanceEnLigneId,
            @RequestParam("date") LocalDateTime date,
            @RequestParam("heureDebut") String heureDebut,
            @RequestParam("heureFin") String heureFin,
            @RequestParam("titre") String titre,
            @RequestParam("lienZoom") String lienZoom,
            @RequestParam(value = "homeWorkFile", required = false) MultipartFile homeWorkFile
    ) {
        try {
            SeanceEnLigne seanceEnLigne = new SeanceEnLigne();
            seanceEnLigne.setDate(date);
            seanceEnLigne.setHeureDebut(heureDebut);
            seanceEnLigne.setHeureFin(heureFin);
            seanceEnLigne.setTitre(titre);
            seanceEnLigne.setLienZoom(lienZoom);

            SeanceEnLigne createdSeance = seanceEnLigneService.editSeanceEnLigne(seanceEnLigneId, seanceEnLigne, homeWorkFile);

            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"SeanceEnLigne créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    @DeleteMapping("/deleteLiveSessinById/{liveSessionId}")
    public ResponseEntity<?> deleteSeanceEnLigneById(@PathVariable Long liveSessionId) {
        Map<String, String> response = new HashMap<>();

        try {
            seanceEnLigneService.deleteSeanceEnLigneById(liveSessionId);
            response.put("successmessage", "LiveSession deleted successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("errormessage", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("errormessage", "Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}