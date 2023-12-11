package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.services.LessonServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@AllArgsConstructor
public class LessonController {

    LessonServiceImpl lessonService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Lesson
    @PostMapping("/addLesson")
    public ResponseEntity<String> createLesson(@RequestBody Lesson lesson) {
        try {
            Lesson createdLesson = lessonService.createLesson(lesson);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Lesson créée avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
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

    //-----------------------------------CRUD ends-----------------------------------//
}

