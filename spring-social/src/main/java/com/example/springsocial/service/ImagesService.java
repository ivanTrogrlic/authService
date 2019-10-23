package com.example.springsocial.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public interface ImagesService {

    CompletableFuture<BufferedImage> fetchImageByUrl(String url);

    BufferedImage downsize(BufferedImage image);

    String base64Encode(BufferedImage image);
}
