package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.repositories.AppUserRepository;
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

import java.io.IOException;
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

    @Autowired
    AppUserRepository appUserRepository;

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

    @GetMapping("/userimage/{userId}/{filename:.+}")
    public ResponseEntity<Resource> serveUserImage(@PathVariable Long userId, @PathVariable String filename) {
        // Assuming you have a method to retrieve user details from the repository
        AppUser appUser = appUserRepository.findById(userId).orElse(null);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(uploadDirectory, "Users", appUser.getId().toString()).resolve(filename);

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



    /********************Get the photo of the user By Id ***********************************/
    @GetMapping("/{userId}/image")
    public ResponseEntity<Resource> getUserImage(@PathVariable Long userId) {
        // Get the user by ID (you need to implement this method in your service)
        AppUser appUser = appUserRepository.findById(userId).orElse(null);

        if (appUser == null || appUser.getPhoto() == null || appUser.getPhoto().isEmpty()) {
            // Return a placeholder image or an error response
            // Customize this based on your requirements
            return ResponseEntity.notFound().build();
        }

        // Construct the file path to the user's image
        String userFolderImagePath = uploadDirectory + "/Users/" + userId;
        Path filePath = Paths.get(userFolderImagePath).resolve(appUser.getPhoto());

        try {
            // Load the file as a Resource
            Resource resource = loadFileAsResource(filePath);

            // Set Content-Disposition header to force the browser to download the file
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust the MediaType based on your image type
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            // Handle exceptions (e.g., file not found, etc.)
            return ResponseEntity.notFound().build();
        }
    }

    public Resource loadFileAsResource(Path filePath) throws MalformedURLException {
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + filePath.toString());
        }
    }






















}
