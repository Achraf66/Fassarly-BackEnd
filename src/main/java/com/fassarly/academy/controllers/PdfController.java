package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.entities.PrototypeExam;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.LessonRepository;
import com.fassarly.academy.repositories.PrototypeExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Paths.get;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_DISPOSITION;

@RestController
@CrossOrigin("*")
public class PdfController {
    @Value("${file.upload.directory}")
    private  String UPLOAD_DIRECTORY;


    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private PrototypeExamRepository prototypeExamRepository;



    private ResponseEntity<Resource> downloadFile(String filePath, String fileName) throws IOException {
        Path path = get(filePath + fileName);
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





/***************************Download Prototype Exam Files ***********************************/


@GetMapping("/download/prototypeExam/correction/{examenId}/{prototypeExamId}")
public ResponseEntity<Resource> downloadCorrectionFilePrototypeExam(@PathVariable Long examenId, @PathVariable Long prototypeExamId) throws IOException {

    Examen examen = examenRepository.findById(examenId).orElse(null);

    PrototypeExam prototypeExam = prototypeExamRepository.findById(prototypeExamId).orElse(null);
    if (prototypeExam == null | examen == null) {
        return ResponseEntity.notFound().build();
    }

    String directoryPath = UPLOAD_DIRECTORY + "/PrototypeExam/" + examen.getId() + "/" + prototypeExam.getId() + "/";

    String PrototypeCorrectionFileName = prototypeExam.getCorrectionFile();

    return downloadFile(directoryPath, PrototypeCorrectionFileName);
}


    @GetMapping("/download/prototypeExam/ExamPrototypeFile/{examenId}/{prototypeExamId}")
    public ResponseEntity<Resource> downloadExamPrototypeFileExam(@PathVariable Long examenId, @PathVariable Long prototypeExamId) throws IOException {

        Examen examen = examenRepository.findById(examenId).orElse(null);

        PrototypeExam prototypeExam = prototypeExamRepository.findById(prototypeExamId).orElse(null);
        if (prototypeExam == null | examen == null) {
            return ResponseEntity.notFound().build();
        }

        String directoryPath = UPLOAD_DIRECTORY + "/PrototypeExam/" + examen.getId() + "/" + prototypeExam.getId() + "/";

        String PrototypeExamFileName = prototypeExam.getExamFile();

        return downloadFile(directoryPath, PrototypeExamFileName);
    }

/*******************************lessonfile*************************************/
@GetMapping("/download/lessonfile/{filename}/{lessonId}")
public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename ,@PathVariable("lessonId") Long lessonId) throws IOException {

    Path filePath = get(UPLOAD_DIRECTORY, "lessons", String.valueOf(lessonId), "piecesJointes").toAbsolutePath().normalize().resolve(filename);

    if(!Files.exists(filePath)) {
        throw new FileNotFoundException(filename + " was not found on the server");
    }
    Resource resource = new UrlResource(filePath.toUri());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("File-Name", filename);
    httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
            .headers(httpHeaders).body(resource);
}

/*************************Downlaod homework For LIVE Session*********************************************************************/
@GetMapping("/download/LiveSessionHomeworkFile/{filename}/{liveSessionId}")
public ResponseEntity<Resource> downloadLiveSessionHomeworkFile(@PathVariable("filename") String filename ,@PathVariable("liveSessionId") Long liveSessionId) throws IOException {

    Path filePath =
            get(UPLOAD_DIRECTORY, "seanceEnLigne", String.valueOf(liveSessionId), "homeWork").toAbsolutePath().normalize().resolve(filename);

    if(!Files.exists(filePath)) {
        throw new FileNotFoundException(filename + " was not found on the server");
    }
    Resource resource = new UrlResource(filePath.toUri());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("File-Name", filename);
    httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
            .headers(httpHeaders).body(resource);
}


}