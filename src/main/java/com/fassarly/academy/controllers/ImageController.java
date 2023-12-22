package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.repositories.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {


    @Autowired
    MatiereRepository matiereRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    //-----------------------------------EndPoint for the Image of Subject With 2 params Subjectname and filename-----------------------------------//

    @GetMapping("/matiereimage/{matiereName}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String matiereName, @PathVariable String filename) {

        Matiere m = matiereRepository.findByNomMatiere(matiereName);

        Path filePath = Paths.get(uploadDirectory+"\\matieres", m.getId().toString()).resolve(filename);
        try {
            Resource file = new UrlResource(filePath.toUri());
            MediaType mediaType = determineMediaType(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                    .body(file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    //-----------------------------------Verify the type of the image JPEG PNG JPG-----------------------------------//

    private MediaType determineMediaType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
