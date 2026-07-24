package com.example.login.service;

/**
 * Thrown when a file (e.g., a food item image) cannot be validated,
 * stored, or read from disk. Kept as an unchecked exception so it can
 * bubble up through service methods without changing existing method
 * signatures.
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
