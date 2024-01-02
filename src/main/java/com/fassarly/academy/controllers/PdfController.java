package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@CrossOrigin("http://localhost:4200/")
public class PdfController {
    @Value("${file.upload.directory}")
    private  String UPLOAD_DIRECTORY;


    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private LessonRepository lessonRepository;

    /***************Download Correction Examen by MatiereID / ExamenID *************/
    @GetMapping("/download/exam/correction/{matiereId}/{examenId}")
    public ResponseEntity<Resource> downloadCorrectionFile(@PathVariable Long matiereId, @PathVariable Long examenId) throws IOException {
        Examen examen = examenRepository.findById(examenId).orElse(null);

        if (examen == null) {
            return ResponseEntity.notFound().build();
        }

        String filePath = UPLOAD_DIRECTORY + "/Exams/" + examenId + "/";
        String correctionFileName = examen.getCorrection();

        return downloadFile(filePath, correctionFileName);
    }

    @GetMapping("/download/exam/piecesjointes/{matiereId}/{examenId}")
    public ResponseEntity<Resource> downloadPiecesJointesExamen(@PathVariable Long matiereId, @PathVariable Long examenId) throws IOException {
        Examen examen = examenRepository.findById(examenId).orElse(null);

        if (examen == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> pieceJointesList = examen.getPieceJointes();

        if (pieceJointesList == null || pieceJointesList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Assuming you want to zip the files before downloading
        String zipFileName = examen.getNomExamen() + "_pieces_jointes.zip";
        String encodedZipFileName = new String(zipFileName.getBytes("UTF-8"), "ISO-8859-1"); // Encode the filename

        String zipFilePath = UPLOAD_DIRECTORY + "/Exams/" + examenId + "/" + zipFileName;

        // Create a zip file containing all pieces jointes
        createZipFile(zipFilePath, pieceJointesList, matiereId, examen);

        // Provide the zip file for download
        Path zipPath = Paths.get(zipFilePath);
        Resource resource = new FileSystemResource(zipPath.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedZipFileName)
                .body(resource);
    }



    @GetMapping("/download/lesson/piecesjointes/{themeId}/{lessonId}")
    public ResponseEntity<Resource> downloadPiecesJointesLesson(@PathVariable Long themeId, @PathVariable Long lessonId) throws IOException {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> pieceJointesList = lesson.getPiecesJointes();

        if (pieceJointesList == null || pieceJointesList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Assuming you want to zip the files before downloading
        String zipFileName = lesson.getNomLesson() + "_pieces_jointes.zip";
        String encodedZipFileName = new String(zipFileName.getBytes("UTF-8"), "ISO-8859-1"); // Encode the filename

        String zipFilePath = UPLOAD_DIRECTORY + "/lessons/" + lessonId + "/" + zipFileName;

        // Create a zip file containing all pieces jointes
        createZipFileLesson(zipFilePath, pieceJointesList, themeId, lesson);

        // Provide the zip file for download
        Path zipPath = Paths.get(zipFilePath);
        Resource resource = new FileSystemResource(zipPath.toFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedZipFileName)
                .body(resource);
    }



    private void createZipFile(String zipFilePath, List<String> fileNames, Long matiereId, Examen examen) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path baseDirectory = Paths.get(UPLOAD_DIRECTORY, "Exams", String.valueOf(examen.getId()), "piecesJointes");

            for (String fileName : fileNames) {
                Path filePath = baseDirectory.resolve(fileName);

                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOutputStream.putNextEntry(zipEntry);

                    Files.copy(filePath, zipOutputStream);

                    zipOutputStream.closeEntry();
                } else {
                    // Log the file path if it doesn't exist
                    System.err.println("File not found: " + filePath);
                }
            }
        }
    }




    private void createZipFileLesson(String zipFilePath, List<String> fileNames, Long matiereId, Lesson lesson) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path baseDirectory = Paths.get(UPLOAD_DIRECTORY, "lessons", String.valueOf(lesson.getId()), "piecesJointes");

            for (String fileName : fileNames) {
                Path filePath = baseDirectory.resolve(fileName);

                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOutputStream.putNextEntry(zipEntry);

                    Files.copy(filePath, zipOutputStream);

                    zipOutputStream.closeEntry();
                } else {
                    // Log the file path if it doesn't exist
                    System.err.println("File not found: " + filePath);
                }
            }
        }
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