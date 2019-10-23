package com.example.springsocial.service.impl;

import com.example.springsocial.exception.ImageDownloadFailedException;
import com.example.springsocial.exception.ImageProcessingFailedException;
import com.example.springsocial.service.ImagesService;
import com.sun.imageio.plugins.bmp.BMPCompressionTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ImagesServiceImpl implements ImagesService {

    @Value("image.scaling.coefficient")
    private Float scale;

    @Override
    public CompletableFuture<BufferedImage> fetchImageByUrl(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ImageIO.read(new URL(url));
            } catch (MalformedURLException e) {
                log.error("Provided malformed image url: " + url, e);
                throw new ImageDownloadFailedException("Provided malformed image url", e);
            } catch (IOException e) {
                log.error("Failed to download image from url: " + url, e);
                throw new ImageDownloadFailedException("Failed to download image", e);
            }
        });
    }

    @Override
    public BufferedImage downsize(BufferedImage image) {
        int scaledHeight = Math.round(image.getHeight() * scale);
        int scaledWidth = Math.round(image.getWidth() * scale);
        BufferedImage outImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        Graphics2D graphics2D = outImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return outImage;
    }


    @Override
    public String base64Encode(BufferedImage image) {
        ByteArrayOutputStream binaryImageStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", binaryImageStream);
        } catch (IOException e) {
            throw new ImageProcessingFailedException("Image base64 encoding failed");
        }
        return Base64.getUrlEncoder().encodeToString(binaryImageStream.toByteArray());
    }
}
