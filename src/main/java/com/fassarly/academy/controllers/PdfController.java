package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.repositories.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin("http://localhost:4200/")
public class PdfController {
    @Value("${file.upload.directory}")
    private  String UPLOAD_DIRECTORY;


    @Autowired
    private ExamenRepository examenRepository;

    /***************Download Correction Examen by MatiereID / ExamenID *************/
    @GetMapping("/download/exam/correction/{matiereId}/{examenId}")
    public ResponseEntity<Resource> downloadCorrectionFile(@PathVariable Long matiereId, @PathVariable Long examenId) throws IOException {
        Examen examen = examenRepository.findById(examenId).orElse(null);

        if (examen == null) {
            // Handle the case where Examen is not found, perhaps throw an exception or return an error response
            return ResponseEntity.notFound().build();
        }

        String filePath = UPLOAD_DIRECTORY + "/Exams/" + matiereId + examen.getNomExamen() + "/";
        String correctionFileName = examen.getCorrection();

        return downloadFile(filePath, correctionFileName);
    }

    private ResponseEntity<Resource> downloadFile(String filePath, String fileName) throws IOException {
        Path path = Paths.get(filePath + fileName);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            // Handle the case where the file is not found, perhaps throw an exception or return an error response
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF) // Adjust the content type as needed
                .body(resource);
    }




}