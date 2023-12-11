package com.fassarly.academy.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageUploadService {
    public String uploadImage(MultipartFile file, String uploadDir) {
        try {
            // Generate a unique filename for the uploaded file
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Copy the file to the specified upload directory
            Path filePath = Path.of(uploadDir).resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path to the uploaded file
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
}
