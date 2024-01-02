package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.services.LessonServiceImpl;
import jakarta.transaction.Transactional;
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
@RequestMapping("/api/lesson")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class LessonController {

    LessonServiceImpl lessonService;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Lesson
    @PostMapping("/addLesson")
    public ResponseEntity<String> createLesson(@RequestBody Lesson lesson) {
        try {
            lessonService.createLesson(lesson);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson créée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Lessons
    @GetMapping("/getAllLessons")
    public ResponseEntity<?> readAllLessons() {
        try {
            List<Lesson> allLessons = lessonService.readAllLesson();
            return ResponseEntity.ok(allLessons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Lesson by ID
    @GetMapping("/getLesson/{id}")
    public ResponseEntity<?> readLesson(@PathVariable Long id) {
        try {
            Lesson lesson = lessonService.readLesson(id);
            if (lesson != null) {
                return ResponseEntity.ok(lesson);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Lesson found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Lesson
    @PutMapping("/updateLesson")
    public ResponseEntity<String> updateLesson(@RequestBody Lesson lesson) {
        try {
            Lesson updatedLesson = lessonService.updateLesson(lesson);
            if (updatedLesson != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson mise à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Lesson found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Lesson by ID
    @DeleteMapping("/removeLesson/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson supprimée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    @Transactional
    @DeleteMapping("/delete/lesson/{lessonId}")
    public ResponseEntity<String> deleteLessonById(@PathVariable Long lessonId) {
            Lesson lesson = lessonService.readLesson(lessonId);
            if (lesson == null) {
                return ResponseEntity.notFound().build();
            }
            try {
                // Delete the examen folder
                String examenFolderPath = uploadDirectory + "/lessons/" + lesson.getId() ;
                FileUtils.deleteDirectory(new File(examenFolderPath));

                // Delete the examen entry from the database
                lessonService.deleteLesson(lessonId);

                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson supprimé avec succès\"}");
            } catch (IOException e) {
                e.printStackTrace(); // Log the exception
                // If deletion fails, you might want to rollback the deletion of the folder
                // and return an appropriate response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to delete examen\"}");
            }
    }


    //-----------------------------------CRUD ends-----------------------------------//


    @PostMapping("/createLessonAndAffectToTheme/{idTheme}")
    public ResponseEntity<?> createLessonAndAffectToTheme(
            @RequestParam("nomLesson") String nomLesson,@RequestParam("videoLien") String videoLien, @RequestParam("description") String description,
            @RequestParam(value = "piecesJointes",required = false)  List<MultipartFile> piecesJointes, @PathVariable("idTheme") Long idTheme)
    {
        try {
            lessonService.createLessonAndAffectToTheme(nomLesson, videoLien, description, piecesJointes, idTheme);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson créée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    @GetMapping("/getLessonsByTheme/{idTheme}")
    public ResponseEntity<?> getLessonsByTheme(@PathVariable("idTheme") Long idTheme) {

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getLessonsByTheme(idTheme));

    }
    @PutMapping("/editLesson/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @RequestParam String nomLesson,
            @RequestParam String videoLien,
            @RequestParam String description,
            @RequestParam(required = false) List<MultipartFile> piecesJointes
    ) {
        try {
            Lesson updatedLesson = lessonService.updateLesson(lessonId, nomLesson, videoLien, description, piecesJointes);
            if (updatedLesson != null) {
                return ResponseEntity.ok(updatedLesson);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}

