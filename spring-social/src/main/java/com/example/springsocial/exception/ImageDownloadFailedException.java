package com.example.springsocial.exception;

public class ImageDownloadFailedException extends RuntimeException {

    public ImageDownloadFailedException(String msg) {
        super(msg);
    }

    public ImageDownloadFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
