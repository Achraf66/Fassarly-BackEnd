package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.services.ExamenServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/examen/")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class ExamenController {

    ExamenServiceImpl examenService;

    @Value("${file.upload.directory}")
    private  String UPLOAD_DIRECTORY;


    //-----------------------------------CRUD begins-----------------------------------//

    // Create Examen
    @PostMapping("/addExamen")
    public ResponseEntity<String> createExamen(@RequestBody Examen examen) {
        try {
            Examen createdExamen = examenService.createExamen(examen);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Examen créé avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Examens
    @GetMapping("/getAllExamens")
    public ResponseEntity<?> readAllExamens() {
        try {
            List<Examen> allExamens = examenService.readAllExamen();
            return ResponseEntity.ok(allExamens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Examen by ID
    @GetMapping("/getExamen/{id}")
    public ResponseEntity<?> readExamen(@PathVariable Long id) {
        try {
            Examen examen = examenService.readExamen(id);
            if (examen != null) {
                return ResponseEntity.ok(examen);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Examen found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Examen
    @PutMapping("/updateExamen")
    public ResponseEntity<String> updateExamen(@RequestBody Examen examen) {
        try {
            Examen updatedExamen = examenService.updateExamen(examen);
            if (updatedExamen != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Examen mis à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Examen found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Examen by ID
    @DeleteMapping("/removeExamen/{id}")
    public ResponseEntity<String> deleteExamen(@PathVariable Long id) {
        try {
            examenService.deleteExamen(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Examen supprimé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//


    @GetMapping("findExamenByMatiereId/{matiereId}")
    public ResponseEntity<List<Examen>> findExamenByMatiereId(@PathVariable("matiereId") Long matiereId) {
        List<Examen> exams = examenService.findExamenByMatieresId(matiereId);


            return ResponseEntity.ok(exams);

    }

    @PostMapping("/create")
    public ResponseEntity<Examen> createExamenAndAffectToMatiere(
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("nomExamen") String nomExamen) {

        try {
            Examen examen = examenService.createExamenAndAffectToMatiere(
                    matiereId,
                    nomExamen);
            return ResponseEntity.ok(examen);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("fetchExamenById/{idExamen}")
    public Examen fetchExamenById(@PathVariable("idExamen") Long idExamen){
        return examenService.fetchExamenById(idExamen);
    }

    @PutMapping("/edit/{examenId}")
    public ResponseEntity<Examen> editExamen(
            @PathVariable Long examenId,
            @RequestParam String nomExamen) {
        try {
            Examen updatedExamen = examenService.editExamen(examenId, nomExamen);
            return ResponseEntity.ok(updatedExamen);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/exam/{examenId}")
    public ResponseEntity<String> deleteExamenById(@PathVariable Long examenId) {
        Examen examen = examenService.fetchExamenById(examenId);

        if (examen == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Delete the examen folder
            String examenFolderPath = UPLOAD_DIRECTORY + "/Exams/" + examen.getId() ;
            FileUtils.deleteDirectory(new File(examenFolderPath));

            // Delete the examen entry from the database
            examenService.deleteExamen(examenId);

            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Examen supprimé avec succès\"}");
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception

            // If deletion fails, you might want to rollback the deletion of the folder
            // and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete examen");
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<Examen>> searchExamens(@RequestParam String partialNomExamen) {
        List<Examen> result = examenService.searchByPartialNomExamen(partialNomExamen);
        return ResponseEntity.ok(result);
    }

}

