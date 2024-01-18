package com.fassarly.academy.controllers;


import com.fassarly.academy.entities.PrototypeExam;
import com.fassarly.academy.services.PrototypeExamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prototypeExam")
@CrossOrigin("*")
public class PrototypeExamController {

    @Autowired
    private PrototypeExamServiceImpl prototypeExamService;

    @PostMapping("/createAndAffectPrototypeExam")
    public ResponseEntity<PrototypeExam> createAndAffectPrototypeExamToExamen(
            @RequestParam("nomPrototypeExam") String nomPrototypeExam,
            @RequestParam(value = "examFile", required = false) MultipartFile examFile,
            @RequestParam(value = "correctionFile", required = false) MultipartFile correctionFile,
            @RequestParam("correctionLink") String correctionLink,
            @RequestParam("examenId") Long examenId
    ) {
        try {
            PrototypeExam prototypeExam = prototypeExamService.createAndAffectPrototypeExamToExamen(
                    nomPrototypeExam, examFile, correctionFile, correctionLink, examenId);
            return ResponseEntity.ok(prototypeExam);
        } catch (IOException e) {
            // Handle IOException (e.g., log it and return an error response)
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            // Handle IllegalArgumentException (e.g., examen not found) and return an error response
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/edit/{prototypeExamId}")
    public ResponseEntity<PrototypeExam> editPrototypeExam(
            @PathVariable("prototypeExamId") Long prototypeExamId,
            @RequestParam("nomPrototypeExam") String nomPrototypeExam,
            @RequestParam(value = "examFile", required = false) MultipartFile examFile,
            @RequestParam(value = "correctionFile", required = false) MultipartFile correctionFile,
            @RequestParam("correctionLink") String correctionLink) {
        try {
            PrototypeExam editedPrototypeExam = prototypeExamService.editPrototypeExam(
                    prototypeExamId, nomPrototypeExam, examFile, correctionFile, correctionLink);

            return ResponseEntity.ok(editedPrototypeExam);
        } catch (IOException e) {
            // Handle IOException (e.g., log it and return an error response)
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            // Handle IllegalArgumentException (e.g., prototypeExam not found) and return an error response
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/prototype-exams/{id}")
    public ResponseEntity<Map<String, String>> deletePrototypeExam(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            prototypeExamService.deletePrototypeExam(id);
            response.put("successmessage", "PrototypeExam deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("errormessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("errormessage", "An error occurred while deleting the PrototypeExam");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/prototype-exams/examen/{examenId}")
    public ResponseEntity<List<PrototypeExam>> getPrototypeExamsByExamenId(@PathVariable Long examenId) {
        try {
            List<PrototypeExam> prototypeExams = prototypeExamService.getPrototypeExamsByExamenId(examenId);
            return ResponseEntity.status(HttpStatus.OK).body(prototypeExams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/prototype-exams/{id}")
    public ResponseEntity<PrototypeExam> getPrototypeExamById(@PathVariable Long id) {
        try {
            Optional<PrototypeExam> prototypeExam = prototypeExamService.getPrototypeExamById(id);
            if (prototypeExam.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(prototypeExam.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
