package com.example.springsocial.exception;

public class ImageProcessingFailedException extends RuntimeException {
    public ImageProcessingFailedException(String msg) {
        super(msg);
    }
}
