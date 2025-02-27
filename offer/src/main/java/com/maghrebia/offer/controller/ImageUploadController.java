package com.maghrebia.offer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    @Value("${application.upload.path}")
    private String UPLOAD_DIR;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                System.out.println(uploadPath.toAbsolutePath());
                Files.createDirectories(uploadPath);
            }

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Save the file to the upload directory
            Files.copy(file.getInputStream(), filePath);

            // Return the file URL (you can customize this based on your server setup)
            String fileUrl = "http://localhost:9002/api/v1/images/" + fileName;
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            System.out.println("Error while uploading image");
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }
}
