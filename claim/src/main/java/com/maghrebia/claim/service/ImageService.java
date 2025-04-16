package com.maghrebia.claim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String UPLOAD_DIR = "uploads/claims";

    public String saveImage(String imageBase64){
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

            String fileName = System.currentTimeMillis() + ".png";
            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, imageBytes);

            return filePath.toString();
        } catch (IOException e) {
            return "";
        }
    }
    public static String convertImageToBase64(String filename) {
        try {
            Path path = Paths.get(filename);
            byte[] imageBytes = Files.readAllBytes(path);
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return null; // Handle errors properly
        }
    }
}
