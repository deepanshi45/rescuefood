package com.example.login.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Handles storage of uploaded food item images on local disk.
 *
 * The upload directory is configurable via `app.upload.dir` in
 * application.properties and defaults to "uploads/food-images"
 * (relative to the application's working directory), so it survives
 * application restarts and is NOT bundled inside target/classes.
 */
@Service
public class FileStorageService {

    // Only these image types are accepted (requirement 8: reject invalid types)
    private static final List<String> ALLOWED_CONTENT_TYPES =
            List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024; // 5 MB

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads/food-images}") String uploadDirProperty) {
        this.uploadDir = Paths.get(uploadDirProperty).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new FileStorageException(
                    "Could not create the upload directory: " + this.uploadDir, e);
        }
    }

    /**
     * Validates and stores an uploaded image.
     *
     * @param file the uploaded MultipartFile
     * @return the generated stored filename (e.g., "3f2a1c9e-....jpg") — NOT the full path
     * @throws FileStorageException if the file is empty, too large, an invalid type, or cannot be written
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("No image file was selected.");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new FileStorageException("Image is too large. Maximum allowed size is 5MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileStorageException(
                    "Invalid file type. Only JPG, PNG, GIF, and WEBP images are allowed.");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload");

        // Prevent path traversal via a crafted filename
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Filename contains an invalid path sequence: " + originalFilename);
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
        }

        // UUID-based filename avoids collisions and strips any unsafe characters from the original name
        String storedFilename = UUID.randomUUID() + extension;

        try {
            Path targetPath = this.uploadDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + storedFilename, e);
        }

        return storedFilename;
    }

    /**
     * Resolves a stored filename to its absolute path on disk, for serving.
     */
    public Path resolve(String filename) {
        return this.uploadDir.resolve(filename).normalize();
    }
}