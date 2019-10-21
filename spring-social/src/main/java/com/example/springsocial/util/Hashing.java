package com.example.springsocial.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hashing {

    public static String sha256(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytesToHash = text.getBytes(StandardCharsets.UTF_8);
        byte[] hashedBytes = digest.digest(bytesToHash);
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

}
